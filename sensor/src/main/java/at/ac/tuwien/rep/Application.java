package at.ac.tuwien.rep;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import at.ac.tuwien.rep.dto.ResourceNominationDTO;
import at.ac.tuwien.rep.dto.ResourceRequestDTO;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		String baseUrl = getBaseUrl();
		while(true) {
			ResponseEntity<String[]> regions = restTemplate.getForEntity(baseUrl + "/regions", String[].class);
			ResourceRequestDTO requestDto = new ResourceRequestDTO();
			requestDto.setParticipant(getParticipant());
			requestDto.setNominations(getNominations(Arrays.asList(regions.getBody()), getResources()));
			ResponseEntity<Long[]> ids = restTemplate.postForEntity(baseUrl + "/nominations", requestDto, Long[].class);
			System.out.println("Posted " + requestDto.getNominations().size() + " nominations for participant "
					+ requestDto.getParticipant() + " ids: "
					+ Arrays.asList(ids.getBody()).stream().map(i -> i.toString()).collect(Collectors.joining(", ")));
			Thread.sleep(10_000L);
		}
	}

	private String getBaseUrl() {
		String baseUrl = "";
		return (baseUrl = System.getProperty("baseUrl") != null ? baseUrl : "http://192.168.99.100:8080/api");
	}

	private List<String> getResources() {
		return Arrays.asList("Oil", "Bricks", "Wood", "Coal");
	}

	private List<ResourceNominationDTO> getNominations(List<String> regions, List<String> resources) {
		List<ResourceNominationDTO> dtos = new LinkedList<>();
		Random random = new Random();
		for(int i = 0; i < random.nextInt(5) + 1; i++) {
			ResourceNominationDTO dto = new ResourceNominationDTO();
			dto.setRegion(regions.get(random.nextInt(regions.size())));
			dto.setQuantity(new BigDecimal(random.nextInt(1000) + 1));
			String resource = resources.get(random.nextInt(resources.size()));
			dto.setResource(resource);
			String unit;
			switch(resource) {
			case "Oil":
				unit = "l";
				break;
			case "Bricks":
				unit = "pcs";
				break;
			case "Wood":
				unit = "t";
				break;
			case "Coal":
				unit = "t";
				break;
			default: unit = "";
			break;
			}
			dto.setUnit(unit);
			dto.setDirection(random.nextBoolean() ? "OFFER" : "DEMAND");
			dtos.add(dto);
		}
		return dtos;
	}

	private String getParticipant() {
		String participant = "";
		return (participant = System.getProperty("participant") != null ? participant : "participant1");
	}
}
