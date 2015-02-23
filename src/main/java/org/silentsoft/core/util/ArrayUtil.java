package org.silentsoft.core.util;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.control.Tab;


public final class ArrayUtil {
	
	public static boolean isInclude(Object[] source, Object target) {
		return isInclude(source, new Object[]{target});
	}
	
	/**
	 * if source contains target, return true nor return false.
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean isInclude(Object[] source, Object[] target) {
		boolean returnValue = true;
		
		int count = 0;
		if (target.length <= source.length) {
			for (int a=0, b=target.length; a<b; a++) {
				for (int c=0, d=source.length; c<d; c++) {
					if (source[c].equals(target[a])) {
						count++;
					}
				}
			}
			
			if (count != target.length) {
				returnValue = false;
			}
		} else {
			returnValue = false;
		}
		
		return returnValue;
	}
	
	public static <T> T[] getDifference(T[] source, T[] target) {
		ArrayList<T> diff = new ArrayList<T>();
		
		if (target.length <= source.length) {
			for (int a=0, b=target.length; a<b; a++) {
				for (int c=0, d=source.length; c<d; c++) {
					if (!source[c].equals(target[a])) {
						diff.add(source[c]);
					}
				}
			}
		} else {
			diff = null;
		}
		
		T[] tList = (T[]) new Object[diff.size()];
		return diff.toArray(tList);
	}
}
