package com.superluli.jpa.participant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/participants")
public class ParticipantController {

	@Autowired
	ParticipantService service;

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ParticipantEntity create(@RequestBody ParticipantEntity entity) {

		return service.createParticipant(entity);
	}

	@RequestMapping(value = "/{participantId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ParticipantEntity create(@RequestBody ObjectNode data,
			@PathVariable("participantId") String participantId) {

		return service.doSomething(participantId, data);
	}
}
