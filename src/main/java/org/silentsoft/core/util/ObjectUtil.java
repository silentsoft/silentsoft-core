package org.silentsoft.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ObjectUtil {
	public enum IndexCaseType {
		UPPER_CASE, LOWER_CASE
	}
	
	/**
	 * str 문자열에서 index에 해당하는 부분만 대문자, 혹은 소문자로 고친다. (IndexCaseTypes에 따른 설정)
	 * 
	 * @param str
	 *            target
	 * @param index
	 *            character index
	 * @param type
	 *            lower or upper
	 * @return
	 */
	public static String getIndexCase(String str, int index, IndexCaseType type) {
		StringBuffer sb = new StringBuffer();

		// DEFAULT UPPERCASE
		char indexChar = Character.toUpperCase(str.charAt(index));
		if (type == IndexCaseType.LOWER_CASE) {
			indexChar = Character.toLowerCase(str.charAt(index));
		}

		switch (index) {
		case 0:
			sb.append(indexChar).append(str.substring(index + 1));
			break;
		default:
			sb.append(str.substring(0, index)).append(indexChar).append(str.substring(index + 1));
			break;
		}
		return sb.toString();
	}
	
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
	
	public static <T extends Object> T bindValue(final T dvo, String methodName, Object emptyValue) throws Exception {
		Class<? extends Object> clazz = dvo.getClass();
		String _methodName = getIndexCase(methodName, 0, IndexCaseType.UPPER_CASE);
		Method getMethod = clazz.getDeclaredMethod("get".concat(_methodName));
		Object value = getMethod.invoke(dvo);

		if (isEmpty(value)) {
			Method setMethod = clazz.getDeclaredMethod("set".concat(_methodName), emptyValue.getClass());
			setMethod.invoke(dvo, emptyValue);
		}
		
		return dvo;
	}
	
	public static Map<String, Object> bindMapValue(final Map<String, Object> map, String fieldName, Object emptyValue) throws Exception {
		Object value = map.get(fieldName);
		
		if (isEmpty(value)) {
			map.put(fieldName, emptyValue);
		}
		
		return map;
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
	
	public static boolean isEqual(Object o1, Object o2)
	{
		if (o1 == null && o2 == null)
		{
			return true;
		} else if ((o1 == null && o2 != null) || (o1 != null && o2 == null))
		{
			return false;
		}

		if (o1.toString().equals(o2.toString()))
		{
			return true;
		}

		return false;
	}
}
