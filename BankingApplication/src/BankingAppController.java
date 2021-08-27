import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingAppController {
	
	private Connection connection;
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void connectToDB() {
	      try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			final String url = "jdbc:mysql:///bankappdb";
			final String user = "root";
			final String password = ".z#ZeCjpSFpP9hCZ";
			connection = DriverManager.getConnection(url, user, password);
			if (connection == null) {
		         System.out.println("connection is not established");
		         return;
			}else 
				System.out.println("connection established");
		}catch (Exception e) {
			e.printStackTrace();
		}     
	}
	
	public void closeConnection() {
		try {
			this.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void loginPressed(ActionEvent e) {
		
	}
	
	public void registerPressed(ActionEvent e) throws IOException {
		switchToLoginScene(e);
	}
	
	public void signUpPressed(ActionEvent e) throws IOException {
		switchToRegisterScene(e);
	}
	
	public void backPressed(ActionEvent e) throws IOException {
		switchToLoginScene(e);
	}	
	
	public void switchToLoginScene(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("login.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add("application.css");
		stage.setScene(scene);
		stage.centerOnScreen();
	}
	
	public void switchToRegisterScene(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("register.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add("application.css");
		stage.setScene(scene);
		stage.centerOnScreen();
	}
	
	public boolean checkLoginDetails(String username, String password) throws SQLException {
		String query = "SELECT * from customer where username = '"+ username +"' and password = '" + password + "'";
	    Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery(query);
		if(rs.next())
			return true;
		return false;
	}
	
	public void register() {
		
	}
}

