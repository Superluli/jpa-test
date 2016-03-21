package com.superluli.jpa.participant;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ParticipantRepository extends
		CrudRepository<ParticipantEntity, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from ParticipantEntity p where p.id=?1")
	public ParticipantEntity findOneForUpdate(String id);
	
	/*
	 * find CLAIMED on deviceId
	 */
	public List<ParticipantEntity> findByPromotionIdAndDeviceIdAndStatus(
			String promotionId, String deviceId, String status);
	
	/*
	 * find CLAIMED on did
	 */
	public List<ParticipantEntity> findByPromotionIdAndXSmpsDIdAndStatus(
			String promotionId, String did, String status);
	
	/*
	 * find CLAIMED on userId
	 */
	public List<ParticipantEntity> findByPromotionIdAndUserIdAndStatus(
			String promotionId, String userId, String status);
	
	/*
	 * find CLAIMED on mid
	 */
	public List<ParticipantEntity> findByPromotionIdAndXSmpsMIdAndStatus(
			String promotionId, String mid, String status);
	
	
	public List<ParticipantEntity> findByPromotionIdAndWalletId(
			String promotionId, String walletId);
}
