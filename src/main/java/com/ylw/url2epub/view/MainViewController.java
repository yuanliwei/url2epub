package com.ylw.url2epub.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.url2epub.MainApp;
import com.ylw.url2epub.controller.BaseController;
import com.ylw.url2epub.controller.JSInterface;

import javafx.concurrent.Worker.State;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class MainViewController extends BaseController {
	private static Log log = LogFactory.getLog(MainViewController.class);

	@FXML
	WebView webView;
	private MainApp mainApp;
	public WebEngine webEngine;
	private JSInterface jsObj;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	protected void initialize() {
		// Initialize the person table with the two columns.
		webEngine = webView.getEngine();
		jsObj = new JSInterface(mainApp);

		webEngine.setOnError(event -> {
			System.out.println(event.getMessage());
		});

		webEngine.setOnAlert((event) -> {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.titleProperty().set("来自网页的信息");
			alert.headerTextProperty().set("来自网页的信息");
			alert.contentTextProperty().set(event.getData());
			alert.showAndWait();
		});

		webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
			JSObject window = (JSObject) webEngine.executeScript("window");
			System.out.println("sssssssss  - " + newState + "   " + window.getMember("jsObj"));
			window.setMember("jsObj", jsObj);
			if (newState == State.SUCCEEDED) {
//				 exec("onPageLoaded()");
			}
		});
		webEngine.setJavaScriptEnabled(true);
		webEngine.setOnError(new EventHandler<WebErrorEvent>() {

			@Override
			public void handle(WebErrorEvent event) {
				// TODO Auto-generated method stub
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
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		jsObj.setMainApp(mainApp);
	}

	public void load(String filePath) {
		webEngine.load("file:///" + filePath);
	}

	public void exec(String jsData) {
		log.debug("exec : " + jsData);
		webEngine.executeScript(jsData);
	}

}
