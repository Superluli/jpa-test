package com.superluli.jpa.participant;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantRepository extends
		JpaRepository<RPParticipantEntity, String> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from RPParticipantEntity p where p.id=?1")
	public RPParticipantEntity findOneForUpdate(String id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from RPParticipantEntity p where p.programId=?1 and p.userId=?2")
	public List<RPParticipantEntity> findByProgramIdAndUserIdForUpdate(
			String programId, String userId);

	public List<RPParticipantEntity> findByProgramIdAndWalletId(
			String programId, String walletId);

}
