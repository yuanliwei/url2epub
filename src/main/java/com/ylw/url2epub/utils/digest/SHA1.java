package com.ylw.url2epub.utils.digest;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SHA1 {
	private static Log log = LogFactory.getLog(MD5.class);

	public static String sha1(String str, String charsetName) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			final byte data[] = str.getBytes(charsetName);
			md.update(data, 0, data.length);
			final byte[] sha1hash = md.digest();
			return Hex.bytesToHexString(sha1hash);
		} catch (UnsupportedEncodingException e) {
			log.error(e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e);
		}
		return null;
	}

	public static String sha1(String str) {
		return sha1(str, "UTF-8");
	}

}
