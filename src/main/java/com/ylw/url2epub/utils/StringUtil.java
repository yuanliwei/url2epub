package com.ylw.url2epub.utils;

import org.apache.http.util.TextUtils;

public class StringUtil {

	public static boolean isEmpty(String string) {
		return TextUtils.isEmpty(string);
	}

	public static String ascii2native(String sAscii) {
		if (TextUtils.isEmpty(sAscii))
			return "";
		StringBuilder sb = new StringBuilder();
		String[] words = sAscii.split("\\\\u");
		sb.append(words[0]);
		for (int i = 1; i < words.length; i++) {
			String word = words[i];
			sb.append((char) Integer.parseInt(word.substring(0, 4), 16));
			if (word.length() > 4) {
				sb.append(word.substring(4));
			}
		}
		return sb.toString();
	}

	public static String native2ascii(String sNative) {
		if (TextUtils.isEmpty(sNative))
			return "";
		char[] chars = sNative.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : chars) {
			if (c > 127) {
				sb.append("\\u");
				sb.append(Integer.toHexString(c));
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String htmlDecode(String sAscii) {
		if (TextUtils.isEmpty(sAscii))
			return "";
		return "";
//		deco
//		return sb.toString();
	}

}
