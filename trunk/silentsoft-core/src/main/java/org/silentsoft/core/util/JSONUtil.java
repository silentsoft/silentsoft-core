package org.silentsoft.core.util;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
	
	public static String ObjectToString(Object target) {
		return new JSONObject(target).toString();
	}
	
	public static JSONObject ObjectToJSON(Object target) {
		return new JSONObject(target);
	}
	
	public static <T> T JSONToObject(String target, Class<T> returnType) throws Exception {
		return (T)new ObjectMapper().readValue(target, returnType);
	}
	
}
