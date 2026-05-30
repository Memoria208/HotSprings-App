package hot.spring.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import hot.spring.controller.model.HotSpringData;
import hot.spring.controller.model.SoakerData;
import hot.spring.service.HotSpringService;
import lombok.extern.slf4j.Slf4j;

/* tell Spring that this is a REST controller- every method will return a
 * 200 or OK status by default, we are expecting JSON to come into the method 
 * and will return JSON as well
 * 
 * tell it how to map the URIs into the methods 
 * 
 * add logger */

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/hot_spring")
@Slf4j
public class HotSpringController {
	
	/*Create service class
	 * 
	 * We want Spring to manage this object by making it a managed bean */
	
	@Autowired
	private HotSpringService hotSpringService;

	/*tell Spring to map POST. This method is going to get a POST request to 
	 * "/hot_spring/soaker" */
	
	@PostMapping("/soaker")
	@ResponseStatus(code = HttpStatus.CREATED)
	public SoakerData insertSoaker(
			@RequestBody SoakerData soakerData) {
		
		/*{} is how you specify a replaceable parameter*/
		
		log.info("Creating soaker {}", soakerData);
		return hotSpringService.saveSoaker(soakerData);
	}
	
	/*Add update method right after the insert method because they’re very similar.*/
	/*Takes a //(@PathVariable Long soakerId,*/
	/*And a payload/body // @RequestBody SoakerData soakerData)*/
	/*Add @PutMapping (“/inside will be the resource/{and the resource ID}”)*/
	/*Set SoakerId inside the SoakerData object just to make sure it is always 
	 * set.*/
	/*Log it. */
	/*Call the same save method on the service that we called when we were inserting a 
	 * Soaker.*/
	
	/*"/table_name/{javaFieldName}*/
	@PutMapping("/soaker/{soakerId}")
	public SoakerData updateSoaker(@PathVariable Long soakerId, 		
			@RequestBody SoakerData soakerData) {
		soakerData.setSoakerId(soakerId);
		log.info("Updating soaker {}", soakerData);
		return hotSpringService.saveSoaker(soakerData);
	}
	
	/*write Soaker method. public, returns List of SoakerData called 
	 * retrieveAllSoakers, takes no parameters*/
	/*add @GetMapping because this is a GET request*/
	/*Log method call*/
	/*call the hot spring service and return the results of the retrieveAllSoaker 
	 * method*/
	
	@GetMapping("/soaker")
	public List<SoakerData> retrieveAllSoakers(){
		log.info("Retrieve all soakers called.");
		return hotSpringService.retrieveAllSoakers();
	}
	
	/*"/soaker/" is a resource, pass in the variable name "{SoakerId}"*/
	
	@GetMapping("/soaker/{soakerId}")
	
	/*tell Spring we're expecting the variable in the URL and then it will go into the 
	 * soaker Id parameter - use "@PathVariable"*/
	/*log call*/
	
	public SoakerData retrieveSoakerById(@PathVariable Long soakerId) {
		log.info("Retrieving soaker with ID ={}" + soakerId);
		return hotSpringService.retrieveSoakerById(soakerId);
	}
	
	/*deleteAll method to assure that delete all soakers CANNOT happen.*/
	/*Write first delete method: delete all.*/
	/*@DeleteMapping("/soaker")*/
	/*log*/
	/*Throw unsupported operation exception, unchecked so no need to declare it.*/
	
	@DeleteMapping("/soaker")
	public void deleteAllSoakers() {
		log.info("Attempting to delete all soakers.");
		throw new UnsupportedOperationException(
			"Deleting all soakers is not allowed.");
	}
	
	/*Write delete soaker by ID method:
	 *  Return a message, and let Jackson convert this to JSON.
	 *  log it.
	 *  Delete it in the service.*/
	
	@DeleteMapping("/soaker/{soakerId}")
	public Map<String, String> deleteSoakerById(@PathVariable Long soakerId){
		log.info("Deleting soaker with ID ={}" + soakerId);
		
		hotSpringService.deleteSoakerById(soakerId);
		
		return Map.of("message", 
			"Deletion of soaker with ID =" + soakerId + " was successful");
	}
	
	/*method to insert hot spring associated with the soaker who is adding it.
	 * Takes 1 resource id plus the JSON payload.
	 * log it.
	 * create HotSpringData method.
	 * return HotSpringData object.*/
	
	@PostMapping("/soaker/{soakerId}/hot_spring")
	@ResponseStatus(code = HttpStatus.CREATED)
	public HotSpringData insertHotSpring(@PathVariable Long soakerId, 
		@RequestBody HotSpringData hotSpringData) {
		
		log.info("Creating hot spring {} for soaker with ID = {}", 
				hotSpringData, soakerId);
		
		//return hot spring data object
		return hotSpringService.saveHotSpring(soakerId, hotSpringData);
	}
	
	/* method to modify a hot spring*/
	/*Copy insertHotSpring method and use it to create update method.
	Change @ PostMapping to @ PutMapping,
	Add {hotSpringId}
	Get rid of @ ResponseStatus line because we want to return 200 status on this, instead 
	of 201 created.
	Add @ PathVariable Long hotSpringId,
	Add hotSpringId into the hotSpringData by saying hotSpringData.setHotSpringId
	(hotSpringId);*/
	
	@PutMapping("/soaker/{soakerId}/hot_spring/{hotSpringId}")
	public HotSpringData updateHotSpring(@PathVariable Long soakerId,
		@PathVariable Long hotSpringId,	
		@RequestBody HotSpringData hotSpringData) {
		
		hotSpringData.setHotSpringId(hotSpringId);
		
		log.info("Updating hot spring {} for soaker with ID = {}", 
				hotSpringData, soakerId);
		
		//return hot spring data object
		return hotSpringService.saveHotSpring(soakerId, hotSpringData);
	}

	/*create retrieveHotSpringById method.
	 * log it.
	 * call service method hotSpringService.retrieveHotSpringById*/	
	
	@GetMapping("/soaker/{soakerId}/hot_spring/{hotSpringId}")
	public HotSpringData retrieveHotSpringById(@PathVariable Long soakerId,
			@PathVariable Long hotSpringId) {
		log.info("Retrieving hot spring with ID = {} for soaker with ID = {}", 
				hotSpringId, soakerId);
			
		return hotSpringService.retrieveHotSpringById(soakerId, hotSpringId);
			}
	@GetMapping("/soaker/{soakerId}/hot_spring")
	public List<HotSpringData> retrieveAllHotSprings(@PathVariable Long soakerId) {
		log.info("Retrieving all hot springs for soaker with ID = {}", soakerId);
		return hotSpringService.retrieveAllHotSprings(soakerId);
	}
	@DeleteMapping("/soaker/{soakerId}/hot_spring/{hotSpringId}")
	public Map<String, String> deleteHotSpringById(
		@PathVariable Long soakerId,
		@PathVariable Long hotSpringId) {
			log.info("Deleting hot spring with ID = {} for soaker with ID = {}",
				hotSpringId, soakerId);
			
			hotSpringService.deleteHotSpringById(soakerId, hotSpringId);

			return Map.of("message",
				"Deletion of hot spring with ID = " + hotSpringId + " was successful");
			
		}
	/*retreive all available detail tags
	 * returns a list of strings so the front end
	 * can display them as checkbox options */
	@GetMapping("/detail")
	public List<String> retrieveAllDetails() {
		log.info("Retrieving all details.");
		return hotSpringService.retrieveAllDetails();
	}

	}
