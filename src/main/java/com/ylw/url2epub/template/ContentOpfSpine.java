package com.ylw.url2epub.template;

import java.text.MessageFormat;
import java.util.List;

import com.ylw.url2epub.model.UrlContent;
import com.ylw.url2epub.utils.FileUtil;

public class ContentOpfSpine {
	String templ = FileUtil.getResString("templ/ContentOpfSpine.xml");
	// <itemref idref="cover" linear="no"/>
	String templItem = "<itemref idref=\"{0}\"/>";

	List<UrlContent> contents;

	public ContentOpfSpine(List<UrlContent> contents) {
		super();
		this.contents = contents;
	}

	public String parse() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		contents.forEach(action -> {
			sb.append(MessageFormat.format(templItem, action.toString()));
		});
		return MessageFormat.format(templ, sb.toString());
	}

}
