package com.ylw.url2epub.template;

import java.text.MessageFormat;

import com.ylw.url2epub.utils.FileUtil;

public class ContentOpfGuide {
	String templ = FileUtil.getResString("templ/ContentOpfGuide.xml");

	String href;
	String title;

	public ContentOpfGuide(String href, String title) {
		super();
		this.href = href;
		this.title = title;
	}

	public String parse() {
		return MessageFormat.format(templ, href, title);
	}

}
