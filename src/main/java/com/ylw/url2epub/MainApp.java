package com.ylw.url2epub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.sqlite.core.CoreConnection;

import com.ylw.url2epub.model.UrlContent;
import com.ylw.url2epub.net.Get;
import com.ylw.url2epub.template.ContentOpf;
import com.ylw.url2epub.template.ContentOpfGuide;
import com.ylw.url2epub.template.ContentOpfManifest;
import com.ylw.url2epub.template.ContentOpfMetadata;
import com.ylw.url2epub.template.ContentOpfSpine;
import com.ylw.url2epub.template.TocNcx;
import com.ylw.url2epub.template.TocNcxDocTitle;
import com.ylw.url2epub.template.TocNcxHead;
import com.ylw.url2epub.template.TocNcxNavMap;
import com.ylw.url2epub.utils.FileUtil;

public class MainApp {

	public static void main(String[] args) {
		int deep = 3;
		String url = "";
		new MainApp().start(url, deep);
	}

	private void start(String url, int deep) {
		// TODO Auto-generated method stub
		List<UrlContent> contents = new ArrayList<>();
		AtomicInteger atomicInteger = new AtomicInteger(1);
		new Get().getContent(url, contents, deep, atomicInteger, () -> {
			generateEPUB(contents);
		});

	}

	private void generateEPUB(List<UrlContent> contents) {
		String rootDir = "C:\\Users\\y\\Desktop\\newEbook\\";
		// content meta-data
		// mainfest
		// spine
		// guide
		// toc.ncx
		// head title navMap
		String articleid = null;
		String title = null;
		String cover = null;
		ContentOpfMetadata metadata = new ContentOpfMetadata(articleid, title, cover);
		ContentOpfManifest manifest = new ContentOpfManifest(contents);
		ContentOpfSpine spine = new ContentOpfSpine(contents);
		String href = null;
		ContentOpfGuide guide = new ContentOpfGuide(href, title);
		ContentOpf contentOpf = new ContentOpf(metadata, manifest, spine, guide);
		String contentOpfResult = contentOpf.parse();

		FileUtil.saveFullPathFile(rootDir + "content.opt", contentOpfResult);

		String uid = null;
		TocNcxHead head = new TocNcxHead(cover, uid);
		TocNcxDocTitle docTitle = new TocNcxDocTitle(title);
		TocNcxNavMap navMap = new TocNcxNavMap(contents);
		TocNcx tocNcx = new TocNcx(head, docTitle, navMap);
		String tocNcxResult = tocNcx.parse();
		FileUtil.saveFullPathFile(rootDir + "toc.ncx", tocNcxResult);

	}
}
