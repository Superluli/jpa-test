package com.superluli.jpa.participant;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ParticipantRepository extends
		JpaRepository<ParticipantEntity, String> {
	
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from ParticipantEntity p where p.id=?1")
	public ParticipantEntity findOneForUpdate(String id);

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from ParticipantEntity p where p.programId=?1 and p.userId=?2")
	public List<ParticipantEntity> findByProgramIdAndUserIdForUpdate(
			String programId, String userId);

	public List<ParticipantEntity> findByProgramIdAndWalletId(
			String programId, String walletId);

}
