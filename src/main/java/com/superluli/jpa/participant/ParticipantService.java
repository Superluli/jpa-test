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

	@Autowired
	private ParticipantTransactionalService service;

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
	public RPParticipantEntity creatParticipant(HttpHeaders headers) {

		String mid = headers.getFirst(Constants.Headers.X_SMPS_MID);
		String dmid = headers.getFirst(Constants.Headers.X_SMPS_DMID);

		RPParticipantEntity participantEntity = null;
		participantEntity = new RPParticipantEntity();
		participantEntity.setId(CommonUtils.generateUUID());
		participantEntity.setProgramId("RP");
		participantEntity.setWalletId(dmid);
		participantEntity.setUserId(mid);

		creatParticipant(participantEntity, true);

		return participantEntity;
	}

	public RPParticipantEntity creatParticipant(RPParticipantEntity entity, boolean db) {

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

	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
	public RPParticipantEntity getParticipantByWalletId(HttpHeaders headers) {

		String mid = headers.getFirst(Constants.Headers.X_SMPS_MID);
		String dmid = headers.getFirst(Constants.Headers.X_SMPS_DMID);

		String thread = Thread.currentThread().getName();

		List<RPParticipantEntity> allParticipantsOnSameUser = getAllParticipantsOnSameUser("RP", mid);

		System.err.println(thread + " 1st get");
		allParticipantsOnSameUser.stream().forEach(p -> System.err.print(p.getWalletId() + ","));
		System.err.println();

		allParticipantsOnSameUser = getAllParticipantsOnSameUser("RP", mid);
		
		System.err.println(thread + " 2nd get");
		allParticipantsOnSameUser.stream().forEach(p -> System.err.print(p.getWalletId() + ","));
		System.err.println();
		
		RPParticipantEntity existingParticipantOnWalletId = allParticipantsOnSameUser.stream()
				.filter(p -> p.getWalletId().equals(dmid)).findFirst().orElse(null);

		RPParticipantEntity participantEntity = null;

		if (!allParticipantsOnSameUser.isEmpty() && existingParticipantOnWalletId == null) {

			RPParticipantEntity anyExistingParticipant = allParticipantsOnSameUser.get(0);

			participantEntity = new RPParticipantEntity();
			participantEntity.setId(CommonUtils.generateUUID());
			participantEntity.setProgramId("RP");
			participantEntity.setWalletId(dmid);
			participantEntity.setUserId(mid);

			participantEntity = participantRepo.save(participantEntity);

			System.err.println(thread + " release lock");
			System.err.println("----------------------------------------------------------");
			return participantEntity;
		}

		return null;
	}

	private List<RPParticipantEntity> getAllParticipantsOnSameUser(String programId, String userId) {

		return participantRepo.findByProgramIdAndUserIdForUpdate(programId, userId);
	}

	public RPParticipantEntity syncParticipantFromExistingParticipant(RPParticipantEntity participantOnWalletId,
			RPParticipantEntity anyExistingParticipant) {

		participantOnWalletId.setHoldings(anyExistingParticipant.getHoldings());
		participantOnWalletId.setStatus(anyExistingParticipant.getStatus());

		return participantOnWalletId;
	}

}
