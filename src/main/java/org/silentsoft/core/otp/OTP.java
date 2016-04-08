package org.silentsoft.core.otp;

import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class OTP {

	private static final String ALGORITHM = "HmacSHA1";
	
	/**
	 * Returns created OTP code by given <code>secretKey</code> and <code>distance</code>.
	 * Please make facade API to access this method without <code>secretKey</code> and <code>distance</code> parameters just like <code>OTP.create();</code>
	 * 
	 * @param secretKey the key material of the secret key. The contents of the array are copied to protect against subsequent modification. 
	 * @param distance the distance of otp interval by millisecond.
	 * @return
	 * @throws Exception
	 * @author silentsoft
	 */
	public static String create(byte[] secretKey, long distance) throws Exception {
		byte[] data = new byte[8];
		
		long value = new Date().getTime() / distance;
		for (int i=8; i-- > 0; value >>>= 8) {
			data[i] = (byte) value;
		}
		
		Mac mac = Mac.getInstance(ALGORITHM);
		mac.init(new SecretKeySpec(secretKey, ALGORITHM));
		
		byte[] hash = mac.doFinal(data);
		int offset = hash[20-1] & 0xF;
		
		long truncatedHash = 0;
		for (int i=0; i<4; ++i) {
			truncatedHash <<= 8;
			truncatedHash |= hash[offset+i] & 0xFF;
		}
		
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		
		return String.format("%06d", truncatedHash);
	}
	
	/**
	 * Returns the <code>code</code> is equals created OTP by distance.
	 * Please make facade API to access this method without <code>secretKey</code> and <code>distance</code> parameters just like <code>OTP.vertify(code);</code>
	 * 
	 * @param secretKey the key material of the secret key. The contents of the array are copied to protect against subsequent modification. 
	 * @param distance the distance of otp interval by millisecond.
	 * @param code the code to vertify OTP
	 * @return <tt>true</tt> if the given code is equals created OTP by distance
	 * @throws Exception
	 */
	public static boolean vertify(byte[] secretKey, long distance, String code) throws Exception {
		return create(secretKey, distance).equals(code);
	}
	
}
