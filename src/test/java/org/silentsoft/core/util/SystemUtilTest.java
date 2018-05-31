package org.silentsoft.core.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.silentsoft.core.util.SystemUtil.ProcessInfo;

import junit.framework.Assert;

public class SystemUtilTest {

//	@Test
	public void findProcessByName() {
		Assert.assertEquals(SystemUtil.findProcessByImageName("cmd.exe"), true);
	}
	
//	@Test
	public void findProcessByPID() {
		Assert.assertEquals(SystemUtil.findProcessByPID("10124"), true);
	}
	
//	@Test
	public void addLibraryPathTest() {
		Assert.assertEquals(true, SystemUtil.addLibraryPath("C:\\test"));
	}
	
	@Test
	public void printOSArchitecture() {
		System.out.println(SystemUtil.getOSArchitecture());
	}
	
//	@Test
	public void printPlatformArchitecture() {
		System.out.println(SystemUtil.getPlatformArchitecture());
	}
	
	@Test
	public void printOS() {
		System.out.println(SystemUtil.getOS());
	}
	
	@Test
	public void printCurrentPID() {
		System.out.println(SystemUtil.getCurrentProcessId());
	}
	
	@Test
	public void printCurrentProcess() {
		long start = System.currentTimeMillis();
		String pid = SystemUtil.getCurrentProcessId();
		ProcessInfo processInfo = SystemUtil.getProcessInfoByPID(pid);
		System.out.println(processInfo.getPid());
		System.out.println(processInfo.getSessionId());
		System.out.println(processInfo.getUserName());
		System.out.println(processInfo.getImageName());
		System.out.println(System.currentTimeMillis() - start + " ms");
	}
	
//	@Test
	public void printCurrentProcess2() throws Exception {
		String psCommand = String.join("", "ps -eo ucomm=,pid=,sess=,user= ", "350");
		String awkCommand = String.join("", "awk '{print ", "\"\\\"\"", String.join("\"\\\",\" \"\\\"\"", "$1", "$2", "$3", "$4"), "\"\\\"\"}'");
		
		Process p1 = Runtime.getRuntime().exec(psCommand);
	    InputStream input = p1.getInputStream();
	    Process p2 = Runtime.getRuntime().exec(awkCommand);
	    OutputStream output = p2.getOutputStream();
	    IOUtils.copy(input, output);
	    output.close(); // signals grep to finish
	    List<String> result = IOUtils.readLines(p2.getInputStream());
	    System.out.println(result);
	}
}
