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
    public static void main(String[] args) throws IOException, SQLException {
        boolean end = false;
        while(!end) {
	        System.out.println("The System Date is now: ");
            System.out.println("<This is the Book Ordering System.>");
            System.out.println("---------------------------------------");
            System.out.println("1. System interface.");
            System.out.println("2. Customer interface.");
            System.out.println("3. Bookstore interface.");
            System.out.println("4. Show System Date.");
            System.out.print("5. Quit the system......\n\nPlease enter your choice??..");
	        int identity = scanner.nextInt();
            switch (identity) {
                case 1:
                    BookSystem.init();
                    break;
                case 4:
                    System.out.println("Today is "+BookSystem.system_date);
                    
                    

                


                case 5:
                    end = true;
                    break;
            }
            System.out.println("");
        }
    }
    
   
}
