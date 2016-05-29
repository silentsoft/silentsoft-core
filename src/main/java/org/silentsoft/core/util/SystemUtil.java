package org.silentsoft.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.silentsoft.core.util.elevator.core.Elevator;

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
	public static Process runCommand(String command) throws IOException {
		return Runtime.getRuntime().exec("cmd /C " + command);
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
	
	/**
	 * Returns <tt>true</tt> if the specific <code>name</code> process is exists, otherwise <tt>false</tt>. 
	 * @param name
	 * @return
	 */
	public static boolean findProcessByName(String name) {
		return findProcess("IMAGENAME", name);
	}
	
	/**
	 * Returns <tt>true</tt> if the specific <code>pid</code> process is exists, otherwise <tt>false</tt>.
	 * @param pid
	 * @return
	 */
	public static boolean findProcessByPID(String pid) {
		return findProcess("PID", pid);
	}
	
	private static boolean findProcess(String command, String target) {
		boolean result = false;
		
		try {
			if (target != null && target.length() > 0) {
				Process process = runCommand(String.format("%s%s%s%s%s%s%s", "tasklist /FI \"", command, " eq ", target, "\" | find /I \"", target, "\""));
				StreamReader reader = new StreamReader(process.getInputStream());
				
				reader.start();
				process.waitFor();
				reader.join();
				
				result = (reader.getResult().trim().length() == 0 ? false : true);
			}
		} catch (Exception e) {
			;
		}
		
		return result;
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
}