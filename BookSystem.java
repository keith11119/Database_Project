import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class BookSystem {
    private static boolean finish = false;
    private static Connection conn = Main.connect();
    public static void init() {
        do {
            System.out.println("<This is the system interface.>");
            System.out.println("-------------------------------");
            System.out.println("1. Create Table.");
            System.out.println("2. Delete Table.");
            System.out.println("3. Insert Data.");
            System.out.println("4. Set System Date.");
            System.out.print("5. Back to main menu.\n\nPlease enter your choice??..");
            //The project_spec shows "Pleae enter your choice" which I believe "Pleae" is a typo 
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            while (choice < 1 || choice > 5){
                System.out.println("[ERROR]: Input is invalid.\n\nPlease enter again.");
                choice = scanner.nextInt();
            }
            switch (choice) {
                case 1:
                    createTable();
                    System.out.println("Processing...Done! Tables are created");
                    break;
                case 2:
                    deleteTable();
                    System.out.println("Processing...Done! Tables are deleted");
                    break;
                case 3:
                    insertData();
                    System.out.println("Processing...Data is loaded!");
                    break;
                case 5:
                    finish = true;
                    break;
                default:
                    System.out.println("[ERROR] Invalid input.\n\nPlease enter again.");
            }
        } while (!finish);
    }

    private static void createTable(){
        String Book = "CREATE TABLE IF NOT EXISTS Book"+
            "(ISBN char(13) CHECK (ISBN LIKE '[0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9]'),"+
            "title varchar(100) NOT NULL CHECK (author_name NOT LIKE'%[%_]%'),"+
            "unit_price integer NOT NULL CHECK (unit_price >= 0),"+
            "no_of_copies integer NOT NULL CHECK (no_of_copies >= 0),"+
            "PRIMARY KEY (ISBN))";

        String Customer = "CREATE TABLE IF NOT EXISTS Customer"+
            "(customer_id varchar(10) NOT NULL,"+
            "name varchar(50) NOT NULL CHECK (author_name NOT LIKE'%[%_]%'),"+
            "shipping_address varchar(200) NOT NULL CHECK (author_name NOT LIKE'%[%_]%'),"+
            "credit_card_no varchar(19) CHECK (credit_card_no LIKE '[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]'),"+
            "PRIMARY KEY (customer_id))";

        String Orders = "CREATE TABLE IF NOT EXISTS Orders"+
            "(order_id char(8) CHECK (order_id LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),"+
            "o_date Date,"+
            "shipping_status char(1) CHECK (shipping_status LIKE '[YN]'),"+
            "charge integer NOT NULL CHECK (charge >= 0),"+
            "customer_id varchar(10) NOT NULL,"+
            "PRIMARY KEY (order_id),"+
            "FOREIGN KEY (customer_id) REFERENCES Customer(customer_id))";

        String Ordering = "CREATE TABLE IF NOT EXISTS Ordering"+
            "(order_id char(8) CHECK (order_id LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),"+
            "ISBN char(13) CHECK (ISBN LIKE '[0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9]'),"+
            "quantity integer NOT NULL CHECK (quantity >= 0),"+
            "PRIMARY KEY (order_id, ISBN),"+
            "FOREIGN KEY (order_id) REFERENCES Orders(order_id),"+
            "FOREIGN KEY (ISBN) REFERENCES Book(ISBN))";

        String Book_author = "CREATE TABLE IF NOT EXISTS Book_author"+
            "(ISBN char(13) CHECK (ISBN LIKE '[0-9]-[0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]-[0-9]'),"+
            "author_name varchar(50) NOT NULL CHECK (author_name NOT LIKE'%[,%_]%'),"+
            "PRIMARY KEY (ISBN, author_name),"+
            "FOREIGN KEY (ISBN) REFERENCES Book(ISBN))";
        //create tables
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(Book);
            stmt.executeUpdate(Customer);
            stmt.executeUpdate(Orders);
            stmt.executeUpdate(Ordering);
            stmt.executeUpdate(Book_author);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteTable() {
        String Book = "DROP TABLE IF EXISTS Book";
        String Customer = "DROP TABLE IF EXISTS Customer";
        String Orders = "DROP TABLE IF EXISTS Orders";
        String Ordering = "DROP TABLE IF EXISTS Ordering";
        String Book_author = "DROP TABLE IF EXISTS Book_author";
    
        try {
            Statement stmt = conn.createStatement();
            // drop table
            stmt.executeUpdate(Book_author);
            stmt.executeUpdate(Ordering);
            stmt.executeUpdate(Orders);
            stmt.executeUpdate(Customer);
            stmt.executeUpdate(Book);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertData() {
        String[] files = {"book.txt", "customer.txt", "orders.txt", "ordering.txt", "book_author.txt"};
        System.out.println("Please enter the folder path");
        Scanner scanner = new Scanner(System.in);
        String folder_path = scanner.next();
        for (String i : files) {
            try {
                File file = new File(folder_path + "/" + i);
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()){
                    String line = myReader.nextLine();
                    String[] columns = line.split("|");
                    switch (i) {
                        case "book.txt":
                            try {
                                Statement stmt = conn.createStatement();
                                columns[0] = "'"+columns[0]+"'";
                                columns[1] = "'"+columns[1]+"'";
                                stmt.executeUpdate("INSERT into Book VALUES (" + String.join(",",columns ) + ")");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "customer.txt":
                            try {
                                Statement stmt = conn.createStatement();
                                columns[0] = "'"+columns[0]+"'";
                                columns[1] = "'"+columns[1]+"'";
                                columns[2] = "'"+columns[2]+"'";
                                columns[3] = "'"+columns[3]+"'";
                                stmt.executeUpdate("INSERT into Customer VALUES (" + String.join(",",columns) + ")");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "orders.txt":
                            try {
                                Statement stmt = conn.createStatement();
                                columns[0] = "'"+columns[0]+"'";
                                columns[1] = "TO_DATE('"+columns[1]+"', 'YYYY/MM/DD)";
                                columns[2] = "'"+columns[2]+"'";
                                columns[4] = "'"+columns[4]+"'";
                                stmt.executeUpdate("INSERT into Orders VALUES (" + String.join(",",columns) + ")");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "ordering.txt":
                            try {
                                columns[0] = "'"+columns[0]+"'";
                                columns[1] = "'"+columns[1]+"'";
                                Statement stmt = conn.createStatement();
                                stmt.executeUpdate("INSERT into Ordering VALUES (" + String.join(",",columns) + ")");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "book_author.txt":
                            try {
                                columns[0] = "'"+columns[0]+"'";
                                columns[1] = "'"+columns[1]+"'";
                                Statement stmt = conn.createStatement();
                                stmt.executeUpdate("INSERT into Book_author VALUES (" + String.join(",",columns) + ")");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                    }
                }
                myReader.close();
            }catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}


