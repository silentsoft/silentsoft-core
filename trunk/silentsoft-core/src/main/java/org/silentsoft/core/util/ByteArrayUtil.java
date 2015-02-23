package org.silentsoft.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteArrayUtil {

	public static byte[] toByteArray(Object target) throws Exception {
		byte[] bytes = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(target);
		oos.flush();
		oos.close();
		bos.close();
		bytes = bos.toByteArray();

		return bytes;
	}

	public static Object toObject(byte[] target) throws Exception {
		Object obj = null;

		ByteArrayInputStream bis = new ByteArrayInputStream(target);
		ObjectInputStream ois = new ObjectInputStream(bis);
		obj = ois.readObject();

		return obj;
	}
}
