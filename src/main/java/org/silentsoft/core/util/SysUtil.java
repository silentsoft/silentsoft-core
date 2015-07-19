package org.silentsoft.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.silentsoft.core.util.elevator.core.Elevator;

public final class SysUtil {
	
	private static final String REGQUERY_UTIL = "reg query ";
	private static final String REGADD_UTIL = "reg add ";
	private static final String REGTYPE_DWORD = "REG_DWORD";
	private static final String REGTYPE_QWORD = "REG_QWORD";
	
//	private static final int REGINFO_ADDR = 0;
//	private static final int REGINFO_NAME = 1;
	private static final int REGINFO_TYPE = 2;
	private static final int REGINFO_RSLT = 3;
	
	private static class StreamReader extends Thread {
		private InputStream is;
		private StringWriter sw;

		StreamReader(InputStream is) {
			this.is = is;
			sw = new StringWriter();
		}

		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {
				;
			}
		}

		String getResult() {
			return sw.toString();
		}
	}
	
	public static String getHostAddress() {
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			;
		}
		
		return "";
	}
	
	/**
	 * Run windows CMD command.
	 * @param command pure CMD command
	 * @throws IOException
	 */
	public static void runCommand(String command) throws IOException {
		Runtime.getRuntime().exec("cmd /C " + command);
	}
	
	/**
	 * Run windows CMD command as administrator.
	 * @param command
	 */
	public static void runCommandAsAdmin(String command) {
		Elevator.executeAsAdmin("c:\\windows\\system32\\cmd.exe", "/C " + command);
	}
	
	/**
	 * Run program using windows CMD command.
	 * @param target
	 * @throws IOException
	 */
	public static void runProgram(String target) throws IOException {
		runProgram(target, "");
	}
	
	/**
	 * Run program using windows CMD command with specific arguments.
	 * @param target
	 * @param args
	 * @throws IOException
	 */
	public static void runProgram(String target, String args) throws IOException {
		args = (args == null) ? "" : args;
		Runtime.getRuntime().exec("cmd /C start " + target + " " + args);
	}
	
	/**
	 * Run program as administrator using windows CMD command.
	 * @param target
	 */
	public static void runProgramAsAdmin(String target) {
		runProgramAsAdmin(target, "");
	}
	
	/**
	 * Run program as administrator using windows CMD command with specific arguments.
	 * @param target
	 * @param args
	 */
	public static void runProgramAsAdmin(String target, String args) {
		args = (args == null) ? "" : args;
		Elevator.executeAsAdmin("c:\\windows\\system32\\cmd.exe", "/C start " + target + " " + args);
	}
	
	/**
	 * Execute program as administrator. (not using windows CMD command)
	 * @param target
	 */
	public static void executeAsAdmin(String target) {
		executeProgramAsAdmin(target, "");
	}
	
	/**
	 * Execute program as administrator with specific arguments. (not using windows CMD command)
	 * @param target
	 * @param args
	 */
	public static void executeProgramAsAdmin(String target, String args) {
		args = (args == null) ? "" : args;
		Elevator.executeAsAdmin(target, args);
	}
	
	/**
	 * get default system language code(ex: en, ko, ..)
	 * @return
	 */
	public static String getLanguage() {
		return Locale.getDefault().getLanguage();
	}
	
	/**
	 * return <code>language</code> is system language or not 
	 * @param language
	 * @return
	 */
	public static boolean isSystemLanguage(String language) {
		return getLanguage().equals(new Locale(language).getLanguage());
	}
	
	/**
	 * Get system time by yyyyMMddhhmmss format.
	 * @return
	 */
	public static String getSystemTime() {
		StringBuffer systemTime = new StringBuffer();
		
		Calendar calendar = Calendar.getInstance();
		systemTime.append(BaseUtil.fillData(BaseUtil.toString(calendar.get(Calendar.YEAR)), 4, "0"));
		systemTime.append(BaseUtil.fillData(BaseUtil.toString(calendar.get(Calendar.MONTH)+1), 2, "0"));
		systemTime.append(BaseUtil.fillData(BaseUtil.toString(calendar.get(Calendar.DAY_OF_MONTH)), 2, "0"));
		systemTime.append(BaseUtil.fillData(BaseUtil.toString(calendar.get(Calendar.HOUR_OF_DAY)), 2, "0"));
		systemTime.append(BaseUtil.fillData(BaseUtil.toString(calendar.get(Calendar.MINUTE)), 2, "0"));
		systemTime.append(BaseUtil.fillData(BaseUtil.toString(calendar.get(Calendar.SECOND)), 2, "0"));
		
		return systemTime.toString();
	}
	
	/**
	 * Set system time by parameter date.
	 * This may need disable UAC(User Access Control).
	 * @param date
	 * @throws IOException
	 */
	public static void setSystemTime(Date date) throws IOException {
		StringBuffer paramDate = new StringBuffer();
		StringBuffer paramTime = new StringBuffer();
		
		paramDate.append(BaseUtil.fillData(BaseUtil.toString(date.getMonth()+1), 2, "0"));
		paramDate.append("-");
		paramDate.append(BaseUtil.fillData(BaseUtil.toString(date.getDate()), 2, "0"));
		paramDate.append("-");
		paramDate.append(BaseUtil.toString(date.getYear()).substring(2, 4));
		
		paramTime.append(BaseUtil.fillData(BaseUtil.toString(date.getHours()), 2, "0"));
		paramTime.append(":");
		paramTime.append(BaseUtil.fillData(BaseUtil.toString(date.getMinutes()), 2, "0"));
		paramTime.append(":");
		paramTime.append(BaseUtil.fillData(BaseUtil.toString(date.getSeconds()), 2, "0"));
		
		setSystemTime(paramDate.toString(), paramTime.toString());
	}

	/**
	 * Set system time by parameter date, time.
	 * This may need disable UAC(User Access Control).
	 * @param date dd-MM-yy
	 * @param time hh:mm:ss
	 * @throws IOException
	 */
	public static void setSystemTime(String date, String time) throws IOException {
		Runtime.getRuntime().exec("cmd /C date " + date); // dd-MM-yy
		Runtime.getRuntime().exec("cmd /C time " + time); // hh:mm:ss
	}
	
	/**
	 * SysUtil.readRegistry("HKLM\\Software\\Microsoft\\Internet Explorer", "Version");
	 * @param location example : "HKLM\\Software\\Microsoft\\Internet Explorer"
	 * @param key example : "Version"
	 * @return
	 * @throws Exception
	 */
	public static String readRegistry(String location, String key) throws Exception {
		Process process = Runtime.getRuntime().exec(REGQUERY_UTIL + "\"" + location + "\"" + " /v " + "\"" + key + "\"");
		StreamReader reader = new StreamReader(process.getInputStream());
		
		reader.start();
		process.waitFor();
		reader.join();
		
		String result[] = reader.getResult().split("    ");
		
		if (result[REGINFO_TYPE].trim().equals(REGTYPE_DWORD) || result[REGINFO_TYPE].trim().equals(REGTYPE_QWORD))
			return new BigInteger(result[REGINFO_RSLT].trim().substring("0x".length()), 16).toString();
		else
			return result[REGINFO_RSLT].trim();
	}
	
	/**
	 * if operating system is higher version then windows XP, you need to start this program "run as administrator".
	 * SysUtil.writeRegistry("HKLM\\Software\\Microsoft\\Internet Explorer", "Info", "REG_SZ", "FOR TEST.");
	 * @param location example : "HKLM\\Software\\Microsoft\\Internet Explorer"
	 * @param key example : "Info"
	 * @param type example : "REG_SZ", "REG_DWORD", "REG_QWORD", etc...
	 * @param value example : "FOR TEST."
	 * @throws IOException
	 */
	public static void writeRegistry(String location, String key, String type, String value) throws IOException {
		Runtime.getRuntime().exec(REGADD_UTIL + "\"" + location + "\"" + " /v " + "\"" + key + "\"" + " /t " + type + " /d " + "\"" + value + "\"");
	}
}