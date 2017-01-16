package com.ylw.url2epub.template;

import java.text.MessageFormat;
import java.util.List;

import com.ylw.url2epub.model.UrlContent;
import com.ylw.url2epub.utils.FileUtil;

public class ContentOpfManifest {
	String templ = FileUtil.getResString("templ/ContentOpfManifest.xml");
	String templItem = FileUtil.getResString("templ/ContentOpfManifestItem.xml");
	List<UrlContent> contents;

	public ContentOpfManifest(List<UrlContent> contents) {
		super();
		this.contents = contents;
	}

	public String parse() {
		StringBuilder manifests = new StringBuilder();
		// TODO Auto-generated method stub

		contents.forEach(action -> {
			manifests.append(MessageFormat.format(templItem, action.getId(), action.getHref(), action.getMediaType()));
		});

		return MessageFormat.format(templ, manifests.toString());
	}

}
