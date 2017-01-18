package com.ylw.url2epub.template;

import java.text.MessageFormat;
import java.util.List;

import com.ylw.url2epub.model.ContentElement;

public class TocNcx {

	private String uid = "_idm140059198535344";
	private String docTitle = "CvPcb";
	private List<ContentElement> contents;

	public TocNcx(String uid, String docTitle, List<ContentElement> contents) {
		super();
		this.uid = uid;
		this.docTitle = docTitle;
		this.contents = contents;
	}

	public String build() {
		MessageFormatBuilder b = new MessageFormatBuilder();
		b.a("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
		b.a("\r\n");
		b.a("<ncx xmlns=\"http://www.daisy.org/z3986/2005/ncx/\" version=\"2005-1\">\r\n");
		b.a("  <head>\r\n");
		b.a("    <meta name=\"cover\" content=\"cover\"/>\r\n");
		b.a("    <meta name=\"dtb:uid\" content=\"{0}\"/>\r\n", uid);
		b.a("  </head>\r\n");
		b.a("  <docTitle>\r\n");
		b.a("    <text>{0}</text>\r\n", docTitle);
		b.a("  </docTitle>\r\n");
		b.a("  <navMap>\r\n");

		int index = 0;
		for (int i = 0; i < contents.size(); i++) {
			ContentElement c = contents.get(i);
			if (c.isHtml()) {
				b.a("    <navPoint id=\"{0}\" playOrder=\"{1}\">\r\n", c.getId(), index++);
				b.a("      <navLabel>\r\n");
				b.a("        <text>{0}</text>\r\n", c.getTitle());
				b.a("      </navLabel>\r\n");
				b.a("      <content src=\"{0}\"/>\r\n", c.getId());
				b.a("    </navPoint>\r\n");
			}
		}
		b.a("  </navMap>\r\n");
		b.a("</ncx>\r\n");
		b.a("\r\n");
		return b.build();
	}

	class MessageFormatBuilder {

		StringBuilder sb;

		public MessageFormatBuilder() {
			super();
			this.sb = new StringBuilder();
		}

		public void a(String message, Object... params) {
			sb.append(MessageFormat.format(message, params));
		}

		public String build() {
			return sb.toString();
		}

	}
}
