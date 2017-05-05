package com.superluli.jpa.participant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.superluli.jpa.CommonUtils;
import com.superluli.jpa.NestedServerRuntimeException;

@Component
public class ParticipantService {

	@Autowired
	private ParticipantRepository participantRepo;

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
	public ParticipantEntity creatParticipant(HttpHeaders headers) {

		String mid = headers.getFirst(Constants.Headers.X_SMPS_MID);
		String dmid = headers.getFirst(Constants.Headers.X_SMPS_DMID);

		ParticipantEntity participantEntity = null;
		participantEntity = new ParticipantEntity();
		participantEntity.setId(CommonUtils.generateUUID());
		participantEntity.setProgramId("RP");
		participantEntity.setWalletId(dmid);
		participantEntity.setUserId(mid);

		creatParticipant(participantEntity, true);

		return participantEntity;
	}

	public ParticipantEntity creatParticipant(ParticipantEntity entity, boolean db) {

		if (!db) {
			throw new NestedServerRuntimeException(HttpStatus.BAD_REQUEST, "SAVE FAIL");
		}

		return participantRepo.save(entity);
	}

	public void test() {
		if (System.currentTimeMillis() > 0) {
			throw new NestedServerRuntimeException(HttpStatus.BAD_REQUEST, "SAVE FAIL");
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
	public ParticipantEntity getParticipantByWalletId1(HttpHeaders headers) {

		String mid = headers.getFirst(Constants.Headers.X_SMPS_MID);
		String dmid = headers.getFirst(Constants.Headers.X_SMPS_DMID);

		String thread = Thread.currentThread().getName();

		List<ParticipantEntity> allParticipantsOnSameUser = participantRepo.findByProgramIdAndUserIdForUpdate("RP", mid);

		allParticipantsOnSameUser.stream().forEach(p -> System.err.print(p.getWalletId() + ","));
		System.err.println();

		allParticipantsOnSameUser = participantRepo.findByProgramIdAndUserIdForUpdate("RP", mid);
		
		allParticipantsOnSameUser.stream().forEach(p -> System.err.print(p.getWalletId() + ","));
		System.err.println();
		
		ParticipantEntity existingParticipantOnWalletId = allParticipantsOnSameUser.stream()
				.filter(p -> p.getWalletId().equals(dmid)).findFirst().orElse(null);

		ParticipantEntity participantEntity = null;

		if (!allParticipantsOnSameUser.isEmpty() && existingParticipantOnWalletId == null) {

			ParticipantEntity anyExistingParticipant = allParticipantsOnSameUser.get(0);

			participantEntity = new ParticipantEntity();
			participantEntity.setId("1");
			participantEntity.setProgramId("RP");
			participantEntity.setWalletId(dmid);
			participantEntity.setUserId(mid);

			participantEntity = participantRepo.save(participantEntity);
		}

		System.err.println("TEST ----------------------------------------------------------");
		return participantEntity;
	}


	public ParticipantEntity syncParticipantFromExistingParticipant(ParticipantEntity participantOnWalletId,
			ParticipantEntity anyExistingParticipant) {

		participantOnWalletId.setHoldings(anyExistingParticipant.getHoldings());
		participantOnWalletId.setStatus(anyExistingParticipant.getStatus());

		return participantOnWalletId;
	}

}
