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
import javafx.scene.control.TextField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class MainViewController extends BaseController {
	private static Log log = LogFactory.getLog(MainViewController.class);

	private MainApp mainApp;

	@FXML
	TextField book_name;

	@FXML
	TextField author;

	@FXML
	ProgressBar progress;

	@FXML
	TextField articleid;

	@FXML
	Label cover_path;

	@FXML
	StackPane url_rule;

	@FXML
	StackPane chapter_rule;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	protected void initialize() {
		// Initialize the person table with the two columns.

	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void load(String filePath) {
		System.out.println("load file:///" + filePath);
	}

	public void exec(String jsData) {
		log.debug("exec : " + jsData);
	}

	@FXML
	public void selectPicture(MouseEvent event) {
	}

	@FXML
	public void startPull(MouseEvent event) {
	}

}
