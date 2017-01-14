package com.ylw.url2epub.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.util.IOUtils;
import com.ylw.url2epub.MainApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class Res {
	private static Log log = LogFactory.getLog(Res.class);

	public static Image getImageFromRes(String resName) {
		Image image = new Image(getInputStream("images/" + resName));
		return image;
	}

	public static String getJsSrc(String jsName) {
		return getString("js/" + jsName);
	}

	public static String getString(String fileName) {
		InputStream inStream = null;
		try {
			inStream = getInputStream(fileName);
			final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			final byte[] tmp = new byte[4096];
			int l;
			while ((l = inStream.read(tmp)) != -1) {
				buffer.write(tmp, 0, l);
			}
			return new String(buffer.toByteArray(), "utf-8");
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			IOUtils.close(inStream);
		}
		return fileName;
	}

	public static InputStream getInputStream(String fileName) {
		try {
			return MainApp.class.getResourceAsStream("../../../" + fileName);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static <T> T loadFXML(String layout) {
		try {
			if (!layout.endsWith(".fxml")) {
				layout += ".fxml";
			}
			return FXMLLoader.load(MainApp.class.getResource("view/" + layout));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FXMLLoader getFXMLLoader(String layout) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/" + layout));
		return loader;
	}

}
