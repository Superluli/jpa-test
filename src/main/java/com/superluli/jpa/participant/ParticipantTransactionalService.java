package com.superluli.jpa.participant;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.superluli.jpa.NestedServerRuntimeException;

@Service
public class ParticipantTransactionalService {

	@Autowired
	private ParticipantRepository repo;

	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation =  Propagation.REQUIRES_NEW)
	public ParticipantEntity doSomething(ParticipantEntity entity,
			ObjectNode data) {

		List<ParticipantEntity> allParticipantsOnUser = repo
				.findByPromotionIdAndUserIdForUpdate(entity.getPromotionId(),
						entity.getUserId());

		System.err.println("allParticipantsOnUser : "
				+ allParticipantsOnUser.stream().map(p -> p.getStatus())
						.collect(Collectors.toList()));

		entity = repo.findOneForUpdate(entity.getId());
		
		if (!entity.getStatus().equals("CLAIMABLE")) {
			throw new NestedServerRuntimeException(HttpStatus.BAD_REQUEST,
					"INVALID STATUS");
		}
		
		System.err.println("CLAIM!!!");
		
		for (ParticipantEntity p : allParticipantsOnUser) {
			
			if (p == entity) {
				p.setStatus("CLAIMED");
				p.setHoldings(1);
			} else {
				p.setStatus("NOT_ELIGIBLE");
			}
		}

		return entity;
	}
}
