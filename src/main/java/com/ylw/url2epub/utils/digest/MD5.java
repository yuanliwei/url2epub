package com.ylw.url2epub.utils.digest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MD5 {

	private static Log log = LogFactory.getLog(MD5.class);

	public static final long _NO_LIMIT_SIZE = 80 * 1024 * 1024; // 80M以内无分割计算
	public static final long _PER_256_LIMIT_SIZE = 256 * 1024 * 1024; // 256M以内按照256K间隔分割
	public static final long _PER_512_LIMIT_SIZE = 512 * 1024 * 1024; // 512M以内按照512K间隔分割

	// 三种不同limit的大小
	public static final long _256_LIMIT = 256 * 1024;
	public static final long _512_LIMIT = 512 * 1024;
	public static final long _1024_LIMIT = 1024 * 1024;

	public static String md5(String str, String charsetName) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
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

	public static String md5(String str) {
		return md5(str, "UTF-8");
	}

	/**
	 * 文件md5码
	 * 
	 * @author fenglei
	 */
	public static String MD5DigestFile(File file) {
		return MD5DigestFile(file, 0);
	}

	public static String MD5DigestFile(File file, long limit) {
		String result = "";
		FileInputStream fis = null;
		try {
			MessageDigest alga = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(file);
			byte[] buffer = new byte[(int) ((limit > 0) ? limit : 2048)];
			int length;
			while ((length = fis.read(buffer)) != -1) {
				alga.update(buffer, 0, length);
				if (limit > 0)
					fis.skip(limit);
			}
			byte[] digest = alga.digest();
			result = Hex.bytesToHexString(digest);
			alga.reset();
		} catch (FileNotFoundException e) {
			log.error("md5 file " + file.getAbsolutePath() + " failed:" + e.getMessage());
		} catch (IOException e) {
			log.error("md5 file " + file.getAbsolutePath() + " failed:" + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return result;
	}

	public static String MD5DigestFileToFileMgr(File file) {
		long fileSize = file.length();
		long limit = fileSize < _NO_LIMIT_SIZE ? 0
				: (fileSize < _PER_256_LIMIT_SIZE ? _256_LIMIT
						: (fileSize < _PER_512_LIMIT_SIZE ? _512_LIMIT : _1024_LIMIT));
		return MD5DigestFile(file, limit);
	}

}
