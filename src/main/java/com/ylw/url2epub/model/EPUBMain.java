package com.ylw.url2epub.model;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.tags.LinkTag;

import com.ylw.url2epub.net.CallBack;
import com.ylw.url2epub.net.Get;
import com.ylw.url2epub.template.ContainerXML;
import com.ylw.url2epub.template.ContentOpf;
import com.ylw.url2epub.template.MimeType;
import com.ylw.url2epub.template.TocNcx;
import com.ylw.url2epub.utils.FileUtil;
import com.ylw.url2epub.utils.ZipUtil;
import com.ylw.url2epub.utils.digest.MD5;

public class EPUBMain {
	private Get get;
	String articleid = UUID.randomUUID().toString();
	String title = "末日边缘";
	String author = "ylw";
	String epubPath = "C:\\Users\\ylw\\Desktop\\new epub\\末日边缘.epub";
	// String url = "http://www.guokr.com/article/441954/";
	String url = "http://book.zongheng.com/showchapter/405946.html";
	int deep = 2;

	public void start() {

		String rootDir = System.getProperty("java.io.tmpdir") + "epub-generate" + File.separator + Math.random()
				+ File.separator;

		AtomicInteger atomicInteger = new AtomicInteger(0);
		get = new Get(rootDir + "/OEBPS/");
		get.setCallBack(new CallBack() {

			@Override
			public void onResult(List<ContentElement> list) {
				generateEPUB(rootDir, epubPath, list);
			}

		});
		get.setFilter(new NodeFilter() {

			private static final long serialVersionUID = -6370991190059591944L;

			@Override
			public boolean accept(Node node) {
				LinkTag linkTag = (LinkTag) node;
				String link = linkTag.getLink();
				if (link.contains("chapter")) {
					return true;
				}
				return false;
			}
		});
		String id = MD5.md5(url) + ".html";
		get.getContent(id, url, deep, atomicInteger);
	}

	private void generateEPUB(String rootDir, String epubPath, List<ContentElement> contents) {

		String uid = articleid;
		String docTitle = title;

		ContentOpf contentOpf = new ContentOpf(articleid, title, author, contents);
		String contentOpfResult = contentOpf.build();
		FileUtil.saveFullPathFile(rootDir + "/OEBPS/content.opf", contentOpfResult);

		TocNcx tocNcx = new TocNcx(uid, docTitle, contents);
		String tocNcxResult = tocNcx.build();
		FileUtil.saveFullPathFile(rootDir + "/OEBPS/toc.ncx", tocNcxResult);
		new ContainerXML().generate(rootDir);
		new MimeType().generate(rootDir);

		ZipUtil.zip(rootDir, epubPath);

		FileUtil.deleteDir(rootDir);
	}

}
