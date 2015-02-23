/*
 * Copyright © Hyesung Lee. All Rights Reserved.
 */

package org.silentsoft.core.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class BaseUtil {
	
	public static String toString(int iNumber) {
		return iNumber+"";
	}
	
	public static String fillData(String strData, int iLength, String strFilter) {
		return fillData(strData, iLength, strFilter, false);
	}
	
	public static String fillData(String strData, int iLength, String strFilter, boolean bRight) {
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
	
	public static Map toMap(Object sourceDVO) {
		try {
			Map<String, Object> returnMap = new HashMap<String, Object>();

			Field[] fields = sourceDVO.getClass().getDeclaredFields();
			int nLength = fields.length;
			for (int i = 0; i < nLength; i++) {
				fields[i].setAccessible(true);
				returnMap.put(fields[i].getName(), fields[i].get(sourceDVO));
			}

			return returnMap;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
