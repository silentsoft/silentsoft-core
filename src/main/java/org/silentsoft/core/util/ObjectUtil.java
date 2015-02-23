package org.silentsoft.core.util;

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
}
