import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankingAppController {
	Connection connection;
	
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

