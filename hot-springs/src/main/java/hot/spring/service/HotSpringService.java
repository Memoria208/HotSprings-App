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
import hot.spring.controller.model.SkinnyDipperData;
import hot.spring.dao.DetailDao;
import hot.spring.dao.HotSpringDao;
import hot.spring.dao.SkinnyDipperDao;
import hot.spring.entity.Detail;
import hot.spring.entity.HotSpring;
import hot.spring.entity.SkinnyDipper;

/* will create this bean and then inject it into the hotSpringService instance variable*/
@Service
public class HotSpringService {
	
	@Autowired
	private HotSpringDao hotSpringDao;

	@Autowired
	private SkinnyDipperDao skinnyDipperDao;
	
	/*Add the DetailDao we just created as an instance variable in HotSpringService.*/
	
	@Autowired
	private DetailDao detailDao;
	
	/* I want to either create an empty skinny dipper object or find one in the database. 
	 * checking whether the skinnyDipperId is null or not*/
	/* start transaction */
	@Transactional(readOnly = false)
	public SkinnyDipperData saveSkinnyDipper(SkinnyDipperData skinnyDipperData) {
		Long skinnyDipperId = skinnyDipperData.getSkinnyDipperId();
		
		// declare SkinnyDipper entity object
		SkinnyDipper skinnyDipper = findOrCreateSkinnyDipper(skinnyDipperId, 			skinnyDipperData.getSkinnyDipperEmail());

		setFieldsInSkinnyDipper(skinnyDipper, skinnyDipperData);
		return new SkinnyDipperData(skinnyDipperDao.save(skinnyDipper));
	}

	private void setFieldsInSkinnyDipper(SkinnyDipper skinnyDipper, 
			SkinnyDipperData skinnyDipperData) {
		skinnyDipper.setSkinnyDipperEmail(skinnyDipperData.getSkinnyDipperEmail());
		skinnyDipper.setSkinnyDipperName(skinnyDipperData.getSkinnyDipperName());
		
		/*don't need to set skinnyDipperId because it will either be null for create
		 * operation or it will be correct for modify operation*/
	}

	private SkinnyDipper findOrCreateSkinnyDipper(Long skinnyDipperId, 
			String skinnyDipperEmail) {
		SkinnyDipper skinnyDipper;

		if (Objects.isNull(skinnyDipperId)) {
			
			Optional<SkinnyDipper> opSkinDip= skinnyDipperDao
					.findBySkinnyDipperEmail(skinnyDipperEmail);
			
		/* Once we’ve gotten the optional, it will either be empty or it will have a skinny 
		 * dipper object in it. If it has a skinny dipper, it’s a duplicate, so*/
			
			if(opSkinDip.isPresent()) {
				throw new DuplicateKeyException(
					"Skinny dipper with email " + skinnyDipperEmail + " already exists.");
				
			}
			
			skinnyDipper = new SkinnyDipper();
		} else {
			skinnyDipper = findSkinnyDipperById(skinnyDipperId);
		}
		return skinnyDipper;
	}

	private SkinnyDipper findSkinnyDipperById(Long skinnyDipperId) {
		return skinnyDipperDao.findById(skinnyDipperId).orElseThrow(
				() -> new NoSuchElementException(
				"Skinny dipper with ID =" + skinnyDipperId + " was not found."));
	}

	/* add @Transactional, give it the attribute (readOnly = true) 
	/* the DAO will return List<SkinnyDipper>, the entity object 
	/* turn the list of SkinnyDipper objects into a list of data objects 
	 * turn the list of SkinnyDipper entities into a list of skinnydipper data, using
	 * an enhanced 'for' loop
	 * turn the list of SkinnyDipper entities into a list of skinnydipper data, using
	 * an enhanced 'for' loop*/
	
	@Transactional(readOnly = true)
	public List<SkinnyDipperData> retrieveAllSkinnyDippers() {
		List<SkinnyDipper> skinnyDippers = skinnyDipperDao.findAll();
		List<SkinnyDipperData> response = new LinkedList<>();

		for (SkinnyDipper skinnyDipper : skinnyDippers) {
			response.add(new SkinnyDipperData(skinnyDipper));
		}

		return response;

		// can use streams - (will look more at this later)
		// we have a stream of skinnyDipper, but we want skinnyDipperData
		// @formatter: off
//		return skinnyDipperDao.findAll()
//		.stream()
//		.map(cont -> new SkinnyDipperData(cont))
//		.toList();	
		// @formatter: on
	}

	@Transactional(readOnly = true)
	public SkinnyDipperData retrieveSkinnyDipperById(Long skinnyDipperId) {
		SkinnyDipper skinnyDipper = findSkinnyDipperById(skinnyDipperId);
		return new SkinnyDipperData(skinnyDipper);
	}
	
	 /*Create deleteSkinnyDipperById
	  * @Transactional(readOnly = false) 
	  * If we pass in an invalid ID, we want it to throw an exception.
	  * if it finds a skinnyDipper, we want to delete it.*/
	
	@Transactional(readOnly = false)
	public void deleteSkinnyDipperById(Long skinnyDipperId) {
		SkinnyDipper skinnyDipper = findSkinnyDipperById(skinnyDipperId);
		skinnyDipperDao.delete(skinnyDipper);
	}

	/* Retrieve skinny dipper by ID
	 * Retrieve all details
	 * Create/retrieve hot springs object
	 * Set skinny dipper in the hot spring
	 * Set hot spring in all details
	 * Set the details in the hot spring
	 * Save the hot spring*/
	
	@Transactional(readOnly = false)
	public HotSpringData savetHotSpring(Long skinnyDipperId, HotSpringData hotSpringData) {
		SkinnyDipper skinnyDipper = findSkinnyDipperById(skinnyDipperId);
		
		Set<Detail> details = detailDao.findAllByDetailIn(hotSpringData.getDetails());
		
		HotSpring hotSpring = findOrCreateHotSpring(hotSpringData.getHotSpringId());
		setHotSpringFields(hotSpring, hotSpringData);
		
		//Set Relationships
		
		//set skinny dipper
		hotSpring.setSkinnyDipper(skinnyDipper);
		skinnyDipper.getHotSprings().add(hotSpring);
		
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
	public HotSpringData retrieveHotSpringById(Long skinnyDipperId, Long hotSpringId) {
		//validate that the skinny dipper Id exists
		findSkinnyDipperById(skinnyDipperId);
		HotSpring hotSpring = findHotSpringById(hotSpringId);
		
		//check skinnyDipperId in hotSpring is the same as what was passed in as a parameter
		if(hotSpring.getSkinnyDipper().getSkinnyDipperId() != skinnyDipperId) {
			throw new IllegalStateException("Hot spring with ID = " 
			+ hotSpringId + " is not owned by skinny dipper with ID = " + skinnyDipperId);
		}
		
		return new HotSpringData(hotSpring);
	}
	
	@Transactional(readOnly = true)
	public List<HotSpringData> retrieveAllHotSprings(Long skinnyDipperId) {
		SkinnyDipper skinnyDipper = findSkinnyDipperById(skinnyDipperId);
		List<HotSpringData> response = new LinkedList<>();

		for(HotSpring hotSpring : skinnyDipper.getHotSprings()) {
			response.add(new HotSpringData(hotSpring));
		}

		return response;
	}
}


