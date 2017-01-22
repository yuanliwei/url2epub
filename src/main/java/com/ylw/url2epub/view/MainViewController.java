package com.ylw.url2epub.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.url2epub.MainApp;
import com.ylw.url2epub.controller.BaseController;
import com.ylw.url2epub.utils.Res;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class MainViewController extends BaseController {
	private static Log log = LogFactory.getLog(MainViewController.class);

	private MainApp mainApp;

	@FXML
	ImageView bookPic;

	@FXML
	TextField textBookName;

	@FXML
	TextField textAuthor;

	@FXML
	TextField textArticleId;

	@FXML
	TextField textUrl;

	@FXML
	TextField textDeep;

	@FXML
	StackPane filterStack;

	@FXML
	JavascriptCodeEditWebView filterCode;

	@FXML
	StackPane chapterStack;

	@FXML
	JavascriptCodeEditWebView chapterCode;

	@FXML
	ProgressBar progressBar;
 
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	protected void initialize() {
		bookPic.setImage(Res.getImageFromRes("icon.png"));
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

	@FXML
	public void selCover() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.titleProperty().set("信息");
		alert.headerTextProperty().set("信息");
		alert.contentTextProperty().set("选择封面");
		alert.showAndWait();
	}
	
	public void stop() {
//		filterCode.stop();
//		chapterCode.stop();
	}

}
