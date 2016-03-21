package com.superluli.jpa.promotion;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PromotionRepository extends
		CrudRepository<PromotionEntity, String> {

	public List<PromotionEntity> getAvailableDeviceBasedPromotions(String deviceModel);
	
	public List<PromotionEntity> getAllUserBasedPromotions();
}
