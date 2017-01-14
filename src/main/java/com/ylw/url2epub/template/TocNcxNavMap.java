package com.ylw.url2epub.template;

import java.text.MessageFormat;
import java.util.List;

import com.ylw.url2epub.model.UrlContent;
import com.ylw.url2epub.utils.FileUtil;

/**
 * <pre>
 *   <navMap>
 *   ..............
 *   </navMap>
 * </pre>
 * 
 * @author ylw
 *
 */
public class TocNcxNavMap {
	String templ = FileUtil.getResString("templ/TocNcxNavMap.xml");
	String templItem = FileUtil.getResString("templ/TocNcxNavMapItem.xml");

	List<UrlContent> contents;

	public TocNcxNavMap(List<UrlContent> contents) {
		super();
		this.contents = contents;
	}

	public String parse() {
		StringBuilder sb = new StringBuilder();

		contents.forEach(action -> {
			sb.append(MessageFormat.format(templItem, action.toString()));
		});
		return MessageFormat.format(templ, sb.toString());
	}

}
