package mobi.jzcx.android.chongmi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 */
public class MD5 {
	/**
	 * 加密为MD5
	 * 
	 * @param source
	 * @return
	 */
	public static String encoderForString(String source) {

		byte[] md5Bytes = encoderForBytes(source);
		if (md5Bytes != null) {
			StringBuilder hexValue = new StringBuilder();
			for (byte md5Byte : md5Bytes) {
				int val = (md5Byte) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			return hexValue.toString().toUpperCase();
		}
		return null;
	}

	/**
	 * 加密为MD5
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] encoderForBytes(String source) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			char[] charArray = source.toCharArray();
			byte[] byteArray = new byte[charArray.length];
			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];
			return digest.digest(byteArray);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String md5(String key) {

		if (null == key) {
			return null;
		}

		try {
			char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			byte[] buf = key.getBytes();
			md.update(buf, 0, buf.length);
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder(32);
			for (byte b : bytes) {
				sb.append(hex[((b >> 4) & 0xF)]).append(hex[((b >> 0) & 0xF)]);
			}
			key = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return key;
	}
}