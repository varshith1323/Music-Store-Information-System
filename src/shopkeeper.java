import java.lang.*;
import java.sql.Connection;
import java.util.*;
import java.sql.*;

public class shopkeeper implements User {

 
    boolean isshopkeeper= true;

    Connection dbConnection;

    public shopkeeper(Connection dbconn) {
        // JDBC driver name and database URL
        // Database credentials
        dbConnection = dbconn;
    }

    @Override
    public void Add_customer(Customer customer) {
        PreparedStatement preparedStatement = null;
        String sql;
        sql = "insert into Customer(CustomerID,CustomerName,CustomerEmail,CustomerPhone,CustomerAddr) values(?,?,?,?,?)";

        try {
            preparedStatement = dbConnection.prepareStatement(sql);

            preparedStatement.setInt(1, customer.getCusID());
            preparedStatement.setString(2, customer.getCusName());
            preparedStatement.setString(3, customer.getCusEmail());
            preparedStatement.setString(4, customer.getCusPhone());
            preparedStatement.setString(5, customer.getCusAddr());

            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("New Customer has been added to record with below details...");
            
            System.out.println("Cutomer ID : " + customer.getCusID());
            System.out.println("Cutomer Name : " + customer.getCusName());
            System.out.println("Cutomer Email : " + customer.getCusEmail());
            System.out.println("Cutomer Phone : " + customer.getCusPhone());
            System.out.println("Cutomer Address : " + customer.getCusAddr());
          

        } catch (SQLException e) {
           

            System.out.println("|Cannot Complete Action...|");
            System.out.println("| ALERT : Customer Already Exists With This CustomerID In Record...|");
            

        }

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

    }

