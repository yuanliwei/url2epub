package com.ylw.url2epub.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpConnection;
import org.junit.runner.Request;

import com.ylw.url2epub.model.UrlContent;

public class Get {

	public void getContent(String url, List<UrlContent> contents, int deep, AtomicInteger atomicInteger,
			CallBack callback) {
		// TODO Auto-generated method stub
		if (deep == 0)
			return;
		get(url, (int code, String body) -> {

		});
	}

	void get(String url, HTTPGETCallBack callBack) {
		try {
			URL url2 = new URL(url);
			HttpConnection connection = (HttpConnection) url2.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			callBack.onResult(-1, e.getMessage());
		}
	}
}
