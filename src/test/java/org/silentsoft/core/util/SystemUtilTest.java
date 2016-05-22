package org.silentsoft.core.util;

import junit.framework.Assert;

import org.junit.Test;

public class SystemUtilTest {

	@Test
	public void findProcessByName() {
		Assert.assertEquals(SystemUtil.findProcessByName("cmd.exe"), true);
	}
	
	@Test
	public void findProcessByPID() {
		Assert.assertEquals(SystemUtil.findProcessByPID("10124"), true);
	}
	
}
