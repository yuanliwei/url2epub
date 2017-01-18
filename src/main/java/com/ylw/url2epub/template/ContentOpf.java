package com.ylw.url2epub.template;

import java.text.MessageFormat;
import java.util.List;

import com.ylw.url2epub.model.ContentElement;

public class ContentOpf {
	private String articleid;
	private String title;
	private String author;
	private List<ContentElement> contents;

	public ContentOpf(String articleid, String title, String author, List<ContentElement> contents) {
		this.articleid = articleid;
		this.title = title;
		this.author = author;
		this.contents = contents;
	}

	public String build() {
		MessageFormatBuilder b = new MessageFormatBuilder();
		b.a("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
		b.a("\r\n");
		b.a("<package xmlns=\"http://www.idpf.org/2007/opf\" version=\"2.0\" unique-identifier=\"articleid\">\r\n");
		b.a("  <metadata>\r\n");
		b.a("    <dc:identifier xmlns:dc=\"http://purl.org/dc/elements/1.1/\" id=\"articleid\">{0}</dc:identifier>\r\n",
				articleid);
		b.a("    <dc:title xmlns:dc=\"http://purl.org/dc/elements/1.1/\">{0}</dc:title>\r\n", title);
		b.a("    <dc:creator xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:opf=\"http://www.idpf.org/2007/opf\" opf:file-as=\"ylw\">{0}</dc:creator>\r\n",
				author);
		b.a("    <dc:language xmlns:dc=\"http://purl.org/dc/elements/1.1/\">en</dc:language>\r\n");
		b.a("    <meta name=\"cover\" content=\"cover-image\"/>\r\n");
		b.a("  </metadata>\r\n");
		b.a("  <manifest>\r\n");
		b.a("    <item id=\"ncxtoc\" media-type=\"application/x-dtbncx+xml\" href=\"toc.ncx\"/>\r\n");
		b.a("    <item id=\"cover\" href=\"cover.html\" media-type=\"application/xhtml+xml\"/>\r\n");
		contents.forEach(action -> {
			b.a("    <item id=\"{0}\" href=\"{0}\" media-type=\"{1}\"/>\r\n", action.getId(), action.getMediaType());
		});
		b.a("  </manifest>\r\n");
		b.a("  <spine toc=\"ncxtoc\">\r\n");
		b.a("    <itemref idref=\"cover\" linear=\"no\"/>\r\n");
		// b.a(" <itemref idref=\"idm140059168785104\"/>\r\n");
		contents.forEach(action -> {
			if (action.isHtml()) {
				b.a("    <itemref idref=\"{0}\"/>\r\n", action.getId());
			}
		});
		b.a("  </spine>\r\n");
		b.a(" <guide>\r\n");
		b.a(" <reference href=\"cover.html\" type=\"cover\" title=\"Cover\"/>\r\n");
		b.a(" </guide>\r\n");
		b.a("</package>\r\n");
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
