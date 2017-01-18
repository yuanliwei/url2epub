package com.ylw.url2epub.template;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import com.ylw.url2epub.model.ContentElement;
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

	List<ContentElement> contents;

	public TocNcxNavMap(List<ContentElement> contents) {
		super();
		this.contents = contents;
	}

	public String parse() {
		StringBuilder sb = new StringBuilder();
		// <navPoint id="{0}" playOrder="{1}">
		// <navLabel>
		// <text>{2}</text>
		// </navLabel>
		// <content src="{3}"/>
		// </navPoint>

		for (int i = 0; i < contents.size(); i++) {
			ContentElement contentElement = contents.get(i);
			if (contentElement.isHtml()) {
				sb.append(MessageFormat.format(templItem, contentElement.getId(), i, contentElement.getTitle(),
						contentElement.getId()));
			}
		}
		return MessageFormat.format(templ, sb.toString());
	}

}
