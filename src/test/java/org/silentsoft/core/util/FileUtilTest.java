package org.silentsoft.core.util;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilTest {

//	@Test
	public void cleanTest() {
		long currentTimeMillis = System.currentTimeMillis();
		FileUtil.clean(new File("K:\\test"), file -> {
			if (TimeUnit.DAYS.convert(currentTimeMillis - file.lastModified(), TimeUnit.MILLISECONDS) > 7) {
				return true;
			}
					
			return false;
		});
	}
	
//	@Test
	public void isValidPathTest() {
		if (SystemUtil.isWindows()) {
			Assert.assertTrue(FileUtil.isValidPath("C:\\test"));
			Assert.assertTrue(FileUtil.isValidPath("C:\\test.txt"));
		}
		Assert.assertTrue(FileUtil.isValidPath("/directory"));
		Assert.assertTrue(FileUtil.isValidPath("../directory"));
		Assert.assertTrue(FileUtil.isValidPath("/file.txt"));
		Assert.assertTrue(FileUtil.isValidPath("../file.txt"));
		
		if (SystemUtil.isWindows()) {
			Assert.assertFalse(FileUtil.isValidPath("C:\\te*st"));
			Assert.assertFalse(FileUtil.isValidPath("C:\\te:st.txt"));
		}
		Assert.assertFalse(FileUtil.isValidPath("*/directory"));
		Assert.assertFalse(FileUtil.isValidPath("?../directory"));
		Assert.assertFalse(FileUtil.isValidPath("/file.t|xt"));
		Assert.assertFalse(FileUtil.isValidPath(">../file.txt"));
	}
	
//	@Test
	public void emptyCreateTest() throws Exception {
		StringBuffer buffer = new StringBuffer();
		FileUtil.saveFile(Paths.get(System.getProperty("user.dir"), "plugins", "priority.ini"), buffer.toString());
	}
}
