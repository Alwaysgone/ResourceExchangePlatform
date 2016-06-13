package at.ac.tuwien.rep;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import at.ac.tuwien.rep.dao.ResourceNominationRepository;
import at.ac.tuwien.rep.model.ResourceDirection;
import at.ac.tuwien.rep.model.ResourceNomination;
import at.ac.tuwien.rep.model.ResourceRegion;

@Component
public class NominationMatcher {
	private ResourceNominationRepository nominationRepository;

	@Autowired
	public NominationMatcher(ResourceNominationRepository nominationRepository) {
		this.nominationRepository = nominationRepository;
	}

	public synchronized void matchNominations() {
		List<ResourceNomination> nominations = nominationRepository.findAll();
		nominations.forEach(n -> n.getMatchedNominations().clear());
		Map<ResourceRegion, List<ResourceNomination>> nominationsGroupedByRegion = nominations.stream().collect(Collectors.groupingBy(ResourceNomination::getRegion));
		for (Map.Entry<ResourceRegion, List<ResourceNomination>> nominationRegionGroup : nominationsGroupedByRegion.entrySet()) {
			Map<String, List<ResourceNomination>> nominationsGroupedByResource = nominationRegionGroup.getValue().stream().collect(Collectors.groupingBy(ResourceNomination::getResource));
			for (Map.Entry<String, List<ResourceNomination>> nominationGroup : nominationsGroupedByResource.entrySet()) {
				Map<ResourceDirection, List<ResourceNomination>>  nominationsGroupedByDirection = nominationGroup.getValue().stream().collect(Collectors.groupingBy(ResourceNomination::getDirection));
				if(nominationsGroupedByDirection.size() != 2) {
					continue;
				}
				List<ResourceNomination> offers = nominationsGroupedByDirection.get(ResourceDirection.OFFER).stream().sorted(new NominationComparator()).collect(Collectors.toList());
				List<ResourceNomination> demands = nominationsGroupedByDirection.get(ResourceDirection.DEMAND).stream().sorted(new NominationComparator()).collect(Collectors.toList());
				Iterator<ResourceNomination> demandIter = demands.iterator();
				while(demandIter.hasNext()) {
					ResourceNomination demand = demandIter.next();
					Iterator<ResourceNomination> offerIter = offers.iterator();
					List<ResourceNomination> offerCache = new LinkedList<>();
					while(offerIter.hasNext()) {
						ResourceNomination offer = offerIter.next();
						BigDecimal allocatableQuantity = offer.getQuantity().subtract(offer.getMatchedNominations().stream().map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
						if(allocatableQuantity.compareTo(BigDecimal.ZERO) == 0) {
							continue;
						}
						if(allocatableQuantity.subtract(demand.getQuantity()).signum() != -1) {
							System.out.println("Allocating demand " + demand.getId() + " to offer " + offer.getId());
							offer.getMatchedNominations().add(demand);
							nominationRepository.saveAndFlush(offer);
							demand.getMatchedNominations().add(offer);
							nominationRepository.saveAndFlush(demand);
							offerCache.clear();
							break;
						} else {
							offerCache.add(offer);
							BigDecimal quantites = offerCache.stream().map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
							BigDecimal allocatedQuantites = offerCache.stream().flatMap(o -> o.getMatchedNominations().stream()).map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
							allocatableQuantity = quantites.subtract(allocatedQuantites);
							if(allocatableQuantity.subtract(demand.getQuantity()).signum() != -1) {
								System.out.println("Allocating demand " + demand.getId() + " to offers " + String.join(", ", offerCache.stream().map(o -> o.getId().toString()).collect(Collectors.toList())));
								offerCache.forEach(o -> {o.getMatchedNominations().add(demand); demand.getMatchedNominations().add(nominationRepository.saveAndFlush(o));});
								nominationRepository.saveAndFlush(demand);
								offerCache.clear();
							}
						}
					}
				}
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

	private static class NominationComparator implements Comparator<ResourceNomination> {

		@Override
		public int compare(ResourceNomination o1, ResourceNomination o2) {
			if(!o1.getMatchedNominations().isEmpty() || !o2.getMatchedNominations().isEmpty()) {
				BigDecimal allocatable1 = o1.getQuantity().subtract(o1.getMatchedNominations().stream().map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
				BigDecimal allocatable2 = o1.getQuantity().subtract(o2.getMatchedNominations().stream().map(n -> n.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
				return allocatable2.compareTo(allocatable1);
			} else if(!o1.getMatchedNominations().isEmpty()) {
				return -1;
			} else if(!o2.getMatchedNominations().isEmpty()) {
				return 1;
			} else {
				return o2.getQuantity().compareTo(o1.getQuantity());
			}
		}

	}
}
