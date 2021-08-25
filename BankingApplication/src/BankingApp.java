import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {

	Stage stage;
	public void makeMainScreen() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
			Scene scene = new Scene(root, 225, 250);
			stage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		makeMainScreen();
		stage.setResizable (false);
		stage.setTitle("No Name Banking");
		stage.show();
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
