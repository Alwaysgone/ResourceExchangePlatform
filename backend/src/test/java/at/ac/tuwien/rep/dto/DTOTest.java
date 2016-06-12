package at.ac.tuwien.rep.dto;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import at.ac.tuwien.rep.Main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Main.class)
public class DTOTest {
	
	@Test
	public void test() throws JsonProcessingException {
		ResourceRequestDTO dto = new ResourceRequestDTO();
		dto.setParticipant("participant1");
		List<ResourceNominationDTO> nominations = new LinkedList<>();
		ResourceNominationDTO nomination1 = new ResourceNominationDTO();
		nomination1.setQuantity(BigDecimal.TEN);
		nomination1.setDirection("OFFER");
		nomination1.setResource("oil");
		nomination1.setRegion("BVG");
		nominations.add(nomination1);
		ResourceNominationDTO nomination2 = new ResourceNominationDTO();
		nomination2.setQuantity(new BigDecimal("5"));
		nomination2.setDirection("DEMAND");
		nomination2.setResource("oil");
		nomination2.setRegion("BVG");
		nominations.add(nomination2);
		dto.setNominations(nominations);
		ObjectMapper mapper = new ObjectMapper();
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto));
	}
}
