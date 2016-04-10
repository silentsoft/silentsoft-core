package org.silentsoft.core.util;

import java.rmi.dgc.VMID;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.junit.Test;

public class GenerateUtilTest {

	@Test
	public void UUID() {
		System.out.println(UUID.randomUUID().toString());
	}

	@Test
	public void VMID() {
		System.out.println(new VMID().toString());
	}

	@Test
	public void Date() {
		System.out.println(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
	}
}
