package com.ylw.url2epub.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropUtils {

	private static Log log = LogFactory.getLog(FileUtil.class);

	private static String propName = "properties.xml";
	private final static Properties properties = new Properties();

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static void put(String key, String value) {
		properties.put(key, value);
	}

	public static int getInt(String key) {
		return getInt(key, 0);
	}

	public static int getInt(String key, int defValue) {
		String value = properties.getProperty(key);
		int result = defValue;
		try {
			result = Integer.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public static void putInt(String key, int value) {
		properties.put(key, String.valueOf(value));
	}

	public static float getFloat(String key) {
		return getFloat(key, 0);
	}

	public static float getFloat(String key, float defValue) {
		String value = properties.getProperty(key);
		float result = defValue;
		try {
			result = Float.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(e.getMessage(), e);
		}
		return result;
	}

	public static void putFloat(String key, float value) {
		properties.put(key, String.valueOf(value));
	}

	public static void load() {
		InputStream inStream;
		try {
			String curPath = FileUtil.getCurPath();
			String fullPath = curPath + propName;
			if (FileUtil.isExistFile(fullPath)) {
				inStream = new FileInputStream(fullPath);
			} else {
				inStream = FileUtil.getResInputSteam(propName);
			}
			properties.loadFromXML(inStream);
			store();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public static void store() {
		OutputStream out;
		try {
			String curPath = FileUtil.getCurPath();
			String fullPath = curPath + propName;
			if (!FileUtil.isExistFile(fullPath)) {
				FileUtil.mkdirParentDir(fullPath);
				new File(fullPath).createNewFile();
			}
			out = new FileOutputStream(fullPath);
			String comments = "程序的配置文件!";
			properties.storeToXML(out, comments, "UTF-8");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}
}