    @Override
    public void Order_Product(int AlbumId, int quantity) {
        Album album = new Album();

        String sql;

        Statement stmt = null;

        try {
            stmt = dbConnection.createStatement();
            sql = "select * from Album where AlbumId =" + AlbumId;
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 5: Extract data from result set
            while (rs.next()) {
                // Retrieve by column name
                int albumid = rs.getInt("AlbumId");
                int albumartistid = rs.getInt("ArtistId");
                String albumtitle = rs.getString("Title");
                String albumrelease = rs.getString("ReleaseDate");
                int albumprice = rs.getInt("Price");
                String albumgenre = rs.getString("Genre");
                int albumquantity = rs.getInt("Quantity");

                album.setAlbumID(albumid);
                album.setAlbumArtistID(albumartistid);
                album.setAlbumTitle(albumtitle);
                album.setReleaseDate(albumrelease);
                album.setAlbumPrice(albumprice);
                album.setAlbumGenre(albumgenre);
                album.setAlbumQuantity(albumquantity);

                break;
                // Add exception handling here if more than one row is returned
            }
        } catch (SQLException ex) {
            // handlling any errors if the customer not found in record
            System.out.println("Error!..No such Album exists in record...");
            return;

        }

        System.out.println("Processing your Order...");
        if (quantity > album.getAlbumQuantity()) {
            System.out.println("Sorry!...Item is out of stock");
            System.out.println("Max quantity that can be order is: " + album.getAlbumQuantity());
        } else {
            int leftQ = album.getAlbumQuantity() - quantity;
            album.setAlbumQuantity(leftQ);
        }

        PreparedStatement preparedStatement = null;
        sql = "update Album set Quantity=? where AlbumId = ?";
        try {

            System.out.println();
            preparedStatement = dbConnection.prepareStatement(sql);

            preparedStatement.setInt(1, album.getAlbumQuantity());
            preparedStatement.setInt(2, album.getAlbumID());
            // execute update SQL stetement
            preparedStatement.executeUpdate();
            System.out.println();
            System.out.println("Remaining Stock of Album After Order");
            
            System.out.println("AlbumId: " + album.getAlbumID() + " | ArtistId: " + album.getAlbumArtistID()
                    + " | Titlel: " + album.getAlbumTitle()
                    + " | ReleaseDate: " + album.getAlbumReleaseDate() + " | Price: " + album.getAlbumPrice()
                    + " | Genre:" + album.getAlbumGenre() + " | Quantity:" + album.getAlbumQuantity());
          
            System.out.println(e.getMessage());
        }

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void View_ProductList() {

        PreparedStatement preparedStatement = null;
        String sql;

        Statement stmt = null;

        try {
            stmt = dbConnection.createStatement();
            sql = "Select * from Album";
            ResultSet rs = stmt.executeQuery(sql);

            // Extracting data from Album data set
            System.out.println("Available Albums In Store");
           
            while (rs.next()) {
                // Retrieve each row
                String AlbumID = rs.getString("AlbumID");
                String ArtistID = rs.getString("ArtistID");
                String Title = rs.getString("Title");
                String ReleaseDate = rs.getString("ReleaseDate");
                int Price = rs.getInt("Price");
                String Genre = rs.getString("Genre");
                int quantity = rs.getInt("Quantity");

                System.out.println("AlbumID: " + AlbumID + " | ArtistID: " + ArtistID + " | Title: " + Title
                        + " | ReleaseDate: " + ReleaseDate + " | Price: " + Price + " | Genre:" + Genre + "| Quantity: "
                        + quantity);

            }
           
        } catch (SQLException ex) {
            // handlling any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    @Override
    public void View_CustomerList() {
        PreparedStatement preparedStatement = null;
        String sql;

        Statement stmt = null;

        try {
            stmt = dbConnection.createStatement();
            sql = "Select * from Customer";
            ResultSet rs = stmt.executeQuery(sql);

            // Extracting data from Album data set
            System.out.println("Customer Record of Store");
            
            while (rs.next()) {
                // Retrieve each row
                String CustomerID = rs.getString("CustomerID");
                String CustomerName = rs.getString("CustomerName");
                String CustomerEmail = rs.getString("CustomerEmail");
                String CustomerPhone = rs.getString("CustomerPhone");
                String CustomerAddr = rs.getString("CustomerAddr");

                System.out.println("CustomerID: " + CustomerID + " | CustomerName: " + CustomerName
                        + " | CustomerEmail: " + CustomerEmail + " | CustomerPhone: " + CustomerPhone
                        + " | CustomerAddr: " + CustomerAddr);

                

            }
        } catch (SQLException ex) {
            // handlling any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    // shopkeeper
    @Override
    public void Add_Album(Album album) {
        PreparedStatement preparedStatement = null;
        String sql;
        sql = "insert into Album(AlbumID,ArtistId,Title,ReleaseDate,Price,Genre,Quantity) values (?,?,?,?,?,?,?)";

        try {
            System.out.println();
            System.out.println("Inserting new Album to Shop...");
            preparedStatement = dbConnection.prepareStatement(sql);

            preparedStatement.setInt(1, album.getAlbumID());
            preparedStatement.setInt(2, album.getAlbumArtistID());
            preparedStatement.setString(3, album.getAlbumTitle());
            preparedStatement.setString(4, album.getAlbumReleaseDate());
            preparedStatement.setInt(5, album.getAlbumPrice());
            preparedStatement.setString(6, album.getAlbumGenre());
            preparedStatement.setInt(7, album.getAlbumQuantity());
            preparedStatement.executeUpdate();

            System.out.println();
            System.out.println("New Album has been added to the Shop with below details");
          
            System.out.println("AlbumID: " + album.getAlbumID() + " | ArtistID: " + album.getAlbumArtistID()
                    + " | Title: " + album.getAlbumTitle()
                    + " | ReleaseDate: " + album.getAlbumReleaseDate() + " | Price: " + album.getAlbumPrice()
                    + " | Genre:" + album.getAlbumGenre() + " | Quantity: " + album.getAlbumQuantity());
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void Changing_Customer_Details(int customerid) {
        Customer customer = new Customer();

        String sql;

        Statement stmt = null;

        try {

            stmt = dbConnection.createStatement();

            sql = "select * from Customer where CustomerId =" + customerid;
            ResultSet rs = stmt.executeQuery(sql);

            // STEP 5: Extract data from result set

            while (rs.next()) {
                // Retrieve by column name
                int cusid = rs.getInt("CustomerId");
                String cusname = rs.getString("CustomerName");
                String cusemail = rs.getString("CustomerEmail");
                String cusphone = rs.getString("CustomerPhone");
                String cusaddr = rs.getString("CustomerAddr");

                customer.setCusID(cusid);
                customer.setCusName(cusname);
                customer.setCusEmail(cusemail);
                customer.setCusPhone(cusphone);
                customer.setCusAdd(cusaddr);
            }

            // Add exception handling here if more than one row is returned
        } catch (SQLException ex) {
            // handlling any errors if the customer not found in record
            System.out.println("Error!..No such Customer exists in record...");
            return;
            // System.out.println("SQLException: " + ex.getMessage());
            // System.out.println("SQLState: " + ex.getSQLState());
            // System.out.println("VendorError: " + ex.getErrorCode());
        }

        Scanner sc = new Scanner(System.in);
        String cmd = "";

        System.out.println("Changing Customer Details...");

        
        System.out.print("Do you want to change customer's name? Y/N : ");
        cmd = sc.next();
        if (cmd.equals("Y")) {

            System.out.print("Enter new Name for Customer: ");
            String str = sc.next();
            customer.setCusName(str);
        }

        System.out.println();
        System.out.print("Do you want to change customer's Email? Y/N : ");
        cmd = sc.next();
        if (cmd.equals("Y")) {

            System.out.print("Enter new Email for Customer: ");
            String str = sc.next();
            customer.setCusEmail(str);
        }

        System.out.println();
        System.out.print("Do you want to change customer's Phone? Y/N : ");
        cmd = sc.next();
        if (cmd.equals("Y")) {

            System.out.print("Enter new Phone for Customer: ");
            String str = sc.next();
            customer.setCusPhone(str);
        }

        System.out.println();
        System.out.print("Do you want to change customer's Add? Y/N : ");
        cmd = sc.next();
        if (cmd.equals("Y")) {

            System.out.print("Enter new Address for Customer: ");
            String str = sc.next();
            customer.setCusAdd(str);
        }
       

        PreparedStatement preparedStatement = null;
        sql = "update Customer set CustomerName=?,CustomerEmail=?,CustomerPhone=?,CustomerAddr=? where CustomerId = ?";

        try {

            System.out.println("Updating Customer details....");
            System.out.println();
            preparedStatement = dbConnection.prepareStatement(sql);

            preparedStatement.setString(1, customer.getCusName());
            preparedStatement.setString(2, customer.getCusEmail());
            preparedStatement.setString(3, customer.getCusPhone());
            preparedStatement.setString(4, customer.getCusAddr());
            preparedStatement.setInt(5, customer.getCusID());
            // execute update SQL stetement
            preparedStatement.executeUpdate();
            System.out.println();
            System.out.println("Customer details has been succesfully updated with below details");
           
            System.out.println("CustomerID: " + customer.getCusID() + " | CustomerName: " + customer.getCusName()
                    + " | CustomerEmail: " + customer.getCusEmail()
                    + " | CustomerPhone: " + customer.getCusPhone() + " | Customer Addr: " + customer.getCusAddr());
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void Delete_Customer(int customerid) {

        // Declare PreparedStatement object and SQL query string
        PreparedStatement preparedStatement = null;
        String sql;

        // Construct SQL query string 
        //to delete customer record from database using given customer ID
        sql = "delete from Customer where CustomerID = " + customerid;

        try {
            // Inform user that customer is being deleted
            System.out.println();
            System.out.println("Deleting Customer...");

            // Create PreparedStatement object and execute SQL query
            preparedStatement = dbConnection.prepareStatement(sql);
            // execute delete SQL stetement
            preparedStatement.executeUpdate();
            System.out.println();
          
            System.out.println("Customer with Customer's ID " + customerid + " has been deleted successfully...");
            

        } catch (SQLException e) {
            //deletion failed due to non-existent customer record
            // or other SQL error
            System.out.println("No Such Customer..");
            System.out.println(e.getMessage());
        }

        try {
            // Close PreparedStatement object
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
