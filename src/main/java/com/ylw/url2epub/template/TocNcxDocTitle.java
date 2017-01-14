package com.ylw.url2epub.template;

import java.text.MessageFormat;

import com.ylw.url2epub.utils.FileUtil;

/**
 * <pre>
 *   <docTitle>
 *     <text>CvPcb</text>
 *   </docTitle>
 * </pre>
 * 
 * @author ylw
 *
 */
public class TocNcxDocTitle {
	String templ = FileUtil.getResString("templ/TocNcxDocTitle.xml");

	String title;

	public TocNcxDocTitle(String title) {
		super();
		this.title = title;
	}

	public String parse() {
		// TODO Auto-generated method stub
		return MessageFormat.format(templ, title);
	}

}
