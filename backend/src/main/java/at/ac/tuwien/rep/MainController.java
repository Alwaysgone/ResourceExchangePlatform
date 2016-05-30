package at.ac.tuwien.rep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import at.ac.tuwien.rep.dao.ResourceRepository;

@RestController
@RequestMapping(path="/")
public class MainController {
	
	private ResourceRepository resourceRepository;
	
	@Autowired
	public MainController(ResourceRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String getHelloWorld() {
		return "Hello World!";
	}
	
	@RequestMapping(path="/resources", method = RequestMethod.GET)
	public String getNumberOfResources() {
		return "Found " + resourceRepository.findAll().size() + " resources";
	}
	
	@RequestMapping(value="/", method=RequestMethod.GET, produces={"application/xml", "application/rdf+xml"})
    public String getModelAsXml() {
       // Note that we added "application/rdf+xml" as one of the supported types
       // for this method. Otherwise, we utilize your existing xml serialization
		return "";
    }
}
