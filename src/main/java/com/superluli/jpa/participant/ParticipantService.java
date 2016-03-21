package com.superluli.jpa.participant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.superluli.jpa.CommonUtils;

@Component
public class ParticipantService {

	@Autowired
	private ParticipantRepository repo;

	public ParticipantEntity createParticipant(ParticipantEntity entity) {

		List<ParticipantEntity> existingParticipants = repo
				.findByPromotionIdAndWalletId(entity.getPromotionId(),
						entity.getWalletId());

		if (existingParticipants.size() == 0) {
			entity.setId(CommonUtils.generateSessionId());
			
			try {
				entity = repo.save(entity);
			} catch (DataIntegrityViolationException e) {
				
				/*
				 * Race Condition
				 */
				entity = repo.findByPromotionIdAndWalletId(
						entity.getPromotionId(), entity.getWalletId()).get(0);
			}

		} else {
			entity = existingParticipants.get(0);
		}

		return entity;
	}
	
	@Transactional
	public ParticipantEntity doSomething(String participantId, ObjectNode data){
		
		ParticipantEntity entity = repo.findOneForUpdate(participantId);
		
		if(entity.getHoldings() == 0){
			System.err.println("DO SOMETHING : " + data);
			entity.setHoldings(1);
			entity = repo.save(entity);
		}
		
		return entity;
	}
}
