package com.ylw.url2epub.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.ylw.url2epub.MainApp;
import com.ylw.url2epub.model.UrlContent;
import com.ylw.url2epub.net.CallBack;
import com.ylw.url2epub.net.Ele;
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
import com.ylw.url2epub.utils.digest.MD5;

import javafx.application.Platform;
import netscape.javascript.JSObject;

public class JSInterface {
	private static Log log = LogFactory.getLog(JSInterface.class);
	private MainApp mainApp;

	public JSInterface(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	// public static void main(String[] args) {
	// int deep = 1;
	// String url = "http://www.guokr.com/article/441954/";
	// new MainApp().start(url, deep);
	// }

	private Get get;

	public void start(String url, int deep) {
		// TODO Auto-generated method stub
		List<UrlContent> contents = new ArrayList<>();
		AtomicInteger atomicInteger = new AtomicInteger(0);
		String rootDir = "C:\\Users\\ylw\\Desktop\\new epub\\OEBPS\\";
		get = new Get(rootDir);
		get.setCallBack(new CallBack() {

			@Override
			public void onResult(List<Ele> list) {
				// TODO Auto-generated method stub
				for (int i = 0; i < list.size(); i++) {
					Ele e = list.get(i);
					contents.add(new UrlContent(e.getTitle(), e.getTitle(), e.getMediaType()));
				}
				generateEPUB(rootDir, contents);
			}
		});
		get.getContent(url, url, deep, atomicInteger);

	}

	public void testA() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				JSObject jsObject = (JSObject) mainApp.mainViewController.webEngine.executeScript("newJsObject()");
				JSObject window = (JSObject) mainApp.mainViewController.webEngine.executeScript("window");
				jsObject.setMember("prefix", "pppp");
				jsObject.setMember("body", "bbbbb");
				jsObject.setMember("deep", "dddddd");
				window.call("parseLinks", jsObject);
			}
		});
	}

	private void generateEPUB(String rootDir, List<UrlContent> contents) {
		// content meta-data
		// mainfest
		// spine
		// guide
		// toc.ncx
		// head title navMap
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

		FileUtil.saveFullPathFile(rootDir + "content.opf", contentOpfResult);

		String uid = null;
		TocNcxHead head = new TocNcxHead(cover, uid);
		TocNcxDocTitle docTitle = new TocNcxDocTitle(title);
		TocNcxNavMap navMap = new TocNcxNavMap(contents);
		TocNcx tocNcx = new TocNcx(head, docTitle, navMap);
		String tocNcxResult = tocNcx.parse();
		// FileUtil.saveFullPathFile(rootDir + "toc.ncx", tocNcxResult);
		// Platform.runLater(new Runnable() {
		// @Override
		// public void run() {
		// mainApp.mainViewController.exec("alert('success')");
		// }
		// });
	}

	public void POST(JSObject params) throws Exception {
		URL url = new URL((String) params.getMember("url"));
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		// 发送请求参数
		out.print(params.getMember("params"));
		// flush输出流的缓冲
		out.flush();
		// 获取所有响应头字段
		Map<String, List<String>> map = connection.getHeaderFields();
		// 遍历所有的响应头字段
		JSONObject headers = new JSONObject();
		for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
			headers.put(key, map.get(key));
		}
		// 定义BufferedReader输入流来读取URL的响应
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream in = connection.getInputStream();
		byte[] buffer = new byte[4096];
		int length = 0;
		while ((length = in.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		params.call("onSuccess", headers.toJSONString(), new String(outputStream.toByteArray()));
		// params.call("onError",
		// "2CCCCCCCCCCCCCCCCCCCCCCCCGggggggggggggggggg");

	}

	public void setMainApp(MainApp mainApp2) {
		this.mainApp = mainApp2;
	}

	public void testB() {
		List<UrlContent> contents = new ArrayList<>();
		AtomicInteger atomicInteger = new AtomicInteger(0);
		String rootDir = "C:\\Users\\ylw\\Desktop\\new epub\\OEBPS\\";
		get = new Get(rootDir);
		get.setCallBack(new CallBack() {

			@Override
			public void onResult(List<Ele> list) {
				// TODO Auto-generated method stub
				for (int i = 0; i < list.size(); i++) {
					Ele e = list.get(i);
					contents.add(new UrlContent(e.getId(), e.getId(), e.getMediaType()));
				}
				generateEPUB(rootDir, contents);
			}

		});
//		String url = "http://www.guokr.com/article/441954/";
		String url = "http://blog.csdn.net/";
		int deep = 1;
		String id = MD5.md5(url) + ".html";
		get.getContent(id, url, deep, atomicInteger);
	}
}
