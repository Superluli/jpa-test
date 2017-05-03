package com.superluli.jpa.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.superluli.jpa.NestedServerRuntimeException;

@Service
public class ParticipantTransactionalService {

	@Autowired
	private ParticipantRepository participantRepo;
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public RPParticipantEntity creatParticipant(RPParticipantEntity entity, boolean db) throws Exception{
		
		if(!db){
			
			throw new NestedServerRuntimeException(HttpStatus.BAD_REQUEST, "SAVE FAIL");	
		}
		
		return participantRepo.save(entity);
	}
}
