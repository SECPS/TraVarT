package at.jku.cps.travart.gui.views;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView extends Application {

	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(final Stage primaryStage) throws Exception {
		URL resource = getClass().getClassLoader().getResource("fxml/TraVarTGui.fxml");
		Parent root = FXMLLoader.load(resource);
		Scene scence = new Scene(root);

		primaryStage.setTitle("TraVarT - Transforming Variability Artifacts");
		primaryStage.setScene(scence);
		primaryStage.show();
	}

}
