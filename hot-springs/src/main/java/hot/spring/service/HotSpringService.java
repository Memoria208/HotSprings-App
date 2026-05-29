package hot.spring.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hot.spring.controller.model.HotSpringData;
import hot.spring.controller.model.SoakerData;
import hot.spring.dao.DetailDao;
import hot.spring.dao.HotSpringDao;
import hot.spring.dao.SoakerDao;
import hot.spring.entity.Detail;
import hot.spring.entity.HotSpring;
import hot.spring.entity.Soaker;

/* will create this bean and then inject it into the hotSpringService instance variable*/
@Service
public class HotSpringService {
	
	@Autowired
	private HotSpringDao hotSpringDao;

	@Autowired
	private SoakerDao soakerDao;
	
	/*Add the DetailDao we just created as an instance variable in HotSpringService.*/
	
	@Autowired
	private DetailDao detailDao;
	
	/* I want to either create an empty soaker object or find one in the database. 
	 * checking whether the soakerId is null or not*/
	/* start transaction */
	@Transactional(readOnly = false)
	public SoakerData saveSoaker(SoakerData soakerData) {
		Long soakerId = soakerData.getSoakerId();
		
		// declare Soaker entity object
		Soaker soaker = findOrCreateSoaker(soakerId, 			soakerData.getSoakerEmail());

		setFieldsInSoaker(soaker, soakerData);
		return new SoakerData(soakerDao.save(soaker));
	}

	private void setFieldsInSoaker(Soaker soaker, 
			SoakerData soakerData) {
		soaker.setSoakerEmail(soakerData.getSoakerEmail());
		soaker.setSoakerName(soakerData.getSoakerName());
		
		/*don't need to set soakerId because it will either be null for create
		 * operation or it will be correct for modify operation*/
	}

	private Soaker findOrCreateSoaker(Long soakerId, 
			String soakerEmail) {
		Soaker soaker;

		if (Objects.isNull(soakerId)) {
			
			Optional<Soaker> opSoaker= soakerDao
					.findBySoakerEmail(soakerEmail);
			
		/* Once we’ve gotten the optional, it will either be empty or it will have a skinny 
		 * dipper object in it. If it has a soaker, it’s a duplicate, so*/
			
			if(opSoaker.isPresent()) {
				throw new DuplicateKeyException(
					"soaker with email " + soakerEmail + " already exists.");
				
			}
			
			soaker = new Soaker();
		} else {
			soaker = findSoakerById(soakerId);
		}
		return soaker;
	}

	private Soaker findSoakerById(Long soakerId) {
		return soakerDao.findById(soakerId).orElseThrow(
				() -> new NoSuchElementException(
				"soaker with ID =" + soakerId + " was not found."));
	}

	/* add @Transactional, give it the attribute (readOnly = true) 
	/* the DAO will return List<Soaker>, the entity object 
	/* turn the list of Soaker objects into a list of data objects 
	 * turn the list of Soaker entities into a list of Soaker data, using
	 * an enhanced 'for' loop
	 * turn the list of Soaker entities into a list of Soaker data, using
	 * an enhanced 'for' loop*/
	
	@Transactional(readOnly = true)
	public List<SoakerData> retrieveAllSoakers() {
		List<Soaker> soakers = soakerDao.findAll();
		List<SoakerData> response = new LinkedList<>();

		for (Soaker soaker : soakers) {
			response.add(new SoakerData(soaker));
		}

		return response;

		// can use streams - (will look more at this later)
		// we have a stream of Soaker, but we want SoakerData
		// @formatter: off
//		return SoakerDao.findAll()
//		.stream()
//		.map(cont -> new SoakerData(cont))
//		.toList();	
		// @formatter: on
	}

	@Transactional(readOnly = true)
	public SoakerData retrieveSoakerById(Long soakerId) {
		Soaker soaker = findSoakerById(soakerId);
		return new SoakerData(soaker);
	}
	
	 /*Create deleteSoakerById
	  * @Transactional(readOnly = false) 
	  * If we pass in an invalid ID, we want it to throw an exception.
	  * if it finds a Soaker, we want to delete it.*/
	
