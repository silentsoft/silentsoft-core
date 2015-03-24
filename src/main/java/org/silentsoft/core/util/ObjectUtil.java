package org.silentsoft.core.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ObjectUtil {
	public static String toString(Object obj) {
		return obj.toString();
	}
	
	public static int toInt(Object obj) {
		return (obj == null) ? 0 : ((Integer)obj).intValue();
	}
	
	public static boolean toBoolean(Object obj) {
		return ((Boolean)obj).booleanValue(); 
	}
	
	public static Map toMap(Object obj) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();

			Field[] fields = obj.getClass().getDeclaredFields();
			for (int i = 0, nLength = fields.length; i < nLength; i++) {
				fields[i].setAccessible(true);
				returnMap.put(fields[i].getName(), fields[i].get(obj));
			}

			return returnMap;
		} catch (Exception e) {
			;
		}

		return null;
	}
	
	public static boolean isEmptyVO(Object obj) {
		try {
			Field[] fields = obj.getClass().getDeclaredFields();
			for (int i = 0, nLength = fields.length; i < nLength; i++) {
				fields[i].setAccessible(true);
				if (isNotEmpty(fields[i].get(obj))) {
					return false;
				}
			}
		} catch (Exception e) {
			;
		}
		
		return true;
	}
	
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else if (obj instanceof String || obj instanceof StringBuffer) {
			String str = obj.toString().trim();
			return str.isEmpty() || str.equalsIgnoreCase("null");
		} else if (obj instanceof Object[]) {
			return ((Object[]) obj).length == 0;
		} else if (obj instanceof Collection<?>) {
			return ((Collection<?>) obj).isEmpty();
		} else if (obj instanceof Map<?, ?>) {
			return ((Map<?, ?>) obj).isEmpty();
		}
		return false;
	}
	
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}
}
