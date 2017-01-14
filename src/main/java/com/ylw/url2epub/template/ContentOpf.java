package com.ylw.url2epub.template;

import java.text.MessageFormat;

import com.ylw.url2epub.utils.FileUtil;

/**
 * <pre>
 * <?xml version="1.0" encoding="utf-8"?>
	
	<package xmlns="http://www.idpf.org/2007/opf" version="2.0" unique-identifier="articleid">
	......................
	.......................
	.........................
	</package>
 * </pre>
 * 
 * @author ylw
 *
 */
public class ContentOpf {
	String templ = FileUtil.getResString("templ/ContentOpf.xml");
	ContentOpfMetadata metadata;
	ContentOpfManifest manifest;
	ContentOpfSpine spine;
	ContentOpfGuide guide;

	public ContentOpf(ContentOpfMetadata metadata, ContentOpfManifest manifest, ContentOpfSpine spine,
			ContentOpfGuide guide) {
		super();
		this.metadata = metadata;
		this.manifest = manifest;
		this.spine = spine;
		this.guide = guide;
	}

	public String parse() {
		return MessageFormat.format(templ, metadata.parse(), manifest.parse(), spine.parse(), guide.parse());
	}
}
