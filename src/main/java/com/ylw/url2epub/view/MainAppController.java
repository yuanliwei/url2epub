package com.ylw.url2epub.view;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.url2epub.MainApp;
import com.ylw.url2epub.controller.BaseController;
import com.ylw.url2epub.utils.PropUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MainAppController extends BaseController {
	private static Log log = LogFactory.getLog(MainAppController.class);

	@FXML
	MenuBar menuBar;

	ProgressBar progressBar;
	ProgressIndicator progressIndicator;

	private MainApp mainApp;

	@FXML
	public StackPane stackPane;

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	@Override
	protected void initialize() {

	}

	@FXML
	public void onClose(ActionEvent event) {
		System.out.println(((MenuItem) event.getSource()).getUserData());
	}

	@FXML
	public void onOpenUrl(ActionEvent event) {
		System.out.println((String) ((MenuItem) event.getSource()).getUserData());
	}

	@FXML
	public void onRefresh() {
		System.out.println("onRefresh()");
		mainApp.mainViewController.filterCode.setValue("onR‘\"efre'sh();");
	}

	@FXML
	public void onAlert() {
		System.out.println("onAlert()");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.titleProperty().set("来自网页的信息");
		alert.headerTextProperty().set("来自网页的信息");
		alert.contentTextProperty().set(mainApp.mainViewController.filterCode.getValue());
		alert.showAndWait();
	}

	@FXML
	public void onOpenFile() {
		log.debug("click open file");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("打开一个html文档");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("html", "*.htm", "*.html"),
				new ExtensionFilter("All Files", "*.*"));

		String lastPath = PropUtils.get("sel_html_path");
		if (org.apache.commons.lang3.StringUtils.isNotEmpty(lastPath)) {
			File file = new File(lastPath);
			if (file.exists() && file.isFile()) {
				lastPath = file.getParent();
			}
			fileChooser.setInitialDirectory(new File(lastPath));
		}

		File selectedFile = fileChooser.showOpenDialog(mainApp.primaryStage);
		if (selectedFile != null) {
			String openFilePath = selectedFile.getAbsolutePath();
			log.debug("selectFile : " + openFilePath);
			mainApp.mainViewController.load(openFilePath);
			PropUtils.put("sel_html_path", openFilePath);
		}
	}

	public void setCenter(BorderPane center2) {
		// TODO Auto-generated method stub

	}
}
