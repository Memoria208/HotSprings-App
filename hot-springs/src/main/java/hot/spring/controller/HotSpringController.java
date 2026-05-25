package hot.spring.controller;

import java.util.List;
import java.util.Map;

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
import hot.spring.controller.model.SkinnyDipperData;
import hot.spring.service.HotSpringService;
import lombok.extern.slf4j.Slf4j;

/* tell Spring that this is a REST controller- every method will return a
 * 200 or OK status by default, we are expecting JSON to come into the method 
 * and will return JSON as well
 * 
 * tell it how to map the URIs into the methods 
 * 
 * add logger */

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
	 * "/hot_spring/skinny_dipper" */
	
	@PostMapping("/skinny_dipper")
	@ResponseStatus(code = HttpStatus.CREATED)
	public SkinnyDipperData insertSkinnyDipper(
			@RequestBody SkinnyDipperData skinnyDipperData) {
		
		/*{} is how you specify a replaceable parameter*/
		
		log.info("Creating skinny dipper {}", skinnyDipperData);
		return hotSpringService.saveSkinnyDipper(skinnyDipperData);
	}
	
	/*Add update method right after the insert method because they’re very similar.*/
	/*Takes a //(@PathVariable Long skinnyDipperId,*/
	/*And a payload/body // @RequestBody SkinnyDipperData skinnyDipperData)*/
	/*Add @PutMapping (“/inside will be the resource/{and the resource ID}”)*/
	/*Set skinnyDipperId inside the skinnyDipperData object just to make sure it is always 
	 * set.*/
	/*Log it. */
	/*Call the same save method on the service that we called when we were inserting a 
	 * skinnyDipper.*/
	
	/*"/table_name/{javaFieldName}*/
	@PutMapping("/skinny_dipper/{skinnyDipperId}")
	public SkinnyDipperData updateSkinnyDipper(@PathVariable Long skinnyDipperId, 		
			@RequestBody SkinnyDipperData skinnyDipperData) {
		skinnyDipperData.setSkinnyDipperId(skinnyDipperId);
		log.info("Updating skinny dipper {}", skinnyDipperData);
		return hotSpringService.saveSkinnyDipper(skinnyDipperData);
	}
	
	/*write skinnyDipper method. public, returns List of SkinnyDipperData called 
	 * retrieveAllSkinnyDippers, takes no parameters*/
	/*add @GetMapping because this is a GET request*/
	/*Log method call*/
	/*call the hot spring service and return the results of the retrieveAllSkinnyDipper 
	 * method*/
	
	@GetMapping("/skinny_dipper")
	public List<SkinnyDipperData> retrieveAllSkinnyDippers(){
		log.info("Retrieve all skinny dippers called.");
		return hotSpringService.retrieveAllSkinnyDippers();
	}
	
	/*"/skinny_dipper/" is a resource, pass in the variable name "{skinnyDipperId}"*/
	
	@GetMapping("/skinny_dipper/{skinnyDipperId}")
	
	/*tell Spring we're expecting the variable in the URL and then it will go into the 
	 * skinny dipper Id parameter - use "@PathVariable"*/
	/*log call*/
	
	public SkinnyDipperData retrieveSkinnyDipperById(@PathVariable Long skinnyDipperId) {
		log.info("Retrieving skinny dipper with ID ={}" + skinnyDipperId);
		return hotSpringService.retrieveSkinnyDipperById(skinnyDipperId);
	}
	
	/*deleteAll method to assure that delete all skinny dippers CANNOT happen.*/
	/*Write first delete method: delete all.*/
	/*@DeleteMapping("/skinny_dipper")*/
	/*log*/
	/*Throw unsupported operation exception, unchecked so no need to declare it.*/
	
	@DeleteMapping("/skinny_dipper")
	public void deleteAllSkinnyDippers() {
		log.info("Attempting to delete all skinny dippers.");
		throw new UnsupportedOperationException(
			"Deleting all skinny dippers is not allowed.");
	}
	
	/*Write delete skinny dipper by ID method:
	 *  Return a message, and let Jackson convert this to JSON.
	 *  log it.
	 *  Delete it in the service.*/
	
	@DeleteMapping("/skinny_dipper/{skinnyDipperId}")
	public Map<String, String> deleteSkinnyDipperById(@PathVariable Long skinnyDipperId){
		log.info("Deleting skinny dipper with ID ={}" + skinnyDipperId);
		
		hotSpringService.deleteSkinnyDipperById(skinnyDipperId);
		
		return Map.of("message", 
			"Deletion of skinny dipper with ID =" + skinnyDipperId + " was successful");
	}
	
	/*method to insert hot spring associated with the skinny dipper who is adding it.
	 * Takes 1 resource id plus the JSON payload.
	 * log it.
	 * create HotSpringData method.
	 * return HotSpringData object.*/
	
	@PostMapping("/skinny_dipper/{skinnyDipperId}/hot_spring")
	@ResponseStatus(code = HttpStatus.CREATED)
	public HotSpringData insertHotSpring(@PathVariable Long skinnyDipperId, 
		@RequestBody HotSpringData hotSpringData) {
		
		log.info("Creating hot spring {} for skinny dipper with ID = {}", 
				hotSpringData, skinnyDipperId);
		
		//return hot spring data object
		return hotSpringService.savetHotSpring(skinnyDipperId, hotSpringData);
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
	
	@PutMapping("/skinny_dipper/{skinnyDipperId}/hot_spring/{hotSpringId}")
	public HotSpringData updateHotSpring(@PathVariable Long skinnyDipperId,
		@PathVariable Long hotSpringId,	
		@RequestBody HotSpringData hotSpringData) {
		
		hotSpringData.setHotSpringId(hotSpringId);
		
		log.info("Updating hot spring {} for skinny dipper with ID = {}", 
				hotSpringData, skinnyDipperId);
		
		//return hot spring data object
		return hotSpringService.savetHotSpring(skinnyDipperId, hotSpringData);
	}

	/*create retrieveHotSpringById method.
	 * log it.
	 * call service method hotSpringService.retrieveHotSpringById*/	
	
	@GetMapping("/skinny_dipper/{skinnyDipperId}/hot_spring/{hotSpringId}")
	public HotSpringData retrieveHotSpringById(@PathVariable Long skinnyDipperId,
			@PathVariable Long hotSpringId) {
		log.info("Retrieving hot spring with ID = {} for skinny dipper with ID = {}", 
				hotSpringId, skinnyDipperId);
			
		return hotSpringService.retrieveHotSpringById(skinnyDipperId, hotSpringId);
			}
	@GetMapping("/skinny_dipper/{skinnyDipperId}/hot_spring")
	public List<HotSpringData> retrieveAllHotSprings(@PathVariable Long skinnyDipperId) {
		log.info("Retrieving all hot springs for skinny dipper with ID = {}", skinnyDipperId);
		return hotSpringService.retrieveAllHotSprings(skinnyDipperId);
	}

	}
