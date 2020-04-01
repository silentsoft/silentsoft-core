package org.silentsoft.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JSONUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtil.class);
	
	public static String ObjectToString(Object target) {
		return ObjectToString(target, true);
	}
	
	public static String ObjectToString(Object target, boolean beautify) {
		String value = null;
		
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, beautify);
			
			value = objectMapper.writeValueAsString(target);
		} catch (JsonProcessingException e) {
			LOGGER.error("", e);
		}
		
		return value;
	}
	
	public static <T> T JSONToObject(String target, Class<T> returnType) throws Exception {
		return (T) new ObjectMapper().readValue(target, returnType);
	}
	
}
