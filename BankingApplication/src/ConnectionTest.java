import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ConnectionTest {
   public static void main(String[] args) throws Exception {

      // register Oracle thin driver with DriverManager service
      // It is optional for JDBC4.x version
      Class.forName("com.mysql.cj.jdbc.Driver");

      // variables
      final String url = "jdbc:mysql:///bankappdb";
      final String user = "root";
      final String password = ".z#ZeCjpSFpP9hCZ";

      // establish the connection
      Connection con = DriverManager.getConnection(url, user, password);
      
      // display status message
      
      if (con == null) {
         System.out.println("JDBC connection is not established");
         return;
      } else
         System.out.println("Congratulations," + 
              " JDBC connection is established successfully.\n");
      //example of insert
      String query = "INSERT INTO customer VALUES(1, 'ali', 'test', 'testing@gmail.com', '111-111-111', 1000)";
      //example of delete
      String query2 = "DELETE FROM customer WHERE customer_id = 1";
      PreparedStatement pst = con.prepareStatement(query);
      
      // close JDBC connection
      con.close();

   } 
}