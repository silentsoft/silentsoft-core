package org.silentsoft.core.util;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class FileUtilTest {

	@Test
	public void cleanTest() {
		long currentTimeMillis = System.currentTimeMillis();
		FileUtil.clean(new File("K:\\test"), file -> {
			if (TimeUnit.DAYS.convert(currentTimeMillis - file.lastModified(), TimeUnit.MILLISECONDS) > 7) {
				return true;
			}
					
			return false;
		});
	}
}
