package hot.spring.controller.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import hot.spring.entity.Detail;
import hot.spring.entity.HotSpring;
import hot.spring.entity.Soaker;
import lombok.Data;
import lombok.NoArgsConstructor;

//DAO

@Data
@NoArgsConstructor
public class SoakerData {

	private Long soakerId;
    private String soakerName;
    private String soakerEmail;
    
    //change HotSpring to HotSpringResponse
    private Set<HotSpringResponse> hotSprings = new HashSet<>();
    
    /* convert from a Soaker object to a SoakerData object */
    public SoakerData(Soaker soaker) {
    	soakerId = soaker.getSoakerId();
    	soakerName = soaker.getSoakerName();
    	soakerEmail = soaker.getSoakerEmail();
    	
    	for(HotSpring hotSpring : soaker.getHotSprings()) {
    		hotSprings.add(new HotSpringResponse(hotSpring));
    	}
	}
    
    /*only creates getters, and a HotSpring constructor with all the fields in 
     * it */
    
    @Data
    @NoArgsConstructor
    static class HotSpringResponse {
    	private Long hotSpringId;
    	private String hotSpringName;
    	private BigDecimal longitude;
    	private BigDecimal latitude;
    	private String county;
    	private String directions;
    	
    	//change Detail to String
    	private Set<String> details = new HashSet<>();
    	
    	HotSpringResponse(HotSpring hotSpring){
    		hotSpringId = hotSpring.getHotSpringId();
    		hotSpringName = hotSpring.getHotSpringName();
    		longitude = hotSpring.getLongitude();
    		latitude = hotSpring.getLatitude();
    		county = hotSpring.getCounty();
    		directions = hotSpring.getDirections();
    		
    		for(Detail detail : hotSpring.getDetails()) {
    			details.add(detail.getDetail());
    		}
    	}
    	    
    }
}
