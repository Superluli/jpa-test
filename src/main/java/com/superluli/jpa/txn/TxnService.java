package com.superluli.jpa.txn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.superluli.jpa.CommonUtils;
import com.superluli.jpa.participant.ParticipantEntity;
import com.superluli.jpa.participant.ParticipantRepository;
import com.superluli.jpa.promotion.PromotionEntity;
import com.superluli.jpa.promotion.PromotionRepository;
import com.superluli.jpa.txn.userbased.TagBaseRepository;
import com.superluli.jpa.txn.userbased.UserTagsBaseRepository;
import com.superluli.jpa.txn.userbased.UserTagsEntity;

@Component
public class TxnService {

	@Autowired
	TxnRepository txnRepo;

	@Autowired
	PromotionRepository promoRepo;

	@Autowired
	ParticipantRepository participantRepo;

	@Autowired
	TagBaseRepository tagRepo;

	@Autowired
	UserTagsBaseRepository userTagsRepo;

	public void processTxn(TxnEntity txnEntity) {

		/*
		 * Always save first
		 */
		txnEntity = txnRepo.save(txnEntity);

		for (PromotionEntity deviceBasedPromotion : getAvailableDeviceBasedPromotions("")) {
			processDeviceBasedPromotion(txnEntity, deviceBasedPromotion);
		}

		for (PromotionEntity userBasedPromotion : getAvailableUserBasedPromotions("")) {
			processUserBasedPromotion(txnEntity, userBasedPromotion);
		}
	}

	public void processDeviceBasedPromotion(TxnEntity txnEntity,
			PromotionEntity promotion) {

		String promotionId = txnEntity.getPromotionId();
		String walletId = txnEntity.getWalletId();

		List<ParticipantEntity> participantsInWallet = participantRepo
				.findByPromotionIdAndWalletId(promotionId, walletId);

		ParticipantEntity entity;

		if (participantsInWallet.size() == 0) {

			entity = new ParticipantEntity();
			entity.setId(CommonUtils.generateSessionId());
			entity.setPromotionId(promotionId);
			entity.setWalletId(walletId);
			entity.setHoldings(1);
			try {
				entity = participantRepo.save(entity);
			} catch (DataIntegrityViolationException e) {
				// Race condition
				entity = participantRepo.findByPromotionIdAndWalletId(
						promotionId, walletId).get(0);
			}

		} else {
			entity = participantsInWallet.get(0);
		}

		sendNotification(entity);
	}

	public void processUserBasedPromotion(TxnEntity txnEntity,
			PromotionEntity promotion) {

		// get claimed participants on user
		List<ParticipantEntity> claimedPaticipantsOnUser = participantRepo
				.findByPromotionIdAndUserIdAndStatus("", "", "");

		// get participant on wallet
		List<ParticipantEntity> participantOnWallet = participantRepo
				.findByPromotionIdAndWalletId("", "");

		/*
		 * Already claimed
		 */

		ParticipantEntity entity = null;

		if (claimedPaticipantsOnUser.size() != 0) {

			if (participantOnWallet.size() != 0) {
				entity = participantOnWallet.get(0);
				entity.setStatus("NOT_ELIGIBLE");
			} else {
				entity.setId("new id");
				entity.setStatus("NOT_ELIGIBLE");
			}
			participantRepo.save(entity);
		}

		/*
		 * Normal
		 */
		else {

			int txnMade = 0;
			int txnCount = 0;
			int txnNeeded = Math.min(txnCount - txnMade, 0);

			if (txnNeeded == 0) {

				// participant already exist
				if (participantOnWallet.size() != 0) {
					entity = participantOnWallet.get(0);

					// only has logic on pending status
					if (entity.getStatus == PENDING) {
						entity.setTxnNeeded(txnCount - txnMade);
						if (txnCount == txnMade) {
							entity.setStatus(QUALIFIED);
						}
					}

					else {
						entity = new ParticipantEntity();
						entity.setId("new ID");
						entity.setStatus("QUELIFIED");
					}
				}
			}

			// txn Count not enough yet, only update txnNeeded if participant
			// exist
			else {
				if (participantOnWallet.size() != 0) {
					entity.setTxnNeeded(txnNeeded);
					return entity;
				}
			}
		}
	}

	public void sendNotification(ParticipantEntity entity) {
		System.out.println("send notification");
	}

	public List<PromotionEntity> getAvailableDeviceBasedPromotions(
			String deviceModel) {

		return promoRepo.getAvailableDeviceBasedPromotions(deviceModel);
	}

	public List<PromotionEntity> getAvailableUserBasedPromotions(String userId) {

		List<PromotionEntity> allUserBasedPromotions = promoRepo
				.getAllUserBasedPromotions();
		List<String> tags = userTagsRepo.getTagsByUserId(userId);
		return allUserBasedPromotions.stream().filter(p -> available(p, tags))
				.collect(Collectors.toList());
	}

	private boolean available(PromotionEntity promoEntity, List<String> tags) {
		List<String> includes = promoEntity.getIncludes();
		List<String> excludes = promoEntity.getExcludes();
		
		boolean includeFound = false;
		
		for(String tag : tags){
			if(excludes.contains(tag)){
				return false;
			}	
			if(includes.contains(tag)){
				includeFound = true;
			}
		}
		return includeFound;
	}
}
