package com.ylw.url2epub.template;

import java.text.MessageFormat;

import com.ylw.url2epub.utils.FileUtil;

public class ContentOpfMetadata {
	String templ = FileUtil.getResString("templ/ContentOpfMetadata.xml");
	String articleid;
	String title;
	String cover; // 封面

	
	
	public ContentOpfMetadata(String articleid, String title, String cover) {
		super();
		this.articleid = articleid;
		this.title = title;
		this.cover = cover;
	}



	public String parse() {
		// TODO Auto-generated method stub
		return MessageFormat.format(templ, articleid, title, cover);
	}

}