	@Transactional(readOnly = false)
	public void deleteSoakerById(Long soakerId) {
		Soaker soaker = findSoakerById(soakerId);
		soakerDao.delete(soaker);
	}

	/* Retrieve soaker by ID
	 * Retrieve all details
	 * Create/retrieve hot springs object
	 * Set soaker in the hot spring
	 * Set hot spring in all details
	 * Set the details in the hot spring
	 * Save the hot spring*/
	
	@Transactional(readOnly = false)
	public HotSpringData saveHotSpring(Long soakerId, HotSpringData hotSpringData) {
		Soaker soaker = findSoakerById(soakerId);
		
		Set<Detail> details = detailDao.findAllByDetailIn(hotSpringData.getDetails());
		
		HotSpring hotSpring = findOrCreateHotSpring(hotSpringData.getHotSpringId());
		setHotSpringFields(hotSpring, hotSpringData);
		
		//Set Relationships
		
		//set soaker
		hotSpring.setSoaker(soaker);
		soaker.getHotSprings().add(hotSpring);
		
		//set details
		for(Detail detail : details) {
			detail.getHotSprings().add(hotSpring);
			hotSpring.getDetails().add(detail);
		}
		/*new object variable dbHotSpring so that if it was an insert operation, it will 
		return an object with the primary key*/
		HotSpring dbHotSpring = hotSpringDao.save(hotSpring);
		
		return new HotSpringData(dbHotSpring);
	}
	@Transactional(readOnly = false)
	public void deleteHotSpringById(Long soakerId, Long hotSpringId){
		HotSpring hotSpring = findHotSpringById(hotSpringId);

		if(!hotSpring.getSoaker().getSoakerId().equals(soakerId)){
			throw new IllegalStateException("Hot spring with ID = "
				+ hotSpringId + " is not owned by soaker with ID =" + soakerId);
		}

		hotSpringDao.delete(hotSpring);
	}

	private void setHotSpringFields(HotSpring hotSpring, HotSpringData hotSpringData) {
		hotSpring.setCounty(hotSpringData.getCounty());
		hotSpring.setDirections(hotSpringData.getDirections());
		hotSpring.setLatitude(hotSpringData.getLatitude());
		hotSpring.setLongitude(hotSpringData.getLongitude());
		hotSpring.setHotSpringName(hotSpringData.getHotSpringName());
		hotSpring.setHotSpringId(hotSpringData.getHotSpringId());
		
	}

	private HotSpring findOrCreateHotSpring(Long hotSpringId) {
		HotSpring hotSpring;
		
		if(Objects.isNull(hotSpringId)) {
			hotSpring = new HotSpring();
		}else {
			hotSpring = findHotSpringById(hotSpringId);
		}
		return hotSpring;
	}

	private HotSpring findHotSpringById(Long hotSpringId) {
		return hotSpringDao.findById(hotSpringId)
			.orElseThrow(() -> new NoSuchElementException(
				"Hot spring with ID = " + hotSpringId + " does not exist."));
	}

	@Transactional(readOnly = true)
	public HotSpringData retrieveHotSpringById(Long soakerId, Long hotSpringId) {
		//validate that the soaker Id exists
		findSoakerById(soakerId);
		HotSpring hotSpring = findHotSpringById(hotSpringId);
		
		//check soakerId in hotSpring is the same as what was passed in as a parameter
		if(!hotSpring.getSoaker().getSoakerId().equals(soakerId)) {
			throw new IllegalStateException("Hot spring with ID = " 
			+ hotSpringId + " is not owned by soaker with ID = " + soakerId);
		}
		
		return new HotSpringData(hotSpring);
	}
	
	@Transactional(readOnly = true)
	public List<HotSpringData> retrieveAllHotSprings(Long soakerId) {
		Soaker soaker = findSoakerById(soakerId);
		List<HotSpringData> response = new LinkedList<>();

		for(HotSpring hotSpring : soaker.getHotSprings()) {
			response.add(new HotSpringData(hotSpring));
		}

		return response;
	}
}


