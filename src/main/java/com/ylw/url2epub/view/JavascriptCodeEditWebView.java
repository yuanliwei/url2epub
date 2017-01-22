package com.ylw.url2epub.view;

import com.ylw.url2epub.controller.JSInterface;
import com.ylw.url2epub.utils.FileUtil;

import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class JavascriptCodeEditWebView extends StackPane {
	WebView webView;
	private JSInterface jsObj;
	private WebEngine engine;

	public JavascriptCodeEditWebView() {
		super();
		init();
	}

	public JavascriptCodeEditWebView(Node... children) {
		super(children);
		init();
	}

	private void init() {
		webView = new WebView();
		getChildren().add(webView);
		engine = webView.getEngine();
		engine.setJavaScriptEnabled(true);
		jsObj = new JSInterface();
//		webView.setContextMenuEnabled(false);
		webView.setOnContextMenuRequested(value->{
			System.out.println(value.getSource());
			System.out.println(value.getTarget());
			System.out.println(value.getEventType().getName());
		});
		 
		engine.setOnError(event -> {
			System.out.println(event.getMessage());
		});

		engine.setOnAlert((event) -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.titleProperty().set("来自网页的信息");
			alert.headerTextProperty().set("来自网页的信息");
			alert.contentTextProperty().set(event.getData());
			alert.showAndWait();
		});

		engine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			JSObject window = (JSObject) engine.executeScript("window");
			System.out.println("sssssssss  - " + newState + "   " + window.getMember("jsObj"));
			window.setMember("jsObj", jsObj);
			if (newState == State.SUCCEEDED) {
				// exec("onPageLoaded()");
				setValue(build().replaceAll("\\r\\n", "\\\\n"));
			}
		});
		engine.setOnError(new EventHandler<WebErrorEvent>() {

			@Override
			public void handle(WebErrorEvent event) {
				System.out.println(event.getMessage() + "  - " + event.getSource());
				System.err.println(event.getException());
			}
		});
		com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(new com.sun.javafx.webkit.WebConsoleListener() {

			@Override
			public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
				System.out.println("Console: " + message + " [" + sourceId + ":" + lineNumber + "] ");
			}
		});
		engine.load(FileUtil.getResUrl("codemirror-5.23.0/code-edit.html").toExternalForm());

	}

	public Object exec(String script) {
		return engine.executeScript(script);
	}

	public void setValue(String code) {
		System.out.println("editor.setValue('" + code.replaceAll("'", "\\\\'") + "')");
		exec("editor.setValue('" + code.replaceAll("'", "\\\\'") + "')");
	}

	public String getValue() {
		return (String) exec("editor.getValue()");
	}

	public String build() {
		StringBuilder b = new StringBuilder();
		b.append("function test(url){\r\n");
		b.append("\r\n");
		b.append("  var regex = /article/;\r\n");
		b.append("\r\n");
		b.append("  if(regex.test(url)){\r\n");
		b.append("\r\n");
		b.append("    return true;\r\n");
		b.append("\r\n");
		b.append("  } else {\r\n");
		b.append("\r\n");
		b.append("    return false;\r\n");
		b.append("\r\n");
		b.append("  }\r\n");
		b.append("\r\n");
		b.append("}\r\n");
		return b.toString();
	}
}
