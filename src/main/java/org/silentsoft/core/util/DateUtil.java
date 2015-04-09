package org.silentsoft.core.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
	
	public static final String DATEFORMAT_TIME_NORMAL = "hh:mm:ss";
	
	public static final String DATEFORMAT_WINDOWS_NORMAL = "yy-MM-dd";
	public static final String DATEFORMAT_WINDOWS_BLENDED = "MM-dd-yy";
	public static final String DATEFORMAT_WINDOWS_REVERSE = "dd-MM-yy";
	
	public static final String DATEFORMAT_YYYYMMDDHHMMSS = "yyyyMMddhhmmss";
//	public static final String DATEFORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	
	public static final String DATEFORMAT_YYYYMMDDHH24MISS = "YYYYMMDDHH24MISS";
	
	public static final char UPPER_CHAR_YEAR = 'Y';
	public static final char UPPER_CHAR_MONTH = 'M';
	public static final char UPPER_CHAR_DATE = 'D';
	public static final String UPPER_STR_YMD = "YMD";
	public static final String UPPER_STR_MDY = "MDY";
	public static final String UPPER_STR_DMY = "DMY";
	
	private static boolean isContain(char source, char[] target) {
		for (char c : target) {
			if (Character.compare(c, source) == 0) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String getDateAsStr(Date date, String format) {
		return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
	}
	
	public static String getSystemDateAsStr(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}
	
	public static String getSystemDateFormat() {
		char[] localFormat = new SimpleDateFormat().toPattern().toCharArray();

		char[] dateChars = new char[]{DateUtil.UPPER_CHAR_YEAR, DateUtil.UPPER_CHAR_MONTH, DateUtil.UPPER_CHAR_DATE};
		StringBuffer patternFormat = new StringBuffer();
		for (char cFormat : localFormat) {
			if (dateChars.length <= patternFormat.length()) {
				break;
			}
			
			cFormat = Character.toUpperCase(cFormat);
			if (isContain(cFormat, dateChars) && !isContain(cFormat, patternFormat.toString().toCharArray())) {
				patternFormat.append(cFormat);
			}
		}
		
		switch (patternFormat.toString()) {
			case UPPER_STR_YMD:
				return DATEFORMAT_WINDOWS_NORMAL;
			case UPPER_STR_MDY:
				return DATEFORMAT_WINDOWS_BLENDED;
			case UPPER_STR_DMY:
				return DATEFORMAT_WINDOWS_REVERSE;
			default:
				LOGGER.error("Not defined system date format <{}>", new Object[]{patternFormat.toString()});
		}
		
		return null;
	}
	
	public static void setSystemDateByNTP(String serverIp) throws IOException {
		setSystemDate(getServerDate(serverIp));
	}
	
	public static Date getServerDate(String serverIp) {
		NTPUDPClient client = new NTPUDPClient();
		client.setDefaultTimeout(10000); // 10 sec timeout.
		try {
			client.open();
			
			try {
				InetAddress hostAddr = InetAddress.getByName(serverIp);
				NtpV3Packet message = client.getTime(hostAddr).getMessage();
				
				return message.getReceiveTimeStamp().getDate();
			} catch (IOException ioe) {
				LOGGER.error(ioe.toString());
			}
		} catch (SocketException e) {
			LOGGER.error(e.toString());
		}
		
		return null;
	}
	
	public static void setSystemDate(Date date) throws IOException {
		String strSysDateFormat = getSystemDateFormat();
		if (strSysDateFormat != null) {
			String strDate = getDateAsStr(date, strSysDateFormat);
			String strTime = getDateAsStr(date, DATEFORMAT_TIME_NORMAL);
			
			setSystemDate(strDate, strTime);
		} else {
			LOGGER.error("Cannot change system date. It may cause wrong format !");
		}
	}
	
	public static void setSystemDate(String date, String time) throws IOException {
		Runtime.getRuntime().exec("cmd /c date " + date);
		Runtime.getRuntime().exec("cmd /c time " + time);
	}
}
