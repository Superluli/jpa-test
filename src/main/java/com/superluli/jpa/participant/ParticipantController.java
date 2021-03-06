package com.superluli.jpa.participant;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pf/rs/v1/participants")
public class ParticipantController {

	@Autowired
	ParticipantService service;

	private static Random rnd = new Random();
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ParticipantEntity getParticipantByWalletId(HttpServletRequest request,
			@RequestParam("wallet.id") String walletId, @RequestHeader HttpHeaders headers) {
		

		try {
			Thread.sleep(rnd.nextInt(100));
		} catch (Exception e) {
		}
		
		service.getParticipantByWalletId1(headers);
		return null;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ParticipantEntity create(HttpServletRequest request,
			@RequestParam("wallet.id") String walletId, @RequestHeader HttpHeaders headers) {

		return service.creatParticipant(headers);
	}
	
	@RequestMapping(value = "/{participantId}", method = RequestMethod.GET)
	public ParticipantEntity getById(@PathVariable("participantId") String participantId) {

		return service.getResult();
		//return service.getById(participantId);
	}
}
