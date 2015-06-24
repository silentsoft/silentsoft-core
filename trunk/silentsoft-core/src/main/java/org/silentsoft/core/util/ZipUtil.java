package org.silentsoft.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.silentsoft.core.CommonConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ZipUtil {
	private static Logger LOGGER = LoggerFactory.getLogger(ZipUtil.class);

	private static final ZipUtil instance = new ZipUtil();
	
	public static final int BUFFER_SIZE = 1024 * 5;
	public static final int DEFAULT_COMPRESSION_LEVEL = 8;

	private String sourcePath;
	private List<String> fileList;

	private static ZipUtil getInstance() {
		return instance;
	}

	public static boolean doZip(String sourcePath, String zipFile) {
		return doZip(sourcePath, zipFile, DEFAULT_COMPRESSION_LEVEL);
	}

	public static boolean doZip(String sourcePath, String zipFile, int compressionLevel) {
		boolean bResult = true;
		
		getInstance().sourcePath = sourcePath;
		getInstance().fileList = new ArrayList<String>();

		generateFileList(new File(sourcePath));

		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			LOGGER.info("Start make zip : <{}>", new Object[] { zipFile });
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
			zos.setLevel(compressionLevel);

			for (String file : getInstance().fileList) {
				zos.putNextEntry(new ZipEntry(file));
				
				// Do not use .isFile or .isDirectory method. Must validate by "/" character.
				if (!file.endsWith(CommonConst.SEPARATOR)) {
					FileInputStream fis = new FileInputStream(sourcePath + File.separator + file);
	
					int len;
					while ((len = fis.read(buffer)) != -1) {
						zos.write(buffer, 0, len);
					}
	
					fis.close();
				}
			}

			zos.closeEntry();
			zos.close();

			LOGGER.info("Complete make zip : <{}>", new Object[] { zipFile });
		} catch (Exception e) {
			bResult = false;
			LOGGER.error("I got catch an exception !", new Object[]{e});
		}
		
		return bResult;
	}

	private static void generateFileList(File node) {
		if (node.isFile()) {
			getInstance().fileList.add(generateZipEntry(node.getAbsolutePath()));
		}

		if (node.isDirectory()) {
			String[] subNode = node.list();
			if (subNode.length == 0) {
				// Do not use File.separator if directory.
				getInstance().fileList.add(generateZipEntry(node.getAbsolutePath() + CommonConst.SEPARATOR));
			} else {
				for (String fileName : subNode) {
					generateFileList(new File(node, fileName));
				}
			}
		}
	}

	private static String generateZipEntry(String file) {
		return file.substring(getInstance().sourcePath.length() + 1, file.length());
	}

	public static boolean unZip(String zipFile, String targetPath) {
		boolean bResult = true;
		
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			LOGGER.info("Start unZip : <{}>", new Object[] { zipFile });

			ZipEntry ze = null;
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));

			File targetFile = null;
			while ((ze = zis.getNextEntry()) != null) {
				targetFile = new File(targetPath + File.separator + ze.getName());

				if (ze.isDirectory()) {
					targetFile.mkdirs();
				} else {
					targetFile.getParentFile().mkdirs();

					FileOutputStream fos = new FileOutputStream(targetFile);

					int len = 0;
					while ((len = zis.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}

					fos.flush();
					fos.close();
				}
			}

			zis.closeEntry();
			zis.close();

			LOGGER.info("Complete unZip : <{}>", new Object[] { zipFile });
		} catch (Exception e) {
			bResult = false;
			LOGGER.error("I got catch an exception !", new Object[]{e});
		}
		
		return bResult;
	}
}
