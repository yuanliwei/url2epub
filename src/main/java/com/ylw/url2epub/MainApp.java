package com.ylw.url2epub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.url2epub.model.UrlContent;
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
import com.ylw.url2epub.utils.PropUtils;
import com.ylw.url2epub.utils.Res;
import com.ylw.url2epub.view.MainAppController;
import com.ylw.url2epub.view.MainViewController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

	private static Log log = LogFactory.getLog(MainApp.class);

	private BorderPane root;
	public MainViewController mainViewController;
	public MainAppController mainAppController;
	public Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("RESTfulTest");

		primaryStage.getIcons().add(Res.getImageFromRes("icon.jpg"));
		PropUtils.load();
		FXMLLoader loader = Res.getFXMLLoader("MainApp.fxml");
		try {
			root = loader.load();
			mainAppController = loader.getController();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		Scene scene = new Scene(root, 1000, 600);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		initCenter();

		initController();
	}

	private void initCenter() {
		FXMLLoader loader = Res.getFXMLLoader("MainView.fxml");
		try {
			BorderPane center = loader.load();
			mainViewController = loader.getController();
			mainAppController.setCenter(center);
			mainAppController.stackPane.getChildren().add(center);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	private void initController() {
		mainAppController.setMainApp(this);
		mainViewController.setMainApp(this);
		mainViewController.load(PropUtils.get("sel_html_path"));
	}

	@Override
	public void stop() throws Exception {
		PropUtils.store();
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
