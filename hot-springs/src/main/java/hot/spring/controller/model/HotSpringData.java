package hot.spring.controller.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import hot.spring.entity.Detail;
import hot.spring.entity.HotSpring;
import hot.spring.entity.Soaker;
import lombok.Data;
import lombok.NoArgsConstructor;

/*Copy/paste everything from the HotSpring entity.
Add @ Data lombok annotation to create Getters/Setters.
Add @ NoArgsConstructor.
Delete pasted annotations
Change Soaker to: HotSpringSoaker
Change Set<Detail> details to Set<String> details
*/

@Data
@NoArgsConstructor
public class HotSpringData {

	 private Long hotSpringId;
	 private String hotSpringName;
	 private BigDecimal longitude;
	 private BigDecimal latitude;
	 private String county;
	 private String directions;
	 
	 //change Soaker to HotSpringSoaker
	 private HotSpringSoaker soaker;
	 
	 //change Set<Detail> to Set<String>
	 private Set<String> details = new HashSet<>();
	 
	 public HotSpringData(HotSpring hotSpring) {
		 hotSpringId = hotSpring.getHotSpringId();
		 hotSpringName = hotSpring.getHotSpringName();
		 longitude = hotSpring.getLongitude();
		 latitude = hotSpring.getLatitude();
		 county = hotSpring.getCounty();
		 directions = hotSpring.getDirections();
		 
		 //need a new hot spring soaker, not a new soaker entity
		 soaker = new HotSpringSoaker(hotSpring.getSoaker());
		 
		 //set details
		 for(Detail detail : hotSpring.getDetails()) {
			 details.add(detail.getDetail());
		 }
	 }
	 
	 /*Create HotSpringSoaker object, like we did for SoakerData
	  * copy/paste - Id, Name, and Email from Soaker class.
	  * Delete all the pasted annotations from Soaker class. DTO?*/
	 
	 @Data
	 @NoArgsConstructor
	 public static class HotSpringSoaker {
		 private Long soakerId;
		 private String soakerName;
		 private String soakerEmail;
		 
		 //constructor for hot spring soaker that takes a soaker
		 public HotSpringSoaker(Soaker soaker) {
			 
			 //set fields for soaker
			 soakerId = soaker.getSoakerId();
			 soakerName = soaker.getSoakerName();
			 soakerEmail = soaker.getSoakerEmail();
		 }
	 }
}
