package com.ylw.url2epub.view;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.url2epub.MainApp;
import com.ylw.url2epub.controller.BaseController;
import com.ylw.url2epub.utils.PropUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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

	private BorderPane center;

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
		mainApp.mainViewController.webEngine.load((String) ((MenuItem) event.getSource()).getUserData());
	}

	@FXML
	public void onRefresh() {
		mainApp.mainViewController.webEngine.reload();
	}

	@FXML
	public void onAlert() {
		mainApp.mainViewController.webEngine.executeScript("alert('heeeeeello')");
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

	@FXML
	public void startFileMonitor() {
		String path = "C:\\Users\\ylw\\Desktop\\jseditor\\";
		Path path2 = Paths.get(path);
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			path2.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

			WatchKey key = null;
			while (true) { // important - create an indefinite loop to watch the
							// file system changes.
				try {
					key = watchService.take();
					for (WatchEvent<?> event : key.pollEvents()) {
						Kind<?> kind = event.kind();
						System.out.println("Event on " + event.context().toString() + " is " + kind);
					}
					System.out.println("ttttttttttttttttttttttttttttttttt");
				} catch (InterruptedException e) {
					System.out.println("InterruptedException: " + e.getMessage());
				}
				boolean reset = key.reset();
				if (!reset)
					break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
