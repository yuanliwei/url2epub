package com.ylw.url2epub.template;

import com.ylw.url2epub.utils.FileUtil;

public class MimeType {
	String templ = "application/epub+zip";

	public void generate(String path) {
		String name = "/mimetype";
		FileUtil.saveFullPathFile(path + name, templ);
	}
}
