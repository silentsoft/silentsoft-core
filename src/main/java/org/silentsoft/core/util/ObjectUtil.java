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
	 * index of str will be change to upper or lower case.
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
		return (isEmpty(obj)) ? "" : obj.toString();
	}
	
	public static String toString(Object obj, String emptyThen) {
		return (isEmpty(obj)) ? emptyThen : toString(obj);
	}
	
	/**
	 * if obj is empty, return 0. otherwise, convert to int value.
	 * @param obj
	 * @return
	 */
	public static int toInt(Object obj) {
		int toInt = 0;
		
		if (isNotEmpty(obj)) {
			if (obj instanceof String) {
				toInt = Integer.valueOf((String) obj);
			} else if (obj instanceof Float) {
				toInt = (int) ((float) obj);
			} else if (obj instanceof Double) {
				toInt = (int) ((double) obj);
			} else if (obj instanceof Boolean) {
				toInt = ((Boolean) obj).compareTo(false);
			} else {
				String toString = obj.toString();
				toInt = Integer.valueOf(toString);
			}
		}
		
		return toInt;
	}
	
	public static int toInt(Object obj, int emptyThen) {
		return (isEmpty(obj)) ? emptyThen : toInt(obj);
	}
	
	/**
	 * if obj is empty, return false. otherwise, convert to boolean value.
	 * @param obj
	 * @return
	 */
	public static boolean toBoolean(Object obj) {
		boolean toBoolean = false;
		
		if (isNotEmpty(obj)) {
			if (obj instanceof String) {
				toBoolean = Boolean.valueOf((String) obj);
			} else if (obj instanceof Integer) {
				toBoolean = ((Integer) obj) == 1 ? true : false;
			} else {
				toBoolean = ((Boolean) obj).booleanValue();
			}
		}
		
		return toBoolean; 
	}
	
	public static boolean toBoolean(Object obj, boolean emptyThen) {
		return (isEmpty(obj)) ? emptyThen : toBoolean(obj);
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
	
	public static String fillString(String strData, int iLength, String strFilter) {
		return fillString(strData, iLength, strFilter, false);
	}
	
	public static String fillString(String strData, int iLength, String strFilter, boolean bRight) {
		if (iLength <= strData.length()) return strData;
		
		StringBuffer returnString = new StringBuffer();
		
		int iCount = iLength - strData.length();
		for (int i=0; i<iCount; i++) {
			returnString.append(strFilter);
		}
		
		if (bRight) {
			returnString.insert(0, strData);
		} else {
			returnString.insert(iCount, strData);
		}
		
		return returnString.toString();		
	}
	
	public static <T extends Object> T bindValue(final T dvo, String methodName, Object emptyValue) throws Exception {
		return bindValue(dvo, methodName, emptyValue, false);
	}
	
	public static <T extends Object> T bindValue(final T dvo, String methodName, Object emptyValue, boolean force) throws Exception {
		Class<? extends Object> clazz = dvo.getClass();
		String _methodName = getIndexCase(methodName, 0, IndexCaseType.UPPER_CASE);
		Method getMethod = clazz.getDeclaredMethod("get".concat(_methodName));
		Object value = getMethod.invoke(dvo);

		if (force || isEmpty(value)) {
			Method setMethod = clazz.getDeclaredMethod("set".concat(_methodName), emptyValue.getClass());
			setMethod.invoke(dvo, emptyValue);
		}
		
		return dvo;
	}
	
	public static Map<String, Object> bindMapValue(final Map<String, Object> map, String fieldName, Object emptyValue) throws Exception {
		return bindMapValue(map, fieldName, emptyValue, false);
	}
	
	public static Map<String, Object> bindMapValue(final Map<String, Object> map, String fieldName, Object emptyValue, boolean force) throws Exception {
		Object value = map.get(fieldName);
		
		if (force || isEmpty(value)) {
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
