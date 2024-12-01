import java.lang.*;
import java.sql.*;
import java.util.*;

/*
	Methods to be called in the following order:

	1. activateConnection
	2. 	Any number getDAO calls with any number of database transactions
	3. deactivateConnection
*/

public class DAO_Factory {

    public enum TXN_STATUS {
        COMMIT, ROLLBACK
    };

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/MusicStore";
    static final String USER = "root";
    static final String PASS = "varshith";
    Connection dbconnection = null;

    // You can add additional DAOs here as needed
    User user = null;

    boolean activeConnection = false;
    boolean isshopkeeper= false;

    public DAO_Factory(boolean isshopkeeper) {
        dbconnection = null;
        activeConnection = false;
        this.isshopkeeper = isshopkeeper;
    }

    public void activateConnection() throws Exception {
        if (activeConnection == true)
            throw new Exception("Connection already active");

        // System.out.println("Connecting to database...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            dbconnection = DriverManager.getConnection(DB_URL, USER, PASS);
            dbconnection.setAutoCommit(false);
            activeConnection = true;
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public boolean CurrentUser() {
        return isshopkeeper;
    }

    public void setshopkeeperAsUser(Scanner scan) {
      {
            System.out.print("Enter your desired username: ");
            String desiredUsername = scan.next();
            
            System.out.print("Enter your desired passw: ");
            String desiredpassw = scan.next();
            
            String query = "SELECT * FROM login WHERE username=? AND passw=?";
            
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
            
                // Set the parameters for the prepared statement using user input
                pstmt.setString(1, desiredUsername);
                pstmt.setString(2, desiredpassw);
                // Execute the query
                ResultSet rs = pstmt.executeQuery();

                // Check if the query returned any rows
                if (rs.next()) {
                    System.out.println("Login successful.");
                    isshopkeeper = true;
                } else {
                    System.out.println("Invalid username or passw.");
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void setcustomerAsUser() {
        isshopkeeper= false;
    }

    public boolean getUserStatus() {
        return isshopkeeper;
    }

    public User getUserDAO() throws Exception {
        if (activeConnection == false)
            throw new Exception("Connection not activated...");

        if (user == null && isshopkeeper) {

            user = new shopkeeper(dbconnection);
        } else if (user == null) {
            user = new Customer_user(dbconnection);
        }
        return user;
    }

    public void deactivateConnection(TXN_STATUS txn_status) {
        // Okay to keep deactivating an already deactivated connection
        activeConnection = false;
        if (dbconnection != null) {
            try {
                if (txn_status == TXN_STATUS.COMMIT)
                    dbconnection.commit();
                else
                    dbconnection.rollback();

                dbconnection.close();
                dbconnection = null;

                // Nullify all DAO objects
                user = null;
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
    }

};