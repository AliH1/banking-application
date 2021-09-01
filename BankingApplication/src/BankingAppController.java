import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BankingAppController {
	
	@FXML TextField usernameTextField;
	@FXML TextField passwordTextField;
	@FXML Label incorrectLoginLabel;
	//Register.fxml components
	@FXML TextField registerUsernameTF;
	@FXML TextField registerEmailTF;
	@FXML TextField registerPhoneNumberTF;
	@FXML TextField registerInitialDepositTF;
	@FXML TextField registerPasswordTF;
	@FXML TextField registerPassword2TF;
	@FXML Label registerStatus;
	
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
		if(connection==null)
			connectToDB();
		if(checkLoginDetails(usernameTextField.getText(), passwordTextField.getText())) {
			if(!incorrectLoginLabel.getText().equals(null) && incorrectLoginLabel.getText().equals("Incorrect login details")) 
				incorrectLoginLabel.setText("");
			PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
		    pause.setOnFinished(event ->{
				try {
					switchToCustomerScene(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    });
			pause.play();
		}
		else {
			incorrectLoginLabel.setText("Incorrect login details");
		}
	}
	
	public void registerPressed(ActionEvent e){
		if(connection==null)
			connectToDB();
		if(register(registerUsernameTF.getText(), registerPasswordTF.getText(), registerPassword2TF.getText(), registerEmailTF.getText(), 
					registerPhoneNumberTF.getText(), registerInitialDepositTF.getText())) {
			if(!registerStatus.getText().equals(null) || !registerStatus.getText().equals(""))
				registerStatus.setText("");
			PauseTransition pause = new PauseTransition(Duration.seconds(0.7));
		    pause.setOnFinished(event ->{
				try {
					switchToLoginScene(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		    });
			pause.play();
		}
	}
	public void signUpPressed(ActionEvent e) throws IOException {
		switchToRegisterScene(e);
	}
	
	public void backPressed(ActionEvent e) throws IOException {
		switchToLoginScene(e);
	}	
	
	public void switchToLoginScene(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add("application.css");
		stage.setScene(scene);
		stage.centerOnScreen();
	}
	
	public void switchToRegisterScene(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Register.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add("application.css");
		stage.setScene(scene);
		stage.centerOnScreen();
	}
	
	public void switchToCustomerScene(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add("application.css");
		stage.setScene(scene);
		stage.centerOnScreen();
	}
	
	public boolean checkLoginDetails(String username, String password) {
		try {
			String query = "SELECT * from customer where username = '"+ username +"' and password = '" + password + "'";
		    Statement statement = connection.createStatement();
			ResultSet rs;
			rs = statement.executeQuery(query);
			if(rs.next())
				return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean register(String username, String password, String password2, String phoneNumber, String email, String deposit) {
		try {
			if(password.equals(password2)) {
				String query = "INSERT INTO customer(username, password, email, phone_number, initial_deposit) VALUES (?,?,?,?,?)";
				PreparedStatement statement = connection.prepareStatement(query);
				statement.setString(1, username);
				statement.setString(2, password);
				statement.setString(3, phoneNumber);
				statement.setString(4, email);
				statement.setBigDecimal(5, new BigDecimal(deposit));
				statement.executeUpdate();
				return true;
			}
			else {
				registerStatus.setText("Password does not match");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}

