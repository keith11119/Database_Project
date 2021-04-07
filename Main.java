//The main control of the program
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.*;




public class Main {

    public static Date latest_date;
    private static Scanner scanner = new Scanner(System.in);
    public static Connection connect() {
        String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db3";
        String dbUsername = "Group3";
        String dbPassword = "GWK3170";
        
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        }catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        }catch (SQLException e){
            System.out.println(e);
        }
        return conn;
    }
    public static void main(String[] args) throws IOException, SQLException, ParseException {
        boolean end = false;
        while(!end) {
            if (BookSystem.system_date == null){
                System.out.println("The System Date is now: 0000-00-00");
            }else {
                System.out.println("The System Date is now: "+BookSystem.system_date);
            }
            //if the system date has not yet set, print 0000-00-00 as a temporary system date
            System.out.println("<This is the Book Ordering System.>");
            System.out.println("---------------------------------------");
            System.out.println("1. System interface.");
            System.out.println("2. Customer interface.");
            System.out.println("3. Bookstore interface.");
            System.out.println("4. Show System Date.");
            System.out.print("5. Quit the system......\n\nPlease enter your choice??..");
            Scanner scanner_book_order_system = new Scanner(System.in);
            int identity = scanner_book_order_system.nextInt();
            
            switch (identity) {
                case 1:
                    BookSystem.init();
                    break;
                case 2:
                    Customer.init();
                    break;
                case 3:
                    Bookstore.init();
                    break;
                case 4:
                    if (BookSystem.system_date == null){
                        System.out.println("[Error] Please enter the system date first!");
                    }else {
                        System.out.println("The System Date is now: "+BookSystem.system_date);
                    }
                    break;
                case 5:
                    end = true;
                    break;
                default:
                    System.out.println("[Error] Invalid Input! Please input 1 - 5!");
            }
            //loop until valid input is made
        }  
            System.out.println("");
        
    }
    
   
}
