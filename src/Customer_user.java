import java.lang.*;
import java.util.*;
import java.sql.*;

public class Customer_user implements User {

   
    boolean isshopkeeper = false;

    Connection dbConnection;

    public Customer_user(Connection dbconn) {
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
           

            // System.out.println(e.getMessage());
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
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        System.out.println("Processing your Order...");
        if (quantity > album.getAlbumQuantity()) {
            System.out.println("Sorry!...Item is out of stock");
            System.out.println("Max quantity that can be ordered is: " + album.getAlbumQuantity());
            System.out.println("If you want to order" + album.getAlbumQuantity()
                    + "albums select 1 else 0 if you dont want to order any album");
            Scanner sc = new Scanner(System.in);
            int temp = sc.nextInt();
            if (temp == 1)
                album.setAlbumQuantity(0);
            else
                return;
        } else {
            int leftQ = album.getAlbumQuantity() - quantity;
            album.setAlbumQuantity(leftQ);
        }

        PreparedStatement preparedStatement = null;
        sql = "update Album set AlbumId=?,ArtistId=?,Title=?,ReleaseDate=?,Price=?,Genre=?,Quantity=? where CustomerId = ?";

        try {

            System.out.println();
            preparedStatement = dbConnection.prepareStatement(sql);

            preparedStatement.setInt(1, album.getAlbumID());
            preparedStatement.setInt(2, album.getAlbumArtistID());
            preparedStatement.setString(3, album.getAlbumTitle());
            preparedStatement.setString(4, album.getAlbumReleaseDate());
            preparedStatement.setInt(6, album.getAlbumPrice());
            preparedStatement.setString(7, album.getAlbumGenre());
            preparedStatement.setInt(8, album.getAlbumQuantity());
            // execute update SQL stetement
            preparedStatement.executeUpdate();
            System.out.println();
            System.out.println("Remaining Stock of Album After Order");
         
            System.out.println("AlbumId: " + album.getAlbumID() + " | ArtistId: " + album.getAlbumArtistID()
                    + " | Titlel: " + album.getAlbumTitle()
                    + " | ReleaseDate: " + album.getAlbumReleaseDate() + " | Price: " + album.getAlbumPrice()
                    + " | Genre:" + album.getAlbumGenre() + " | Quantity:" + album.getAlbumQuantity());
         
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

    // Exclusive shopkeeper Operations
    @Override
    public void View_CustomerList() {
       
        System.out.println("Error!..Only shopkeeper Can Perform This Operation.");
        System.out.println("You Can Switch User From Customer_user To shopkeeper To Perfrom This Task...");
       
    }

    // shopkeeper
    @Override
    public void Add_Album(Album album) {
       
        System.out.println("Error!..Only shopkeeper Can Perform This Operation.");
        System.out.println("You Can Switch User From Customer_user To shopkeeper To Perfrom This Task...");
       
    }

    @Override
    public void Changing_Customer_Details(int customerid) {
       
        System.out.println("Error!..Only shopkeeper Can Perform This Operation.");
        System.out.println("You Can Switch User From Customer_user To shopkeeper To Perfrom This Task...");
       
    }

    @Override
    public void Delete_Customer(int customerid) {
       
        System.out.println("Error!..Only shopkeeper Can Perform This Operation.");
        System.out.println("You Can Switch User From Customer_user To shopkeeper To Perfrom This Task...");
       
    }
}