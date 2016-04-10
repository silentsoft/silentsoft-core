package org.silentsoft.core.otp;

import org.junit.Test;

public class OTPTest {

	@Test
	public void createTest() throws Exception {
		System.out.println(OTP.create("my otp secret key!".getBytes(), 30000));
	}
	
	@Test
	public void vertifyTest() throws Exception {
//		Assert.assertEquals(true, OTP.vertify("my otp secret key!".getBytes(), 30000, "573440"));
	}
	
}
