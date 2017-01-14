package com.ylw.url2epub.template;

import java.text.MessageFormat;

import com.ylw.url2epub.utils.FileUtil;

/**
 * <pre>
	<?xml version="1.0" encoding="utf-8"?>
	
	<ncx xmlns="http://www.daisy.org/z3986/2005/ncx/" version="2005-1">
	....................
	.....................
	...................
	</ncx>
 * </pre>
 * 
 * @author ylw
 *
 */
public class TocNcx {
	String templ = FileUtil.getResString("templ/TocNcx.xml");
	TocNcxHead head;
	TocNcxDocTitle docTitle;
	TocNcxNavMap navMap;

	public TocNcx(TocNcxHead head, TocNcxDocTitle docTitle, TocNcxNavMap navMap) {
		super();
		this.head = head;
		this.docTitle = docTitle;
		this.navMap = navMap;
	}

	public String parse() {
		return MessageFormat.format(templ, head.parse(), docTitle.parse(), navMap.parse());
	}
}
