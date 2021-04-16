package at.jku.cps.travart.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class MainViewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField pathSourceModel;

	@FXML
	private Button buttonPickSourceModel;

	@FXML
	private ChoiceBox<?> targetModelTypeSelector;

	@FXML
	private Button buttonPickSaveLocation;

	@FXML
	private TextField pathSaveLocation;

	@FXML
	private Button buttonConvert;

	@FXML
	void initialize() {
		assert pathSourceModel != null
				: "fx:id=\"pathSourceModel\" was not injected: check your FXML file 'TraVarTGui.fxml'.";
		assert buttonPickSourceModel != null
				: "fx:id=\"buttonPickSourceModel\" was not injected: check your FXML file 'TraVarTGui.fxml'.";
		assert targetModelTypeSelector != null
				: "fx:id=\"targetModelTypeSelector\" was not injected: check your FXML file 'TraVarTGui.fxml'.";
		assert buttonPickSaveLocation != null
				: "fx:id=\"buttonPickSaveLocation\" was not injected: check your FXML file 'TraVarTGui.fxml'.";
		assert pathSaveLocation != null
				: "fx:id=\"pathSaveLocation\" was not injected: check your FXML file 'TraVarTGui.fxml'.";
		assert buttonConvert != null
				: "fx:id=\"buttonConvert\" was not injected: check your FXML file 'TraVarTGui.fxml'.";

	}

	@FXML
	void handleConvert(final ActionEvent event) {
		System.out.println("handleConvert");
	}

	@FXML
	void handlePickSaveLocation(final ActionEvent event) {
		System.out.println("handlePickSaveLocation");
	}

	@FXML
	void handlePickSourceModel(final ActionEvent event) {
		System.out.println("handlePickSourceModel");
	}
}
