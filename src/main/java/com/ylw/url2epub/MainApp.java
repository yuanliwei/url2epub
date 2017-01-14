package com.ylw.url2epub;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ylw.url2epub.model.UrlContent;
import com.ylw.url2epub.net.Get;

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
		Get.getContent(url, contents, atomicInteger, () -> {
			generateEPUB(contents);
		});

	}

	private void generateEPUB(List<UrlContent> contents) {
		// content meta-data
		// mainfest
		// spine
		// guide
		// toc.ncx
		//  head title navMap
		

	}
}
