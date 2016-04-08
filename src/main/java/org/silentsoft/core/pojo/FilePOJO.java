package org.silentsoft.core.pojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.silentsoft.core.util.FileUtil;

public class FilePOJO {

	private File file;

	private byte[] bytes;

	private String tag;

	private boolean isDirectory;

	private String name;

	private String extension;

	private long length;

	private String size;

	public FilePOJO() {
	}

	public FilePOJO(String tag) {
		setTag(tag);
	}

	public FilePOJO(File file) {
		this(null, file);
	}

	public FilePOJO(String tag, File file) {
		this(tag);
		
		setFile(file);
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		
		if (getFile() != null) {
			setDirectory(getFile().isDirectory());
			setName(FileUtil.getName(getFile().getName()));
			setExtension(FileUtil.getExtension(getFile().getName()));
			setLength(getFile().length());
		}
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isDirectory() {
		return isDirectory;
	}

	private void setDirectory(boolean isDirectory) {
		this.isDirectory = isDirectory;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public String getNameWithExtension() {
		return getName().concat(".").concat(getExtension());
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
		setSize(String.valueOf(this.length).concat(" byte"));

		double sizeKB = ((double) this.length / 1024);
		if (sizeKB >= 1) {
			setSize(String.format("%.2f", sizeKB).concat(" KB"));

			double sizeMB = ((double) this.length / 1024 / 1024);
			if (sizeMB >= 1) {
				setSize(String.format("%.2f", sizeMB).concat(" MB"));

				double sizeGB = ((double) this.length / 1024 / 1024 / 1024);
				if (sizeGB >= 1) {
					setSize(String.format("%.2f", sizeGB).concat(" GB"));
				}
			}
		}
	}

	public String getSize() {
		return size;
	}

	private void setSize(String size) {
		this.size = size;
	}

	public boolean store(String destination) throws Exception {
		return store(new File(destination));
	}

	public boolean store(File destination) throws Exception {
		if (getBytes() == null) {
			throw new NullPointerException("Source bytes cannot be null !");
		}
		if (destination == null) {
			throw new NullPointerException("Destination cannot be null !");
		}

		File parentFile = destination.getParentFile();
		if (parentFile != null) {
			if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
				throw new IOException("Cannot create destination '".concat(parentFile.toString()).concat("' directory"));
			}
		}
		if (destination.exists() && destination.canWrite() == false) {
			throw new IOException("Destination '".concat(destination.toString()).concat("' exist. but it is read-only"));
		}

		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(destination);
			IOUtils.write(getBytes(), fileOutputStream);
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}

		return true;
	}
}