
//STEP 1. Import required packages
import java.sql.*;
import java.util.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class DAO_Demo {
    public static DAO_Factory daoFactory; // creating globale variable of DAO_Facory type

    public static Scanner sc = new Scanner(System.in);

    // main function
    public static void main(String[] args) {

        try {
            daoFactory = new DAO_Factory(false); 
            daoFactory.setcustomerAsUser();// passing defualt user as customer
           
            while (true) {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

            
                if (!daoFactory.CurrentUser()) {
                    System.out.println(
                            " |Music Store Information System|      " + dtf.format(now)
                                    + "                    |Logged As: customer|");
                } else {
                    System.out.println(
                            " |Music Store Information System|      " + dtf.format(now)
                                    + "                    |Logged As: shopkeeper|");
                }

                
                System.out.println(
                        " |Main Menu|   (C/s-> Tells Wheather Customer Or shopkeeperOr Both Can Perfrom This Task) ");
              
                System.out.println("| Use Cases                              |  |To Select|");
                
                System.out.println("| To Display ProductList (c/s)               |  | Enter 1 |");
                System.out.println("| To Add A New Customer (C/s)            |  | Enter 2 |");
                System.out.println("| To Order A Product (C/s)               |  | Enter 3 |");
                System.out.println("| To Display CustomerList (s)            |  | Enter 4 |");
                System.out.println("| To Delete A Customer (s)               |  | Enter 6 |");
                System.out.println("| To Change CustomerDetails (s)          |  | Enter 7 |");
                System.out.println("| To Change User(shopkeeper/customer) (C/s)  |  | Enter 8 |");
                System.out.println("| To Quit (C/s)                          |  | Enter 9 |");

                
                        
                System.out.print("Please Select Your Chioice: ");
                int option = sc.nextInt();
               
                if (option == 1)
                    ViewProductList();
                else if (option == 2)
                    AddingCustomer();
                else if (option == 3)
                    OrderProduct();
                else if (option == 4)
                    ViewCustomerList();
                
                else if (option == 6)
                    Delete_Customer();
                else if (option == 7)
                    changingCustomerDetails();
                else if (option == 8)
                    ChangeUser();
                else if (option == 9) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("You Need to Select Atleast One Option From Above");
                }

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public static void AddingCustomer() {

        System.out.println("Adding new customer to store...");
        int intInput;
        String strInput;

        Customer cus = new Customer();
        System.out.println();

        System.out.print("Enter Customer'ID (int) : ");
        intInput = sc.nextInt();
        cus.setCusID(intInput);

        System.out.println("Enter Customer's Name: ");
        sc.next();
        strInput = sc.next();
        cus.setCusName(strInput);

        System.out.println("Enter Customer Email: ");
        strInput = sc.next();
        cus.setCusEmail(strInput);

        System.out.println("Enter Customer Phone: ");
        strInput = sc.next();
        cus.setCusPhone(strInput);

        System.out.println("Enter Customer Address: ");
        strInput = sc.next();
        cus.setCusAdd(strInput);

        try {

            daoFactory.activateConnection();

            // Carry out DB operations using DAO
            User udao = daoFactory.getUserDAO();
            udao.Add_customer(cus);

            // End transaction boundary with success
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);
        } catch (Exception e) {
            // End transaction boundary with failure
            // System.out.println("3ok tested ");
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);
            e.printStackTrace();
        }
    }

    public static void ChangeUser() {
        if (daoFactory.CurrentUser()) {
            System.out.println("Switching User From shopkeeper To customer...");
            daoFactory.setcustomerAsUser();
        } else {

            daoFactory.setshopkeeperAsUser(sc);
            System.out.println("Switching User From customer To shopkeeper...");
          
        }

    }

    public static void ViewCustomerList() {

        try {
            daoFactory.activateConnection();

            User user = daoFactory.getUserDAO();
            user.View_CustomerList();
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);
        } catch (Exception e) {
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);
            e.printStackTrace();
        }
    }

    public static void ViewProductList() {

       
        System.out.println("Displaying all Product details...");

       
        try {
            daoFactory.activateConnection();

            User user = daoFactory.getUserDAO();
            user.View_ProductList();
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);
        } catch (Exception e) {
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);
            e.printStackTrace();
        }
    }

    public static void Delete_Customer() {
       
        System.out.println("Enter Customer's ID to be removed: ");
        int intInput = sc.nextInt();

        try {
            // Start transaction boundary
            daoFactory.activateConnection();

            // Carry out DB operations using DAO
            User udao = daoFactory.getUserDAO();
            udao.Delete_Customer(intInput); // passing id of customer's need to be deleted

            // End transaction boundary with success
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);
        } catch (Exception e) {
            // End transaction boundary with failure
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);
            e.printStackTrace();
        }

    }

    public static void changingCustomerDetails() {
       
        System.out.print("Enter Customer's ID: ");
        int intInput = sc.nextInt();

        try {
            // Start transaction boundary
            daoFactory.activateConnection();

            // Carry out DB operations using DAO
            User udao = daoFactory.getUserDAO();
            udao.Changing_Customer_Details(intInput);

            // End transaction boundary with success
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);
        } catch (Exception e) {
            // End transaction boundary with failure
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);
            e.printStackTrace();
        }
    }

    public static void AddingNewAlbum() {

        if (daoFactory.CurrentUser()) {
           
            System.out.println("Error!..Only shopkeeperCan Perform This Operation.");
            System.out.println("You Can Switch User From customer To shopkeeperTo Perfrom This Task...");
           
            return;
        }
       
        System.out.println("Adding new Album to store...");
        int intInput;
        String strInput;

        Album album = new Album();
        System.out.println();

        System.out.print("Enter Album'ID(int): ");
        intInput = sc.nextInt();
        album.setAlbumID(intInput);

        System.out.print("Enter AlbumArtistID(int): ");
        intInput = sc.nextInt();
        album.setAlbumArtistID(intInput);

        System.out.print("Enter Album's Title: ");
        // sc.next();
        strInput = sc.next();
        album.setAlbumTitle(strInput);

        System.out.print("Enter Release Date: ");
        strInput = sc.next();
        album.setReleaseDate(strInput);

        System.out.print("Enter price: ");
        intInput = sc.nextInt();
        album.setAlbumPrice(intInput);

        System.out.print("Enter Genre: ");
        // sc.next();
        strInput = sc.next();
        album.setAlbumGenre(strInput);

        System.out.print("Enter Quantity: ");
        intInput = sc.nextInt();
        album.setAlbumQuantity(intInput);

        try {
            // Start transaction boundary
            daoFactory.activateConnection();

            // Carry out DB operations using DAO
            User udao = daoFactory.getUserDAO();
            udao.Add_Album(album);

            // End transaction boundary with success
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);
        } catch (Exception e) {
            // End transaction boundary with failure
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);
            e.printStackTrace();
        }
    }

    public static void OrderProduct() {

        System.out.println("Available Products In Shope, Please Select Your Choice.");

        ViewProductList();
        System.out.println("Enter the AlbumId of Album to order: ");
        int albumid = sc.nextInt();
        System.out.println("Enter quantity: ");
        int quantity = sc.nextInt();
        try {
            // Start transaction boundary
            daoFactory.activateConnection();

            // Carry out DB operations using DAO
            User udao = daoFactory.getUserDAO();
            udao.Order_Product(albumid, quantity);

            // End transaction boundary with success
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);
        } catch (Exception e) {
            // End transaction boundary with failure
            daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);
            e.printStackTrace();
        }
    }

}