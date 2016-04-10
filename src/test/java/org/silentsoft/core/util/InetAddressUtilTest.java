package org.silentsoft.core.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class InetAddressUtilTest {

	@Test
	public void getAddressTest() {
		System.out.println(InetAddressUtil.getAddress());
	}
	
	@Test
	public void isIncludeTest() {
		{
			List<String> addresses = new ArrayList<String>();
			addresses.add("10.120.172.3");
			addresses.add("10.120.173.3");
			
//			Assert.assertEquals(true, InetAddressUtil.isInclude(addresses));
		}
		{
			List<String> addresses = new ArrayList<String>();
			addresses.add("9.*.*.*");
			addresses.add("10.120.*.*");
			
//			Assert.assertEquals(true, InetAddressUtil.isInclude(addresses));
		}
	}
}
