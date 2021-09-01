import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BankingApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("application.css");
		stage.setScene(scene);
		stage.setResizable (false);
		stage.centerOnScreen();
		Image icon = new Image("icon.png");
		stage.getIcons().add(icon);
		stage.setTitle("Banking App");
		stage.show();
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
