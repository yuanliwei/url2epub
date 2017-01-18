package com.ylw.url2epub.template;

import com.ylw.url2epub.utils.FileUtil;

public class ContainerXML {
	public String build() {
		StringBuilder b = new StringBuilder();
		b.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
		b.append("\r\n");
		b.append("<container xmlns=\"urn:oasis:names:tc:opendocument:xmlns:container\" version=\"1.0\">\r\n");
		b.append("  <rootfiles>\r\n");
		b.append("    <rootfile full-path=\"OEBPS/content.opf\" media-type=\"application/oebps-package+xml\"/>\r\n");
		b.append("  </rootfiles>\r\n");
		b.append("</container>\r\n");
		b.append("\r\n");
		return b.toString();
	}

	public void generate(String path) {
		String name = "/META-INF/container.xml";
		FileUtil.saveFullPathFile(path + name, build());
	}
}
