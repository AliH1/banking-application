import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {

	private Stage stage;
	private Scene login, main, register;
	
	public void makeMainScreen() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
			main = new Scene(root, 225, 250);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeLoginScreen() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
			 login = new Scene(root, 225, 250);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeRegisterScreen() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
			 register = new Scene(root, 450, 570);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		makeMainScreen();
		makeLoginScreen();
		makeRegisterScreen();
		stage.setScene(register);
		stage.setResizable (false);
		stage.setTitle("No Name Banking");
		stage.show();
		
	}
	public static void main(String[] args) {
		launch(args);
	}
}
