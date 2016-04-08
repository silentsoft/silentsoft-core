package org.silentsoft.core.util;

import java.util.UUID;

public class GenerateUtil {

	public static String generateUUID() {
		return generateUUID(false);
	}
	
	public static String generateUUID(boolean includeHyphen) {
		String uuid = UUID.randomUUID().toString();
		return (includeHyphen ? uuid : uuid.replaceAll("-", ""));
	}
	
}
