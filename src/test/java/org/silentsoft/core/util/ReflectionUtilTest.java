package org.silentsoft.core.util;

import junit.framework.Assert;

import org.junit.Test;

public class ReflectionUtilTest {
	
	private class reflectObject {
		
		private String name; 
		
		private String getName() {
			return name;
		}
		
		private void setName(String name) {
			this.name = name;
		}
		
	}
	
	@Test
	public void test() throws Exception {
		reflectObject reflectObject = new reflectObject();
		ReflectionUtil.invoke(reflectObject, ReflectionUtil.getMethod(reflectObject.class, "setName", String.class), "REFLECT");
		Assert.assertEquals("REFLECT", ReflectionUtil.invoke(reflectObject, ReflectionUtil.getMethod(reflectObject.class, "getName")));
	}
}
