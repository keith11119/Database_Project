import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.util.Arrays;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookSystem {
    private static boolean finish = false;
    private static Connection conn = Main.connect();
    public static LocalDate system_date;
    public static void init() throws IOException, SQLException, ParseException {
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
                case 4:
                    setDate();
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
            "(ISBN char(13),"+
            "title varchar(100) NOT NULL,"+
            "unit_price integer,"+
            "no_of_copies integer,"+
            "PRIMARY KEY (ISBN))";

        String Customer = "CREATE TABLE IF NOT EXISTS Customer"+
            "(customer_id varchar(10) NOT NULL,"+
            "name varchar(50) NOT NULL,"+
            "shipping_address varchar(200) NOT NULL,"+
            "credit_card_no varchar(19),"+
            "PRIMARY KEY (customer_id))";

        String Ordering = "CREATE TABLE IF NOT EXISTS Ordering"+
            "(order_id char(8),"+
            "ISBN char(13),"+
            "quantity integer,"+
            "PRIMARY KEY (order_id, ISBN),"+
            "FOREIGN KEY (ISBN) REFERENCES Book(ISBN))";
        
        String Orders = "CREATE TABLE IF NOT EXISTS Orders"+
            "(order_id char(8),"+
            "o_date Date,"+
            "shipping_status char(1),"+
            "charge integer,"+
            "customer_id varchar(10) NOT NULL,"+
            "PRIMARY KEY (order_id),"+
            "FOREIGN KEY (order_id) REFERENCES Ordering(order_id),"+
            "FOREIGN KEY (customer_id) REFERENCES Customer(customer_id))";

        String Book_author = "CREATE TABLE IF NOT EXISTS Book_author"+
            "(ISBN char(13),"+
            "author_name varchar(50) NOT NULL,"+
            "PRIMARY KEY (ISBN, author_name),"+
            "FOREIGN KEY (ISBN) REFERENCES Book(ISBN))";
        //create tables
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(Book);
            stmt.executeUpdate(Customer);
            stmt.executeUpdate(Ordering);
            stmt.executeUpdate(Orders);
            stmt.executeUpdate(Book_author);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteTable() {
        String Book = "DROP TABLE IF EXISTS Book";
        String Customer = "DROP TABLE IF EXISTS Customer";
        String Ordering = "DROP TABLE IF EXISTS Ordering";
        String Orders = "DROP TABLE IF EXISTS Orders";
        String Book_author = "DROP TABLE IF EXISTS Book_author";
    
        try {
            Statement stmt = conn.createStatement();
            // drop table
            stmt.executeUpdate(Book_author);
            stmt.executeUpdate(Orders);
            stmt.executeUpdate(Ordering);
            stmt.executeUpdate(Customer);
            stmt.executeUpdate(Book);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertData() throws IOException {
        String[] files = {"book.txt", "customer.txt", "ordering.txt", "orders.txt", "book_author.txt"};
        System.out.println("Please enter the folder path");
        Scanner scanner = new Scanner(System.in);
        String folder_path = scanner.next();
        for (String i : files) {
            try {
                String file = folder_path + "/" + i;
                BufferedReader br = new BufferedReader(new FileReader(file));
                String CurrentLine;
                //read the file line by line
                while ((CurrentLine = br.readLine()) != null){
                    String[] columns = CurrentLine.split("\\|");
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
                            columns[1] = "STR_TO_DATE('"+columns[1]+"','%Y-%m-%d')";
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
            }catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    private static void setDate() {
        boolean end = false;
        boolean correct_state = false;
        while(!correct_state){
            try{
                System.out.print("Please Input the date (YYYYMMDD): ");
                Scanner scanner = new Scanner(System.in);
                String date = scanner.next();
                correct_state=true;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate date2 = LocalDate.parse(date, formatter);
                Date latest_date = null;
                try{
                    Statement stmt = conn.createStatement();
                    String query = "SELECT * FROM Orders ";
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next()){
                        latest_date = rs.getDate("o_date");
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                if (latest_date == null){
                    latest_date = Date.valueOf("1000-01-01");
                    System.out.println("[Error] The lastest date in orders is NULL ");
                }
                try {
                    latest_date.toLocalDate();
                    System.out.println("Latest date in orders: "+latest_date);
                    if(system_date == null && date2.isAfter(latest_date.toLocalDate())== true){
                        system_date =date2;
                        System.out.println("Today is "+system_date);
                    }
                    else if(date2.isAfter(system_date) == true && date2.isAfter(latest_date.toLocalDate()) == true ){
                        system_date =date2;
                        System.out.println("Today is "+system_date);
                    }  
                    else if (date2.isAfter(system_date) == false || date2.isAfter(latest_date.toLocalDate())==false){
                        System.out.println("[Error] Invalid Input! It is set backward!");
                        correct_state=false;
                        //handle the set backward error
                    }
                } catch (Exception e) {
                    System.out.println("[Error] Invalid Input! It is set backward!");


                        //TODO: handle exception
                }
                   
            }catch(Exception e){
                System.out.println("Incorrect format.");
                //handle the incorrect format error
            }

        }      

    }
}


