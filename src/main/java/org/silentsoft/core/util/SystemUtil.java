package org.silentsoft.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.util.elevator.core.Elevator;
import org.silentsoft.core.util.elevator.extend.CLibrary;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public final class SystemUtil {
	
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
	
	public static class ProcessInfo {
		private String pid;
		private String sessionId;
		private String userName;
		private String imageName;
		
		public ProcessInfo(String pid, String sessionId, String userName, String imageName) {
			this.pid = pid;
			this.sessionId = sessionId;
			this.userName = userName;
			this.imageName = imageName;
		}

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public String getSessionId() {
			return sessionId;
		}

		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getImageName() {
			return imageName;
		}

		public void setImageName(String imageName) {
			this.imageName = imageName;
		}
	}
	
	public static String getHostAddress() {
		InetAddress localAddress = getLocalAddress();
		if (localAddress == null) {
			try {
				return Inet4Address.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				;
			}
		} else {
			return localAddress.getHostAddress();
		}
		
		return "";
	}
	
	private static InetAddress getLocalAddress() {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				List<InterfaceAddress> interfaceAddresses = networkInterfaces.nextElement().getInterfaceAddresses();
				for (InterfaceAddress interfaceAddress : interfaceAddresses) {
					InetAddress address =interfaceAddress.getAddress();
					if (address.isSiteLocalAddress()) {
						return address;
					}
				}
			}
		} catch (Exception e) {
			;
		}
		
		return null;
	}
	
	/**
	 * Run windows CMD command.
	 * @param command pure CMD command
	 * @throws IOException
	 */
	public static Process runCommand(String command) throws IOException {
		if (isWindows()) {
			return Runtime.getRuntime().exec("cmd /C " + command);
		}
		
		return Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", command});
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
	
	public static Image getIconFromExtensionFx(String extension) {
		return getIconFromExtensionFx(extension, 0, 0);
	}
	
	public static Image getIconFromExtensionFx(String extension, int width, int height) {
		return SwingFXUtils.toFXImage(getIconFromExtension(extension, width, height), null);
	}
	
	public static BufferedImage getIconFromExtension(String extension) {
		return getIconFromExtension(extension, 0, 0);
	}
	
	private static Map<String, BufferedImage> extensionMap = new HashMap<String, BufferedImage>();	
	public static BufferedImage getIconFromExtension(String extension, int width, int height) {
		if (extension == null) {
			return null;
		}
		
		BufferedImage bufferedImage = extensionMap.get(extension);
		if (bufferedImage == null) {
			File file = null;
			try {
				boolean isDirectory = (extension.length() <= 0) ? true : false;
				
				if (isDirectory) {
					file = Files.createTempDirectory("temp").toFile();
				} else {
					file = Files.createTempFile("temp", ".".concat(extension)).toFile();
				}
				
		        // Windows {
				Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
		        // }
	
		        // OS X {
		        //final JFileChooser fileChooser = new JFileChooser();
		        //Icon icon = fileChooser.getUI().getFileView(fileChooser).getIcon(file);
		        // }
				
	            width = (width == 0) ? icon.getIconWidth() : width;
	            height = (height == 0) ? icon.getIconHeight() : height;
	            
	            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            icon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
	            
	            extensionMap.put(extension, bufferedImage);
			} catch (Exception e) {
				
			} finally {
				if (file != null) {
					file.delete();
				}
			}
		}
		
		return bufferedImage;
	}
	
	/**
	 * Get system time by yyyyMMddhhmmss format.
	 * @return
	 */
	public static String getSystemTime() {
		StringBuffer systemTime = new StringBuffer();
		
		Calendar calendar = Calendar.getInstance();
		systemTime.append(ObjectUtil.fillString(ObjectUtil.toString(calendar.get(Calendar.YEAR)), 4, "0"));
		systemTime.append(ObjectUtil.fillString(ObjectUtil.toString(calendar.get(Calendar.MONTH)+1), 2, "0"));
		systemTime.append(ObjectUtil.fillString(ObjectUtil.toString(calendar.get(Calendar.DAY_OF_MONTH)), 2, "0"));
		systemTime.append(ObjectUtil.fillString(ObjectUtil.toString(calendar.get(Calendar.HOUR_OF_DAY)), 2, "0"));
		systemTime.append(ObjectUtil.fillString(ObjectUtil.toString(calendar.get(Calendar.MINUTE)), 2, "0"));
		systemTime.append(ObjectUtil.fillString(ObjectUtil.toString(calendar.get(Calendar.SECOND)), 2, "0"));
		
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
		
		paramDate.append(ObjectUtil.fillString(ObjectUtil.toString(date.getMonth()+1), 2, "0"));
		paramDate.append("-");
		paramDate.append(ObjectUtil.fillString(ObjectUtil.toString(date.getDate()), 2, "0"));
		paramDate.append("-");
		paramDate.append(ObjectUtil.toString(date.getYear()).substring(2, 4));
		
		paramTime.append(ObjectUtil.fillString(ObjectUtil.toString(date.getHours()), 2, "0"));
		paramTime.append(":");
		paramTime.append(ObjectUtil.fillString(ObjectUtil.toString(date.getMinutes()), 2, "0"));
		paramTime.append(":");
		paramTime.append(ObjectUtil.fillString(ObjectUtil.toString(date.getSeconds()), 2, "0"));
		
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
		runCommand(String.format("%s%s", "date ", date)); // dd-MM-yy
		runCommand(String.format("%s%s", "time ", time)); // hh:mm:ss
	}
	
	/**
	 * SysUtil.readRegistry("HKLM\\Software\\Microsoft\\Internet Explorer", "Version");
	 * @param location example : "HKLM\\Software\\Microsoft\\Internet Explorer"
	 * @param key example : "Version"
	 * @return
	 * @throws Exception
	 */
	public static String readRegistry(String location, String key) throws Exception {
		if (isWindows()) {
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
		
		return null;
	}
	
	public enum RegType {
		REG_SZ,
		REG_DWORD
		//,REG_QWORD, ...
	}
	
	/**
	 * if operating system is higher version then windows XP, you need to start this program "run as administrator".
	 * SysUtil.writeRegistry("HKLM\\Software\\Microsoft\\Internet Explorer", "Info", "REG_SZ", "FOR TEST.");
	 * @param location example : "HKLM\\Software\\Microsoft\\Internet Explorer"
	 * @param key example : "Info"
	 * @param regType example : REG_SZ, REG_DWORD
	 * @param value example : "FOR TEST."
	 * @throws IOException
	 */
	public static void writeRegistry(String location, String key, RegType regType, Object value) throws IOException {
		if (isWindows()) {
			Runtime.getRuntime().exec(REGADD_UTIL + "\"" + location + "\"" + " /v " + "\"" + key + "\"" + " /t " + regType.name() + " /d " + (regType == RegType.REG_SZ ? "\"" + value + "\"" : value) + " /f");
		}
	}
	
	/**
	 * Write registry as administrator.
	 * @param location
	 * @param key
	 * @param regType
	 * @param value
	 * @see #writeRegistry(String, String, RegType, Object)
	 */
	public static void writeRegistryAsAdmin(String location, String key, RegType regType, Object value) {
		if (isWindows()) {
			Elevator.executeAsAdmin("c:\\windows\\system32\\reg.exe", "add " + "\"" + location + "\"" + " /v " + "\"" + key + "\"" + " /t " + regType.name() + " /d " + (regType == RegType.REG_SZ ? "\"" + value + "\"" : value) + " /f");
		}
	}
	
	/**
	 * Returns current process's PID as string.
	 * @return
	 */
	public static String getCurrentProcessId() {
		/*if (isWindows()) {
			return String.valueOf(Kernel32.INSTANCE.GetCurrentProcessId());
		} else*/ if (isMac() || isLinux()) {
			return String.valueOf(CLibrary.INSTANCE.getpid());
		}
		
		return ManagementFactory.getRuntimeMXBean().getName().split(CommonConst.AT)[CommonConst.FIRST_INDEX];
	}

	
	/**
	 * Returns <tt>true</tt> if the specific <code>imageName</code> process is exists, otherwise <tt>false</tt>.
	 * @param imageName
	 * @return
	 */
	public static boolean findProcessByImageName(String imageName) {
		return getProcessInfoByImageName(imageName).isEmpty() ? false : true;
	}
	
	/**
	 * Returns <tt>true</tt> if the process that specific <code>imageName</code> but not matched the specific <code>excludePid</code> is exists, otherwise <tt>false</tt>.
	 * @param imageName
	 * @param excludePid
	 * @return
	 */
	public static boolean findProcessByImageName(String imageName, String excludePid) {
		List<ProcessInfo> processes = getProcessInfoByImageName(imageName);
		return processes.stream().filter(processInfo -> processInfo.getPid().equals(excludePid) == false).count() == 0 ? false : true;
	}
	
	/**
	 * Returns <tt>true</tt> if the specific <code>pid</code> process is exists, otherwise <tt>false</tt>.
	 * @param pid
	 * @return
	 */
	public static boolean findProcessByPID(String pid) {
		return getProcessInfoByPID(pid) == null ? false : true;
	}
	
	/**
	 * Returns specific <code>imageName</code> processes as {@link ProcessInfo} list.
	 * if there are no specific <code>imageName</code> processes, returns an empty list.
	 * @param imageName
	 * @return
	 */
	public static List<ProcessInfo> getProcessInfoByImageName(String imageName) {
		return findProcess("IMAGENAME", imageName);
	}

	/**
	 * Returns <tt>null</tt> if the specific <code>pid</code> process is not exists, otherwise {@link ProcessInfo}.
	 * @param pid
	 * @return
	 */
	public static ProcessInfo getProcessInfoByPID(String pid) {
		List<ProcessInfo> processes = findProcess("PID", pid);
		return processes.isEmpty() ? null : processes.get(CommonConst.FIRST_INDEX);
	}
	
	/**
	 * Find process by given <code>command</code>(could be "IMAGENAME" or "PID") and <code>target</code>(could be image name or PID value).
	 * and then returns {@link ProcessInfo} as list that contains process information(image name, PID, session name, memory usage, status, user name, CPU time, window title).
	 * @param command
	 * @param target
	 * @return
	 */
	private static List<ProcessInfo> findProcess(String command, String target) {
		List<ProcessInfo> processes = new ArrayList<ProcessInfo>();
		
		try {
			if (target != null && target.length() > 0) {
				Process process = null;
				
				if (isWindows()) {
					process = runCommand(String.join("", "tasklist /V /FO \"CSV\" /FI \"", command, " eq ", target, "\" | findstr /I \"", target, "\""));
				} else {
					if ("IMAGENAME".equals(command)) {
						process = runCommand(String.join("", "ps -eo ucomm=,pid=,sess=,user= | grep -w \"^", target, " \"", " | awk '{print ", "\"\\\"\"", String.join("\"\\\",\" \"\\\"\"", "$1", "$2", "$3", "$4"), "\"\\\"\"}'"));
					} else if ("PID".equals(command)) {
						process = runCommand(String.join("", "ps -eo ucomm=,pid=,sess=,user= ", target, " | awk '{print ", "\"\\\"\"", String.join("\"\\\",\" \"\\\"\"", "$1", "$2", "$3", "$4"), "\"\\\"\"}'"));
					}
				}
				
				if (process != null) {
					StreamReader reader = new StreamReader(process.getInputStream());
					
					reader.start();
					process.waitFor();
					reader.join();
					
					String[] rows = reader.getResult().split(CommonConst.ENTER);
					for (String row : rows) {
						if ("".equals(row) || row.trim().length() == 0) {
							continue;
						}
						
						if (isWindows()) {
							String[] cols = row.split("\",\"");
							cols[0] = cols[0].replaceAll(CommonConst.QUOTATION_MARK_DOUBLE, CommonConst.NULL_STR);
							cols[cols.length-1] = cols[cols.length-1].replaceAll(CommonConst.QUOTATION_MARK_DOUBLE, CommonConst.NULL_STR);
							/**
							 * final int IMAGE_NAME = 0;
							 * final int PID = 1;
							 * final int SESSION_NAME = 2;
							 * final int SESSION_ID = 3;
							 * final int MEMORY_USAGE = 4;
							 * final int STATUS = 5;
							 * final int USER_NAME = 6;
							 * final int CPU_TIME = 7;
							 * final int WINDOW_TITLE = 8;
							 */
							processes.add(new ProcessInfo(cols[1], cols[3], cols[6], cols[0]));
						} else {
							String[] cols = row.split("\t");
							cols[0] = cols[0].replaceAll(CommonConst.QUOTATION_MARK_DOUBLE, CommonConst.NULL_STR);
							cols = cols[0].split(",");
							cols[cols.length-1] = cols[cols.length-1].replaceAll("\n", CommonConst.NULL_STR);
							
							processes.add(new ProcessInfo(cols[1], cols[2], cols[3], cols[0]));
						}
					}
				}
			}
		} catch (Exception e) {
			;
		}
		
		return processes;
	}
	
	/**
	 * Returns <tt>true</tt> if successfully added library path. otherwise, <tt>false</tt>.
	 * @param pathToAdd
	 * @return
	 */
	public static boolean addLibraryPath(String pathToAdd) {
		boolean result = true;
		
		try {
			Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
			usrPathsField.setAccessible(true);
			
			String[] paths = (String[]) usrPathsField.get(null);
			
			if (Arrays.stream(paths).anyMatch(path -> path.equals(pathToAdd))) {
				return true;
			}
			
			String[] newPaths = Arrays.copyOf(paths, paths.length+1);
			newPaths[newPaths.length-1] = pathToAdd;
			usrPathsField.set(null, newPaths);
		} catch (Exception e) {
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Returns <tt>"x64"</tt> if the current operating system is based on 64-bit. otherwise returns <tt>"x86"</tt>.
	 * @return
	 */
	public static String getOSArchitecture() {
		String osArchitecture = "";
		
		if (isWindows()) {
			try {
				Process process = runCommand("wmic OS get OSArchitecture");
				StreamReader reader = new StreamReader(process.getInputStream());
				
				reader.start();
				process.waitFor();
				reader.join();
				
				osArchitecture = reader.getResult().trim().split(CommonConst.ENTER)[1].contains("64") ? "x64" : "x86";
			} catch (Exception e) {
				;
			}
		} else {
			osArchitecture = System.getProperty("os.arch").contains("64") ? "x64" : "x86";
		}
		
		return osArchitecture;
	}
	
	/**
	 * Returns <tt>"x64"</tt> if the current platform is based on 64-bit. otherwise returns <tt>"x86"</tt>.
	 * @return
	 */
	public static String getPlatformArchitecture() {
		return System.getProperty("os.arch").contains("64") ? "x64" : "x86";
	}
	
	public enum OS {
		Windows, Linux, Mac
	}
	
	public static OS getOS() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) {
			return OS.Windows;
		} else if (osName.contains("nix") || osName.contains("nux")) {
			return OS.Linux;
		} else if (osName.contains("mac")) {
			return OS.Mac;
		}
		
		return null;
	}
	
	/**
	 * Returns <tt>"windows"</tt> or <tt>"macosx"</tt> or <tt>"linux"</tt>. otherwise returns <tt>"unknown"</tt>
	 * @return
	 */
	public static String getOSName() {
		if (isWindows()) {
			return "windows";
		} else if (isMac()) {
			return "macosx";
		} else if (isLinux()) {
			return "linux";
		}
		
		return "unknown";
	}
	
	public static boolean isWindows() {
		return getOS() == OS.Windows;
	}
	
	public static boolean isLinux() {
		return getOS() == OS.Linux;
	}
	
	public static boolean isMac() {
		return getOS() == OS.Mac;
	}
	
}