package at.ac.tuwien.rep;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.rep.dao.ResourceAllocationRepository;
import at.ac.tuwien.rep.dao.ResourceNominationRepository;
import at.ac.tuwien.rep.model.ResourceAllocation;
import at.ac.tuwien.rep.model.ResourceDirection;
import at.ac.tuwien.rep.model.ResourceNomination;

@Component
public class NominationMatcher {

	private ResourceNominationRepository nominationRepository;
	private ResourceAllocationRepository allocationRepository;

	@Autowired
	public NominationMatcher(ResourceNominationRepository nominationRepository, ResourceAllocationRepository allocationRepository) {
		this.nominationRepository = nominationRepository;
		this.allocationRepository = allocationRepository;
	}

	public synchronized void matchNominations() {
		List<ResourceAllocation> allocations = allocationRepository.findAll();
		List<ResourceNomination> nominations = nominationRepository.findAllWithNoAllocation();
		for (ResourceAllocation allocation : allocations) {
			Iterator<ResourceNomination> iter = nominations.iterator();
			ResourceNomination allocatedNomination = allocation.getNomination();
			
			BigDecimal allocatableQuantity = allocatedNomination.getQuantity().subtract(allocation.getMatchedNominations().stream().map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
			if(allocatableQuantity.equals(BigDecimal.ZERO)) {
				System.out.println("Skipping allocation with id " + allocation.getId() + " because its quantity is already fully allocated.");
				continue;
			} else {
				System.out.println("Trying to find nominations for allocation " + toString(allocatedNomination));
			}
			while(iter.hasNext()) {
				ResourceNomination nomination = iter.next();
				if(!allocatedNomination.getResource().equals(nomination.getResource()) || allocatedNomination.getDirection().equals(nomination.getDirection())) {
					continue;
				}
				if(allocatableQuantity.subtract(nomination.getQuantity()).signum() != -1) {
					allocatableQuantity = allocatableQuantity.subtract(nomination.getQuantity());
					System.out.println("Allocating demand " + nomination.getId() + " to offer " + allocatedNomination.getId());
					allocation.getMatchedNominations().add(nomination);
					iter.remove();
				}
			}
			allocationRepository.save(allocation);
		}
		Map<String, List<ResourceNomination>> nominationsGroupedByResource = nominations.stream().collect(Collectors.groupingBy(ResourceNomination::getResource));
		for (Map.Entry<String, List<ResourceNomination>> nominationGroup : nominationsGroupedByResource.entrySet()) {
			Map<ResourceDirection, List<ResourceNomination>>  nominationsGroupedByDirection = nominationGroup.getValue().stream().collect(Collectors.groupingBy(ResourceNomination::getDirection));
			if(nominationsGroupedByDirection.size() != 2) {
				continue;
			}
			List<ResourceNomination> offers = nominationsGroupedByDirection.get(ResourceDirection.OFFER).stream().sorted(new DescendingNominationComparator()).collect(Collectors.toList());
			List<ResourceNomination> demands = nominationsGroupedByDirection.get(ResourceDirection.DEMAND).stream().sorted(new DescendingNominationComparator()).collect(Collectors.toList());
			Iterator<ResourceNomination> demandIter = demands.iterator();
			Map<ResourceNomination, List<ResourceNomination>> matchedNominations = new HashMap<>();
			while(demandIter.hasNext()) {
				ResourceNomination demand = demandIter.next();
				Iterator<ResourceNomination> offerIter = offers.iterator();
				while(offerIter.hasNext()) {
					ResourceNomination offer = offerIter.next();
					List<ResourceNomination> matches = matchedNominations.get(offer);
					if(matches == null) {
						matches = new LinkedList<>();
						matchedNominations.put(offer, matches);
					}
					BigDecimal allocatableQuantity = offer.getQuantity().subtract(matches.stream().map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
					if(allocatableQuantity.subtract(demand.getQuantity()).signum() != -1) {
						System.out.println("Allocating demand " + demand.getId() + " to offer" + offer.getId());
						matches.add(demand);
						break;
					}
				}
			}
			for (Map.Entry<ResourceNomination, List<ResourceNomination>> matchedNomination : matchedNominations.entrySet()) {
				ResourceAllocation newAllocation = new ResourceAllocation();
				ResourceNomination offer = matchedNomination.getKey();
				newAllocation.setNomination(offer);
				newAllocation.setMatchedNominations(matchedNomination.getValue());
				System.out.println("Creating new allocation for offer " + offer.getId() + " for demands " + String.join(", ", matchedNomination.getValue().stream().map(n -> n.getId().toString()).collect(Collectors.toList())));
				allocationRepository.save(newAllocation);
				offer.setAllocation(newAllocation);
				nominationRepository.save(offer);
				// TODO create allocation for demands and offers
			}
		}
	}

	private String toString(ResourceNomination nomination) {
		return "[resource=" + nomination.getResource() + ", quantity=" + nomination.getQuantity() 
				+ ", unit=" + nomination.getUnit() + ", direction=" + nomination.getDirection() + "]";
	}

	public static class NominationMatcherThread implements Runnable {
		private NominationMatcher matcher;

		public NominationMatcherThread(NominationMatcher matcher) {
			this.matcher = matcher;
		}

		@Override
		public void run() {
			System.out.println("Starting matching.");
			matcher.matchNominations();
			System.out.println("Finished matching.");
		}
	}
	
	private static class DescendingNominationComparator implements Comparator<ResourceNomination> {

		@Override
		public int compare(ResourceNomination o1, ResourceNomination o2) {
			return o2.getQuantity().compareTo(o1.getQuantity());
		}
		
	}
}
