package com.superluli.jpa;

import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtils {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private CommonUtils() {

	}

	public static String generateSessionId() {

		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}
	
	public static String generateUUID() {

		return UUID.randomUUID().toString();
	}

	public static String toJson(Object o) {

		try {
			return MAPPER.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return MAPPER.createObjectNode().put("ERROR", e.getMessage())
					.toString();
		}
	}
}
