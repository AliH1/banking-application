import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BankingAppController implements Initializable {
	
	//Login.fxml components
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
	//Customer.fxml components
	@FXML Label balanceLabel;
	@FXML Label totalBalance;
	@FXML TextField depositTF;
	@FXML TextField withDrawTF;
	@FXML Label errorLabel;
	//AccountInfo.fxml components
	@FXML Label username;
	@FXML Label email;
	@FXML Label phoneNumber;
	//ChangePassword.fxml
	@FXML TextField confirmUsername;
	@FXML TextField confirmEmail;
	@FXML TextField confirmPhoneNumber;
	@FXML TextField newPasswordTF;
	@FXML TextField newPassword2TF;
	@FXML Label changePasswordError;
	
	private Connection connection = connectToDB();
	private Stage stage;
	private Scene scene;
	private Parent root;
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(this.balanceLabel != null) 
			updateBalance();
		if(username != null && email != null && phoneNumber != null) {
			updateAccountInfo();
		}
	}
	
	private void updateAccountInfo() {			
		try {
			Preferences userPref = Preferences.userRoot();
			String username = userPref.get("user", null); //null should never be reached
			String query = "SELECT email from customer where username = '"+ username +"'";
		    Statement statement = connection.createStatement();
		    ResultSet rs = statement.executeQuery(query);
		    this.username.setText(username);
		    if(rs.next()) {
		    	String emailString = rs.getString(1);
		    	email.setText(emailString);
		    }
		    query = "SELECT phone_number from customer where username = '"+ username +"'";
		    statement = connection.createStatement();
		    rs = statement.executeQuery(query);
		    if(rs.next()) {
		    	String phoneNum = rs.getString(1);
		    	phoneNumber.setText(phoneNum);
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateBalance() {
		try {
			Preferences userPref = Preferences.userRoot();
			String username = userPref.get("user", null); //null should never be reached
			String query = "SELECT balance from customer where username = '"+ username +"'";
		    Statement statement = connection.createStatement();
		    ResultSet rs = statement.executeQuery(query);
		    if(rs.next()) {
		    	String rounded_amount = String.format("%.2f",rs.getDouble(1));
		    	balanceLabel.setText("$"+rounded_amount);
		    }
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Connection connectToDB() {
	      try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			final String url = "jdbc:mysql:///bankappdb";
			final String user = "root";
			final String password = ".z#ZeCjpSFpP9hCZ";
			Connection conn = DriverManager.getConnection(url, user, password);
			if (conn == null) {
		         System.out.println("connection is not established");
		         return conn;
			}else {
				System.out.println("connection established");
				return conn;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;     
	}
	
	public void loginPressed(ActionEvent e) {
		String username = usernameTextField.getText();
		Preferences userPref = Preferences.userRoot();
		userPref.put("user", username);
		if(checkLoginDetails(username, passwordTextField.getText())) {
			if(!incorrectLoginLabel.getText().equals(null) && incorrectLoginLabel.getText().equals("Incorrect login details")) 
				incorrectLoginLabel.setText("");
			PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
		    pause.setOnFinished(event ->{
				try {
					switchToCustomerScene(e);
				} catch (Exception e1) {
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
		if(register(registerUsernameTF.getText(), registerPasswordTF.getText(), registerPassword2TF.getText(), registerEmailTF.getText(), 
					registerPhoneNumberTF.getText(), registerInitialDepositTF.getText())) {
			if(!registerStatus.getText().equals(null) || !registerStatus.getText().equals(""))
				registerStatus.setText("");
			PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
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
		switchToCustomerScene(e);
	}	
		
	public void depositPressed(ActionEvent e){
		try {
			Preferences userPref = Preferences.userRoot();
			String username = userPref.get("user", null); //null should never be reached 
			String amount = String.format("%.2f", Double.parseDouble(depositTF.getText()));
			String query = "update customer set balance = balance + "+amount+" where username = '"+ username +"'";
		    Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			depositTF.setText(null);
			System.out.println("deposited: "+ amount);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}	
	
	public void withdrawPressed(ActionEvent e){
		try {
			Preferences userPref = Preferences.userRoot();
			String username = userPref.get("user", null); //null should never be reached 
			String amount = String.format("%.2f", Double.parseDouble(withDrawTF.getText()));
			String query = "update customer set balance = balance - "+amount+" where username = '"+ username +"'";
		    Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			withDrawTF.setText(null);
			System.out.println("withdrew: "+ amount);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}	
	
	public void deleteAccountPressed(ActionEvent e){
		try {
			Preferences userPref = Preferences.userRoot();
			String username = userPref.get("user", null); //null should never be reached 
			String query = "DELETE FROM customer where username = '"+ username +"'";
		    Statement statement = connection.createStatement();
			statement.executeUpdate(query);
			switchToLoginScene();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}	
	public void forgotPasswordPressed(ActionEvent e) {
		if(checkCredentials()) {
			if(newPasswordTF.getText().equals(newPassword2TF.getText())) {
				changePassword();
				try {
					switchToLoginScene(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			else {
				changePasswordError.setText("Password does not match");
			}
		}
		else {
			changePasswordError.setText("Credentials do not match");
		}
	}
	public void changePasswordPressed(ActionEvent e){
		if(checkCredentials()) {
			if(newPasswordTF.getText().equals(newPassword2TF.getText())) {
				changePassword();
				switchToCustomerScene(e);
			}
			else {
				changePasswordError.setText("Password does not match");
			}
		}
		else {
			changePasswordError.setText("Credentials do not match");
		}
	}
	
	private boolean checkCredentials() {
		try {
			Preferences userPref = Preferences.userRoot();
			String username = userPref.get("user", null); //null should never be reached 
			String email = null, phoneNum = null;
			String query = "SELECT email from customer where username = '"+ username +"'";
		    Statement statement = connection.createStatement();
		    ResultSet rs = statement.executeQuery(query);
		    if(rs.next()) {
		    	 email = rs.getString(1);
		    }
			query = "SELECT phone_number from customer where username = '"+ username +"'";
		    statement = connection.createStatement();
		    rs = statement.executeQuery(query);
		    if(rs.next()) {
		    	 phoneNum = rs.getString(1);
		    }
			if(confirmUsername.getText().equals(username) && confirmEmail.getText().equals(email) && confirmPhoneNumber.getText().equals(phoneNum)) 
				return true;
			else 
				return false;
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
	    
	}

	private void changePassword() {
		try {
			Preferences userPref = Preferences.userRoot();
			String username = userPref.get("user", null); //null should never be reached 
			String query = "update customer set password = '"+ newPasswordTF.getText()+"' where username = '"+ username +"'";
		    Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	public void acountInfoPressed(ActionEvent e) {
		switchToAccountInfoScene(e);
	}
	
	public void logOutPressed(ActionEvent e) {
		try {
			switchToLoginScene();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	public void switchToAccountInfoScene(ActionEvent e) {
		try {
			root = FXMLLoader.load(getClass().getResource("AccountInfo.fxml"));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add("application.css");
			stage.setScene(scene);
			stage.centerOnScreen();
		} catch(Exception e1){
			e1.printStackTrace();
		}
	}
	
	public void switchToLoginScene() throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)totalBalance.getScene().getWindow();
		scene = new Scene(root);
		scene.getStylesheets().add("application.css");
		stage.setScene(scene);
		stage.centerOnScreen();
	}
	public void switchToLoginScene(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Login.fxml"));
		stage = (Stage)((Node) e.getSource()).getScene().getWindow();
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
	
	public void switchToCustomerScene(ActionEvent e){
		try {
			root = FXMLLoader.load(getClass().getResource("Customer.fxml"));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add("application.css");
			stage.setScene(scene);
			stage.centerOnScreen();
		} catch(Exception e1){
			e1.printStackTrace();
		}
			
	}
	
	public void switchToChangePasswordScene(ActionEvent e){
		try {
			root = FXMLLoader.load(getClass().getResource("ChangePassword.fxml"));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add("application.css");
			stage.setScene(scene);
			stage.centerOnScreen();
		} catch(Exception e1){
			e1.printStackTrace();
		}
			
	}
	
	public void switchToChangePassword2Scene(ActionEvent e){
		try {
			root = FXMLLoader.load(getClass().getResource("ChangePassword2.fxml"));
			stage = (Stage)((Node)e.getSource()).getScene().getWindow();
			scene = new Scene(root);
			scene.getStylesheets().add("application.css");
			stage.setScene(scene);
			stage.centerOnScreen();
		} catch(Exception e1){
			e1.printStackTrace();
		}
			
	}
	
	
	public boolean checkLoginDetails(String username, String password) {
		try {
			String query = "SELECT * from customer where username = '"+ username +"' and password = '" + password + "'";
		    Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(query);
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
				String query = "INSERT INTO customer(username, password, email, phone_number, balance) VALUES (?,?,?,?,?)";
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

