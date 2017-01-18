package com.ylw.url2epub.template;

import com.ylw.url2epub.utils.FileUtil;

public class ContainerXML {
	static String templ = FileUtil.getResString("templ/container.xml");

	public void generate(String path) {
		String name = "/META-INF/container.xml";
		FileUtil.saveFullPathFile(path + name, templ);
	}
}
