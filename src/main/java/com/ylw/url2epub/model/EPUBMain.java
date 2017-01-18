package com.ylw.url2epub.model;

import java.util.ArrayList;
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
import com.ylw.url2epub.template.ContentOpfGuide;
import com.ylw.url2epub.template.ContentOpfManifest;
import com.ylw.url2epub.template.ContentOpfMetadata;
import com.ylw.url2epub.template.ContentOpfSpine;
import com.ylw.url2epub.template.MimeType;
import com.ylw.url2epub.template.TocNcx;
import com.ylw.url2epub.template.TocNcxDocTitle;
import com.ylw.url2epub.template.TocNcxHead;
import com.ylw.url2epub.template.TocNcxNavMap;
import com.ylw.url2epub.utils.FileUtil;
import com.ylw.url2epub.utils.ZipUtil;
import com.ylw.url2epub.utils.digest.MD5;

public class EPUBMain {
	private Get get;

	public void start() {
		String epubPath = "C:\\Users\\ylw\\Desktop\\new epub\\vbvb.epub";
		String rootDir = "C:\\Users\\ylw\\Desktop\\new epub\\vbvb\\";
		// String url = "http://www.guokr.com/article/441954/";
		String url = "http://blog.csdn.net/";
		int deep = 0;

		// String folder=System.getProperty("java.io.tmpdir");

		List<ContentElement> contents = new ArrayList<>();
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
				if (link.contains("article/details")) {
					return true;
				}
				return false;
			}
		});
		String id = MD5.md5(url) + ".html";
		get.getContent(id, url, deep, atomicInteger);
	}

	private void generateEPUB(String rootDir, String epubPath, List<ContentElement> contents) {
		String articleid = UUID.randomUUID().toString();
		String title = "EBOOK";
		String cover = "cover.html";
		ContentOpfMetadata metadata = new ContentOpfMetadata(articleid, title, cover);
		ContentOpfManifest manifest = new ContentOpfManifest(contents);
		ContentOpfSpine spine = new ContentOpfSpine(contents);
		String href = cover;
		ContentOpfGuide guide = new ContentOpfGuide(href, title);
		ContentOpf contentOpf = new ContentOpf(metadata, manifest, spine, guide);
		String contentOpfResult = contentOpf.parse();

		FileUtil.saveFullPathFile(rootDir + "/OEBPS/content.opf", contentOpfResult);

		String uid = null;
		TocNcxHead head = new TocNcxHead(cover, uid);
		TocNcxDocTitle docTitle = new TocNcxDocTitle(title);
		TocNcxNavMap navMap = new TocNcxNavMap(contents);
		TocNcx tocNcx = new TocNcx(head, docTitle, navMap);
		String tocNcxResult = tocNcx.parse();
		FileUtil.saveFullPathFile(rootDir + "/OEBPS/toc.ncx", tocNcxResult);
		new ContainerXML().generate(rootDir);
		new MimeType().generate(rootDir);

		ZipUtil.zip(rootDir, epubPath);

	}

}
