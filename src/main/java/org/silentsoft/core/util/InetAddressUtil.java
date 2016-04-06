package org.silentsoft.core.util;

import java.net.InetAddress;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author silentsoft
 */
public class InetAddressUtil {

	/**
     * Returns the IP address string in textual presentation.
     *
     * @return  the raw IP address in a string format.
     */
	public static String getAddress() {
		String value = null;
		
		try {
			value = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			;
		}
		
		return value;
	}
	
	/**
	 * This method will pass parameter to {@link #isInclude(String, List)} as <code>isInclude(getAddress(), addresses);</code>
	 * 
	 * @param addresses
	 * @return <tt>true</tt> if the addresses are include this machine's raw IP address
	 * @author silentsoft
	 */
	public static boolean isInclude(List<String> addresses) {
		return isInclude(getAddress(), addresses);
	}
	
	/**
	 * Check whether <code>addresses</code> are include <code>source</code> even if there are asterisk in the given string.
	 * 
	 * @param source
	 * @param addresses
	 * @return <tt>true</tt> if the addresses are include given parameter <code>source</code>
	 * @author silentsoft
	 */
	public static boolean isInclude(String source, List<String> addresses) {
		boolean value = false;
		
		try {
			if ("*".equals(source) || "*.*.*.*".equals(source) || "*.*.*.*.*.*".equals(source)) {
				value = true;
			} else if (addresses.contains(source) || addresses.contains("*") || addresses.contains("*.*.*.*") || addresses.contains("*.*.*.*.*.*")) {
				value = true;
			} else {
				for (String target : addresses) {
					if (source.contains("*") || target.contains("*")) {
						String[] sourceElements = source.split(Pattern.quote("."));
						String[] targetElements = target.split(Pattern.quote("."));
						if (sourceElements.length == targetElements.length) {
							boolean isMatch = true;
							for (int i=0, j=sourceElements.length; i<j; i++) {
								if ("*".equals(sourceElements[i]) || "*".equals(targetElements[i])) {
									continue;
								} else if (sourceElements[i].equals(targetElements[i])) {
									continue;
								} else {
									isMatch = false;
									break;
								}
							}
							if (isMatch) {
								value = true;
								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			;
		}
		
		return value;
	}
}
