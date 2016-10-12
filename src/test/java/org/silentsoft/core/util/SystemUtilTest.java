package org.silentsoft.core.util;

import junit.framework.Assert;

import org.junit.Test;

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
	
//	@Test
	public void printOSArchitecture() {
		System.out.println(SystemUtil.getOSArchitecture());
	}
	
//	@Test
	public void printPlatformArchitecture() {
		System.out.println(SystemUtil.getPlatformArchitecture());
	}
	
}
