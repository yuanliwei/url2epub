package com.ylw.url2epub.template;

import java.text.MessageFormat;

import com.ylw.url2epub.utils.FileUtil;

/**
 * <pre>
 * <head>
 *   <meta name="cover" content="cover"/>
 *   <meta name="dtb:uid" content="_idm140059198535344"/>
 * </head>
 * </pre>
 * 
 * @author ylw
 *
 */
public class TocNcxHead {
	String templ = FileUtil.getResString("templ/TocNcxHead.xml");
	String cover;
	String uid;

	public TocNcxHead(String cover, String uid) {
		super();
		this.cover = cover;
		this.uid = uid;
	}

	public String parse() {
		// TODO Auto-generated method stub
		return MessageFormat.format(templ, cover, uid);
	}

}
