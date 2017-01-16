package com.ylw.url2epub.net;

import java.io.InputStream;

public interface GETCallBack {
	void onResult(int code, InputStream in, String type);
}
