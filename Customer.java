import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Customer {
    private static boolean finish = false;
    private static Connection conn = Main.connect();
    public static void init() {
        if (BookSystem.system_date == null) {
            System.out.println("[Error] Please enter the system date first!");
        } else {
            do {
                boolean cus_in = false;   //Check whether user input integer for the customer interface.
                while (!cus_in) {
                    try {
                        System.out.println("<This is the customer interface.>");
                        System.out.println("-------------------------------");
                        System.out.println("1. Book Search.");
                        System.out.println("2. Order Creation.");
                        System.out.println("3. Order Altering.");
                        System.out.println("4. Order Query.");
                        System.out.print("5. Back to main menu.\n\nWhat is your choice??..");
                        Scanner scanner = new Scanner(System.in);
                        int choice = scanner.nextInt();
                        while (choice < 1 || choice > 5){
                            System.out.println("[ERROR]: Input is invalid.\n\nPlease enter again.");
                            choice = scanner.nextInt();
                        }
                        cus_in = true;
                        switch (choice) {
                            case 1:
                                bookSearch();
                                break;
                            case 2:
                                orderCreation();
                                break;
                            case 3:
                                orderAltering();
                                break;
                            case 4:
                                orderQuery();
                                break;
                            case 5:
                                finish = true;
                                break;
                            default:
                                System.out.println("[ERROR] Invalid input.\n\nPlease enter again.");
                        }
                    } catch (Exception e) {
                        System.out.println("[Error] Invalid input. Please input 1 - 5!\n");
                    }
                }
                System.out.println("");
            } while (!finish);
        }
    }

    private static void bookSearch(){
        boolean end_book_search = false;   //Check whether the user input is between 1 to 4
        while (!end_book_search) {
            boolean correct_book_search = false;   //Check whether user input integer in book search
            while (!correct_book_search) {
                try {
                    System.out.println("What do you want to search??");
                    System.out.println("1. ISBN");
                    System.out.println("2. Book Title");
                    System.out.println("3. Author Name");
                    System.out.print("4. Exit\nYour choice?...");
                    Scanner scanner = new Scanner(System.in);
                    int choice = scanner.nextInt();
                    correct_book_search = true;
                    while (choice < 1 || choice > 4){
                        System.out.println("[ERROR]: Input is invalid.\n\nPlease enter again.");  //Make sure the input is betwwen 1 & 4
                        choice = scanner.nextInt();
                    }
                    switch (choice){
                        case 1:
                        boolean end1 = false;   //Make sure the ISBN is valid
                        int count = 1;
                        while (!end1) {
                            try {
                                System.out.print("Input the ISBN: ");
                                Scanner scan_isbn = new Scanner(System.in);
                                String isbn = scan_isbn.next();
                                Statement stmt = conn.createStatement();
                                String cquery = "SELECT count(*) FROM Book WHERE ISBN = \"" + isbn + "\"";
                                ResultSet crs = stmt.executeQuery(cquery);
                                crs.next();
                                int count_exist = crs.getInt(1);  //count_exist = count whether there is data that matches the search criteria
                                if (count_exist > 0) {
                                    end1 = true;
                                    String query = "SELECT * FROM Book WHERE ISBN = \"" + isbn + "\" ORDER BY title, ISBN";
                                    ResultSet rs = stmt.executeQuery(query);
                                    while (rs.next()){
                                        System.out.println("\nRecord "+ count);
                                        count++;
                                        String isbn_q = rs.getString("ISBN");
                                        String title_q = rs.getString("title");
                                        int unit_price_q = rs.getInt("unit_price");
                                        int no_of_copies_q = rs.getInt("no_of_copies");
                                        System.out.println("ISBN: " + isbn_q);
                                        System.out.println("Book Title:" + title_q);
                                        System.out.println("Unit Price:" + unit_price_q);
                                        System.out.println("No Of Available:" + no_of_copies_q);
                                        String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn + "\"";
                                        System.out.println("Authors:");
                                        ResultSet ars = stmt.executeQuery(aquery);
                                        int count_author = 1;   //count_author = assign number for authors
                                        while (ars.next()){
                                            String author_q = ars.getString("author_name");
                                            System.out.println(count_author+" :"+author_q);
                                            count_author++;
                                        }
                                    }
                                    System.out.println("");
                                } else {
                                System.out.println("cannot query the book\n");
                                }
                            } catch (SQLException e){
                                System.out.println(e.getMessage());
                            }
                        } break;
                        
                        case 2:
                        boolean end2 = false;  //Make sure the Book Title is valid
                        count = 1;
                        while (!end2) {
                            System.out.print("Input the Book Title: ");
                            Scanner scan_title = new Scanner(System.in);
                            String title = scan_title.next();
                            Statement stmt = conn.createStatement();
                            if (title.charAt(0) != '%' && title.charAt(0) != '_'){  //The first letter is not _ or %
                                if (title.charAt(title.length() - 1) != '%' && title.charAt(title.length() - 1) != '_'){  //Base Case (No % and _)
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title = \"" + title + "\"";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title = \"" + title + "\" ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                } else if (title.charAt(title.length() - 1) == '_') {  // The last letter is _
                                    title = title.substring(0, title.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title Like \'" + title + "_\'";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'" + title + "_\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }

                                } else if (title.charAt(title.length() - 1) == '%') {  // The last letter is %
                                    title = title.substring(0, title.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title = \"" + title + "\"";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title = \"" + title + "\" ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } 
                                    String cquery2 = "SELECT count(*) FROM Book WHERE BINARY title Like \'" + title + "_%\'";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'" + title + "_%\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                }

                            } else if (title.charAt(0) == '_') {   //The first letter is _
                                title = title.substring(1, title.length());
                                if (title.charAt(title.length() - 1) != '_' && title.charAt(title.length() - 1) != '%') {  //The last letter is not _ nor %
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title Like \'_" + title + "\'";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_" + title + "\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                    

                                } else if (title.charAt(title.length() - 1) == '_') {   //Both first and last letter are _
                                    title = title.substring(0, title.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title Like \'_" + title + "_\'";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_" + title + "_\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }

                                } else if (title.charAt(title.length() - 1) == '%'){    //The first letter is _ while the last letter is %
                                    title = title.substring(0, title.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title Like \'_" + title + "\'";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_" + title + "\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } 


                                    String cquery2 = "SELECT count(*) FROM Book WHERE BINARY title Like \'_" + title + "_%\'";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_" + title + "_%\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                }                 
                                
                            } else if (title.charAt(0) == '%'){  //The first letter is %
                                title = title.substring(1, title.length());
                                if (title.charAt(title.length() - 1) != '_' && title.charAt(title.length() - 1) != '%') {  //The last letter is not _ nor %
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title = \"" + title + "\"";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title = \"" + title + "\" ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } 
                                    String cquery2 = "SELECT count(*) FROM Book WHERE BINARY title Like \'_%" + title + "\'";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_%" + title + "\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }

                                } else if (title.charAt(title.length() - 1) == '_') {   //The first letter is % while the last letter is _
                                    title = title.substring(0, title.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title LIKE \'" + title + "_\'";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title LIKE \'" + title + "_\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    }
                                    String cquery2 = "SELECT count(*) FROM Book WHERE BINARY title Like \'_%" + title + "_\'";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_%" + title + "_\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }

                                } else if (title.charAt(title.length() - 1) == '%'){    //Both first and last letter are %
                                    title = title.substring(0, title.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE BINARY title = \"" + title + "\"";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title = \"" + title + "\" ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    }
                                    String cquery2 = "SELECT count(*) FROM Book WHERE BINARY title Like \'" + title + "_%\'";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'" + title + "_%\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    } 
                                    String cquery3 = "SELECT count(*) FROM Book WHERE BINARY title Like \'_%" + title + "\'";
                                    ResultSet crs3 = stmt.executeQuery(cquery3);
                                    crs3.next();
                                    int count_exist3 = crs3.getInt(1);
                                    if (count_exist3 > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_%" + title + "\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    }
                                    String cquery4 = "SELECT count(*) FROM Book WHERE BINARY title Like \'_%" + title + "_%\'";
                                    ResultSet crs4 = stmt.executeQuery(cquery4);
                                    crs4.next();
                                    int count_exist4 = crs4.getInt(1);
                                    if (count_exist4 > 0) {
                                        end2 = true;
                                        String query = "SELECT * FROM Book WHERE BINARY title Like \'_%" + title + "_%\' ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            System.out.println();   
                                        }
                                    }                                  
                                    else {
                                        System.out.println("cannot query the book");
                                    }

                                }
                            }
                        } break;
                        
                        case 3:
                        boolean end3 = false;  //Make sure the Author Name is valid
                        count = 1;
                        while (!end3) {
                            System.out.print("Input the Author Name: ");
                            Scanner scan_author = new Scanner(System.in);
                            String author = scan_author.next();
                            Statement stmt = conn.createStatement();
                            if (author.charAt(0) != '%' && author.charAt(0) != '_') {
                                if (author.charAt(author.length() - 1) != '%' && author.charAt(author.length() - 1) != '_') {
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\")";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\") ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                } else if (author.charAt(author.length() - 1) == '_') {
                                    author = author.substring(0, author.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_\')";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }

                                } else if (author.charAt(author.length() - 1) == '%') {
                                    author = author.substring(0, author.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\")";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\") ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    }

                                    String cquery2 = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_%\')";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_%\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                }
                                    

                            } else if (author.charAt(0) == '_') {
                                author = author.substring(1, author.length());
                                if (author.charAt(author.length() - 1) != '%' && author.charAt(author.length() - 1) != '_') {
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "\')";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                } else if (author.charAt(author.length() - 1) == '_') {
                                    author = author.substring(0, author.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "_\')";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "_\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }

                                } else if (author.charAt(author.length() - 1) == '%') {
                                    author = author.substring(0, author.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "\')";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    }


                                    String cquery2 = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "_%\')";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_" + author + "_%\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                }
                                     
                            } else if (author.charAt(0) == '%') {
                                author = author.substring(1, author.length());
                                if (author.charAt(author.length() - 1) != '%' && author.charAt(author.length() - 1) != '_') {
                                    
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\")";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\") ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    }

                                    String cquery2 = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "\')";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }


                                } else if (author.charAt(author.length() - 1) == '_') {
                                    author = author.substring(0, author.length()-1);
                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_\')";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    }


                                    String cquery2 = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "_\')";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "_\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }




                                } else if (author.charAt(author.length() - 1) == '%') {
                                    author = author.substring(0, author.length()-1);

                                    String cquery = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\")";
                                    ResultSet crs = stmt.executeQuery(cquery);
                                    crs.next();
                                    int count_exist = crs.getInt(1);
                                    if (count_exist > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name = \"" + author + "\") ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    }

                                    String cquery2 = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_%\')";
                                    ResultSet crs2 = stmt.executeQuery(cquery2);
                                    crs2.next();
                                    int count_exist2 = crs2.getInt(1);
                                    if (count_exist2 > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'" + author + "_%\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    }

                                    String cquery3 = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "_%\')";
                                    ResultSet crs3 = stmt.executeQuery(cquery3);
                                    crs3.next();
                                    int count_exist3 = crs3.getInt(1);
                                    if (count_exist3 > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "_%\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name";
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    }


                                    String cquery4 = "SELECT count(*) FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "_%\')";
                                    ResultSet crs4 = stmt.executeQuery(cquery4);
                                    crs4.next();
                                    int count_exist4 = crs4.getInt(1);
                                    if (count_exist4 > 0) {
                                        end3 = true;
                                        String query = "SELECT * FROM Book WHERE ISBN IN (SELECT ISBN FROM Book_author WHERE BINARY author_name Like \'_%" + author + "_%\') ORDER BY title, ISBN";
                                        ResultSet rs = stmt.executeQuery(query);
                                        //System.out.println("\nRecord "+ count);
                                        while (rs.next()){
                                            System.out.println("Record " + count);
                                            count++;
                                            String isbn_q = rs.getString("ISBN");
                                            String title_q = rs.getString("title");
                                            int unit_price_q = rs.getInt("unit_price");
                                            int no_of_copies_q = rs.getInt("no_of_copies");
                                            System.out.println("ISBN: " + isbn_q);
                                            System.out.println("Book Title:" + title_q);
                                            System.out.println("Unit Price:" + unit_price_q);
                                            System.out.println("No Of Available:" + no_of_copies_q);
                                            
                                            Statement stmta = conn.createStatement();  //stmta = a new statement with query for book_author
                                            String aquery = "SELECT author_name FROM Book_author WHERE ISBN = \"" + isbn_q + "\" ORDER BY author_name" ;
                                            System.out.println("Authors:");
                                            ResultSet ars = stmta.executeQuery(aquery);
                                            int count_author = 1;
                                            while (ars.next()){
                                                String author_q = ars.getString("author_name");
                                                System.out.println(count_author+" :"+author_q);
                                                count_author++;
                                            }
                                            
                                            System.out.println();   
                                        }
                                    } else {
                                        System.out.println("cannot query the book");
                                    }
                                    
                                }
                            }
                        } break;

                        case 4:
                        end_book_search = true;
                        break;
                        default:
                        System.out.println("[ERROR] Invalid input.\n\nPlease enter again.\n");
                    }
                } catch (Exception e) {
                    System.out.println("[Error] Invalid input. Please input 1-4!\n");
                }
            }
            
        }
    }
    private static void orderCreation(){
        List<String> c_id_list = new ArrayList<>();
        try {
            Statement stmt_c_id = conn.createStatement();
            String query = "SELECT customer_id FROM Customer";
            ResultSet rs_c_id = stmt_c_id.executeQuery(query);
            while (rs_c_id.next()){
                c_id_list.add(rs_c_id.getString("customer_id"));
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        List<String> isbn_list = new ArrayList<>();
        try {
            Statement stmt_isbn = conn.createStatement();
            String query = "SELECT ISBN FROM Book";
            ResultSet rs_isbn = stmt_isbn.executeQuery(query);
            while (rs_isbn.next()){
                isbn_list.add(rs_isbn.getString("ISBN"));
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String old_order_id = "00000000";
        String new_order_id = old_order_id;
        try {
            Statement stmt_order_id = conn.createStatement();
            String query = "SELECT Max(order_id) FROM Ordering";
            ResultSet rs_order_id = stmt_order_id.executeQuery(query);
            rs_order_id.next();
            old_order_id = rs_order_id.getString(1); 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        try {
            int old_o_id = Integer.parseInt(old_order_id);
            int new_o_id = old_o_id+1;
            int n_digits = 0;
            int tmp = new_o_id;
            while (tmp!=0) {
                tmp/=10;
                n_digits++;
            }
            new_order_id = Integer.toString(new_o_id);
            for (int i = 0; i < 8 - n_digits; i++) {
                new_order_id = "0"+new_order_id;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int no_of_copies = 0;
        int unit_price = 0;
        int order_quantity = 0;
        int total_quantity = 0;
        int total_charge = 0;
        System.out.print("Please input your customer ID??");
        Scanner scanner = new Scanner(System.in);
        String c_id = scanner.next();
        if (c_id_list.contains(c_id)) {
            boolean end_order_creation = false;
            System.out.println(">> What books do you want to order??");
            System.out.println(">> Input ISBN and then the quantity.");
            System.out.println(">> You can press \"L\" to see ordered list, or \"F\" to finish ordering.");
            boolean sus_order = false;   //Check if there is a successful order
            while (!end_order_creation){
                System.out.print("Please enter the book's ISBN: ");
                Scanner scan_order_isbn = new Scanner(System.in);
                String order_isbn = scan_order_isbn.next();
                if (order_isbn.equals("F")) {
                    end_order_creation = true;
                    if (sus_order == true){
                        try {
                            Statement stmt_create_orders = conn.createStatement();
                            stmt_create_orders.executeUpdate("INSERT into Orders VALUES (\""+ new_order_id+ "\", \"" + BookSystem.system_date + "\", 'N', " + (total_charge + 10) +", \"" + c_id + "\")");                            
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (order_isbn.equals("L")) {
                    System.out.println("ISBN          Number:");
                    if (sus_order == true) {
                        try {
                            Statement stmt_tmp_check = conn.createStatement();
                            String query = "SELECT * FROM Ordering WHERE order_id = \"" + new_order_id + "\"";
                            ResultSet rs_tmp_check = stmt_tmp_check.executeQuery(query);
                            while (rs_tmp_check.next()) {
                                String tmp_isbn = rs_tmp_check.getString("ISBN");
                                String tmp_number = rs_tmp_check.getString("quantity");
                                System.out.println(tmp_isbn+ "   "+tmp_number);
                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                        
                    }
  
                } else if (isbn_list.contains(order_isbn)) {
                    boolean correct_order_quantity = false;
                    while (!correct_order_quantity) {
                        try {
                            System.out.print("Please enter the quantity of the order: ");
                            Scanner scan_order_quantity = new Scanner(System.in);
                            order_quantity = scan_order_quantity.nextInt();
                            if (order_quantity > 0) {
                                correct_order_quantity = true;
                                total_quantity = total_quantity + order_quantity;
                                try {
                                    Statement stmt_no_of_copies = conn.createStatement();
                                    String query = "SELECT no_of_copies FROM Book WHERE ISBN = \""+order_isbn+"\"";
                                    ResultSet rs_no_of_copies = stmt_no_of_copies.executeQuery(query);
                                    rs_no_of_copies.next();
                                    no_of_copies = rs_no_of_copies.getInt(1);
                                } catch (SQLException e) {
                                    System.out.println(e.getMessage());
                                }
                                try {
                                    Statement stmt_unit_price = conn.createStatement();
                                    String query = "SELECT unit_price FROM Book WHERE ISBN = \""+order_isbn+"\"";
                                    ResultSet rs_unit_price = stmt_unit_price.executeQuery(query);
                                    rs_unit_price.next();
                                    unit_price = rs_unit_price.getInt(1);
                                } catch (SQLException e) {
                                    System.out.println(e.getMessage());
                                }

                                
                                if (no_of_copies >= order_quantity) {
                                    try {
                                        Statement stmt_update_quantity = conn.createStatement();
                                        stmt_update_quantity.executeUpdate("UPDATE Book SET no_of_copies = " + (no_of_copies - order_quantity) + " WHERE ISBN = \"" + order_isbn + "\""); 
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Statement stmt_create_ordering = conn.createStatement();
                                        stmt_create_ordering.executeUpdate("INSERT INTO Ordering VALUES (\"" + new_order_id + "\", \"" + order_isbn + "\", "  + order_quantity + ")");                            
                                        sus_order = true;
                                        total_charge = total_charge + (unit_price + 10) * order_quantity; 
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    System.out.println("There is not enough inventory for this book! You can at most order "+ no_of_copies + ".");
                                }
                            }
                            else {
                                System.out.println("[Error] You should input positive integer!");
                            }
                        } catch (Exception e) {
                            System.out.println("[Error] Please enter integer! (e.g. 3)");
                        }
                    }

                    
                        
                } else {
                    System.out.println("[Error], Invalid input!\n");
                }
            }
        } else {
            System.out.println("The Customer ID doesn't exist");
        }
    
    }
    
    private static void orderAltering(){
        System.out.print("Please enter the OrderID that you want to change: ");
        Scanner scanner_order_id = new Scanner(System.in);
        String o_id = scanner_order_id.next();
        List<String> o_id_list = new ArrayList<>();
        try {
            Statement stmt_o_id = conn.createStatement();
            String query = "SELECT * FROM Orders";
            ResultSet rs_o_id = stmt_o_id.executeQuery(query);
            while (rs_o_id.next()){
                o_id_list.add(rs_o_id.getString("order_id"));
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (o_id_list.contains(o_id)) {
            String orders_ship_status = "N";
            int orders_charge = 0;
            try {
                Statement stmt_orders_check = conn.createStatement();
                String query = "SELECT * FROM Orders WHERE order_id = \"" + o_id+ "\"";
                ResultSet rs_orders_check = stmt_orders_check.executeQuery(query);
                while (rs_orders_check.next()) {
                    orders_ship_status = rs_orders_check.getString("shipping_status");
                    orders_charge = rs_orders_check.getInt("charge");
                    String orders_c_id = rs_orders_check.getString("customer_id");
                    System.out.println("order_id:"+o_id+"  shipping:"+orders_ship_status+"  charge="+orders_charge+"  customerID="+orders_c_id);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            List<String> ordering_isbn_list = new ArrayList<>();
            try {
                int tmp_count = 1;
                Statement stmt_ordering_check = conn.createStatement();
                String query = "SELECT * FROM Ordering WHERE order_id = \"" + o_id+ "\"";
                ResultSet rs_ordering_check = stmt_ordering_check.executeQuery(query);
                while (rs_ordering_check.next()) {
                    String ordering_isbn = rs_ordering_check.getString("ISBN");
                    int ordering_quantity = rs_ordering_check.getInt("quantity");
                    System.out.println("book no: "+ tmp_count+ " ISBN = "+ordering_isbn+" quantity = "+ordering_quantity);
                    ordering_isbn_list.add(ordering_isbn);
                    tmp_count++;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            boolean correct_alter_no = false;
            while (!correct_alter_no) {
                try {
                    System.out.println("Whcih book you want to alter (input book no.):");
                    Scanner scanner_alter_no = new Scanner(System.in);
                    int alter_no = scanner_alter_no.nextInt();
                    if (alter_no > 0) {
                        correct_alter_no = true;
                        if (alter_no <= ordering_isbn_list.size()) {
                            System.out.println("input add or remove");
                            Scanner scanner_add_remove = new Scanner(System.in);
                            String add_remove = scanner_add_remove.next();
                            if (add_remove.equals("add")) {
                                if (orders_ship_status.equals("Y")){
                                    System.out.println("The books in the order are shipped");
                                } else {
                                    boolean correct_add_no = false;
                                    while (!correct_add_no) {
                                        try {
                                            System.out.print("Input the number: ");
                                            Scanner scanner_add = new Scanner(System.in);
                                            int add_no = scanner_add.nextInt();
                                            if (add_no > 0) {
                                                correct_add_no = true;
                                                int tmp_quantity = 0;
                                                int tmp_no_of_copies = 0;
                                                int tmp_unit_price = 0;
                                                try {
                                                    Statement stmt_alter_quantity = conn.createStatement();
                                                    String query = "SELECT quantity FROM Ordering WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1)+ "\" AND order_id = \"" +o_id + "\"";
                                                    ResultSet rs_alter_quantity = stmt_alter_quantity.executeQuery(query);
                                                    rs_alter_quantity.next();
                                                    tmp_quantity = rs_alter_quantity.getInt(1);
                                                } catch (SQLException e) {
                                                    System.out.println(e.getMessage());
                                                }
                                                try {
                                                    Statement stmt_alter_no_of_copies = conn.createStatement();
                                                    String query = "SELECT no_of_copies FROM Book WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1)+ "\"";
                                                    ResultSet rs_alter_no_of_copies = stmt_alter_no_of_copies.executeQuery(query);
                                                    rs_alter_no_of_copies.next();
                                                    tmp_no_of_copies = rs_alter_no_of_copies.getInt(1);
                                                } catch (SQLException e) {
                                                    System.out.println(e.getMessage());
                                                }
                                                try {
                                                    Statement stmt_alter_unit_price = conn.createStatement();
                                                    String query = "SELECT unit_price FROM Book WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1)+ "\"";
                                                    ResultSet rs_alter_unit_price = stmt_alter_unit_price.executeQuery(query);
                                                    rs_alter_unit_price.next();
                                                    tmp_unit_price = rs_alter_unit_price.getInt(1);
                                                } catch (SQLException e) {
                                                    System.out.println(e.getMessage());
                                                }
                                                if (add_no <= tmp_no_of_copies) {
                                                    try {
                                                        Statement stmt_alter_quantity = conn.createStatement();
                                                        stmt_alter_quantity.executeUpdate("UPDATE Ordering SET quantity = " + (tmp_quantity+add_no) + " WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1) + "\" AND order_id =\"" + o_id + "\""); 
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    System.out.println("Update is ok!");
                                                    try {
                                                        Statement stmt_alter_no_of_copies = conn.createStatement();
                                                        stmt_alter_no_of_copies.executeUpdate("UPDATE Book SET no_of_copies = " + (tmp_no_of_copies - add_no) + " WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1) + "\""); 
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    System.out.println("update done!!");
                                                    try {
                                                        Statement stmt_alter_charge = conn.createStatement();
                                                        stmt_alter_charge.executeUpdate("UPDATE Orders SET charge = " + (add_no * (tmp_unit_price + 10) + orders_charge) + " WHERE order_id = \"" + o_id + "\""); 
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    System.out.println("updated charge");
                                                    try {
                                                        Statement stmt_alter_date = conn.createStatement();
                                                        stmt_alter_date.executeUpdate("UPDATE Orders SET o_date = \"" + BookSystem.system_date + "\" WHERE order_id = \"" + o_id + "\""); 
                                                    } catch (SQLException e) {
                                                        //System.out.println("[Error] System date is missing!");
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        Statement stmt_orders_check_new = conn.createStatement();
                                                        String query = "SELECT * FROM Orders WHERE order_id = \"" + o_id+ "\"";
                                                        ResultSet rs_orders_check_new = stmt_orders_check_new.executeQuery(query);
                                                        while (rs_orders_check_new.next()) {
                                                            String orders_ship_status_new = rs_orders_check_new.getString("shipping_status");
                                                            int orders_charge_new = rs_orders_check_new.getInt("charge");
                                                            String orders_c_id_new = rs_orders_check_new.getString("customer_id");
                                                            System.out.println("order_id:"+o_id+"  shipping:"+orders_ship_status_new+"  charge="+orders_charge_new+"  customerID="+orders_c_id_new);
                                                        }
                                                    } catch (SQLException e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                    try {
                                                        int tmp_count_new = 1;
                                                        Statement stmt_ordering_check_new = conn.createStatement();
                                                        String query = "SELECT * FROM Ordering WHERE order_id = \"" + o_id+ "\"";
                                                        ResultSet rs_ordering_check_new = stmt_ordering_check_new.executeQuery(query);
                                                        while (rs_ordering_check_new.next()) {
                                                            String ordering_isbn_new = rs_ordering_check_new.getString("ISBN");
                                                            int ordering_quantity_new = rs_ordering_check_new.getInt("quantity");
                                                            System.out.println("book no: "+ tmp_count_new+ " ISBN = "+ordering_isbn_new+" quantity = "+ordering_quantity_new);
                                                            tmp_count_new++;
                                                        }
                                                    } catch (SQLException e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                } else {
                                                    System.out.println("There is not enough copies available!\n");
                                                }
                                            } else {
                                                System.out.println("[Error] You should input positive integer!");
                                            }
                                            
                                        } catch (Exception e) {
                                            System.out.println("[Error] Please input an integer! (e.g. 1)");
                                        }
                                    }
    
                                    
                                }
                            } else if (add_remove.equals("remove")) {
                                if (orders_ship_status.equals("Y")){
                                    System.out.println("The books in the order are shipped");
                                 
                                    
                                }else {
                                    int tmp_quantity = 0;
                                    try {
                                        Statement stmt_alter_quantity = conn.createStatement();
                                        String query = "SELECT quantity FROM Ordering WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1)+ "\" AND order_id = \"" +o_id + "\"";
                                        ResultSet rs_alter_quantity = stmt_alter_quantity.executeQuery(query);
                                        rs_alter_quantity.next();
                                        tmp_quantity = rs_alter_quantity.getInt(1);
                                    } catch (SQLException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    if (tmp_quantity <= 0) {
                                        System.out.println("The quantity that you ordered for this book is 0 already!");
                                    } else {
                                        boolean correct_remove_no = false;
                                        while (!correct_remove_no) {
                                            try {
                                                System.out.print("Input the number: ");
                                                Scanner scanner_remove = new Scanner(System.in);
                                                int remove_no = scanner_remove.nextInt();
                                                if (remove_no > 0) {
                                                    correct_remove_no = true;
                                                    int tmp_no_of_copies = 0;
                                                    int tmp_unit_price = 0;
                                                    
                                                    try {
                                                        Statement stmt_alter_no_of_copies = conn.createStatement();
                                                        String query = "SELECT no_of_copies FROM Book WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1)+ "\"";
                                                        ResultSet rs_alter_no_of_copies = stmt_alter_no_of_copies.executeQuery(query);
                                                        rs_alter_no_of_copies.next();
                                                        tmp_no_of_copies = rs_alter_no_of_copies.getInt(1);
                                                    } catch (SQLException e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                    try {
                                                        Statement stmt_alter_unit_price = conn.createStatement();
                                                        String query = "SELECT unit_price FROM Book WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1)+ "\"";
                                                        ResultSet rs_alter_unit_price = stmt_alter_unit_price.executeQuery(query);
                                                        rs_alter_unit_price.next();
                                                        tmp_unit_price = rs_alter_unit_price.getInt(1);
                                                    } catch (SQLException e) {
                                                        System.out.println(e.getMessage());
                                                    }
                                                    if (remove_no <= tmp_quantity) {
                                                        try {
                                                            Statement stmt_alter_quantity = conn.createStatement();
                                                            stmt_alter_quantity.executeUpdate("UPDATE Ordering SET quantity = " + (tmp_quantity-remove_no) + " WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1) + "\" AND order_id =\"" + o_id + "\""); 
                                                        } catch (SQLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        System.out.println("Update is ok!");
                                                        try {
                                                            Statement stmt_alter_no_of_copies = conn.createStatement();
                                                            stmt_alter_no_of_copies.executeUpdate("UPDATE Book SET no_of_copies = " + (tmp_no_of_copies + remove_no) + " WHERE ISBN = \"" + ordering_isbn_list.get(alter_no - 1) + "\""); 
                                                        } catch (SQLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        System.out.println("update done!!");
                                                        if (remove_no == tmp_quantity) {
                                                            try {
                                                                Statement stmt_alter_charge_all = conn.createStatement();
                                                                stmt_alter_charge_all.executeUpdate("UPDATE Orders SET charge = 0 WHERE order_id = \"" + o_id + "\""); 
                                                            } catch (SQLException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            try {
                                                                Statement stmt_alter_charge = conn.createStatement();
                                                                stmt_alter_charge.executeUpdate("UPDATE Orders SET charge = " + (orders_charge - remove_no * (tmp_unit_price + 10)) + " WHERE order_id = \"" + o_id + "\""); 
                                                            } catch (SQLException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        System.out.println("updated charge");
                                                        try {
                                                            Statement stmt_alter_date = conn.createStatement();
                                                            stmt_alter_date.executeUpdate("UPDATE Orders SET o_date = \"" + BookSystem.system_date + "\" WHERE order_id = \"" + o_id + "\""); 
                                                        } catch (SQLException e) {
                                                            //System.out.println("[Error] System date is missing!");
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            Statement stmt_orders_check_new = conn.createStatement();
                                                            String query = "SELECT * FROM Orders WHERE order_id = \"" + o_id+ "\"";
                                                            ResultSet rs_orders_check_new = stmt_orders_check_new.executeQuery(query);
                                                            while (rs_orders_check_new.next()) {
                                                                String orders_ship_status_new = rs_orders_check_new.getString("shipping_status");
                                                                int orders_charge_new = rs_orders_check_new.getInt("charge");
                                                                String orders_c_id_new = rs_orders_check_new.getString("customer_id");
                                                                System.out.println("order_id:"+o_id+"  shipping:"+orders_ship_status_new+"  charge="+orders_charge_new+"  customerID="+orders_c_id_new);
                                                            }
                                                        } catch (SQLException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                        try {
                                                            int tmp_count_new = 1;
                                                            Statement stmt_ordering_check_new = conn.createStatement();
                                                            String query = "SELECT * FROM Ordering WHERE order_id = \"" + o_id+ "\"";
                                                            ResultSet rs_ordering_check_new = stmt_ordering_check_new.executeQuery(query);
                                                            while (rs_ordering_check_new.next()) {
                                                                String ordering_isbn_new = rs_ordering_check_new.getString("ISBN");
                                                                int ordering_quantity_new = rs_ordering_check_new.getInt("quantity");
                                                                System.out.println("book no: "+ tmp_count_new+ " ISBN = "+ordering_isbn_new+" quantity = "+ordering_quantity_new);
                                                                tmp_count_new++;
                                                            }
                                                        } catch (SQLException e) {
                                                            System.out.println(e.getMessage());
                                                        }
                                                    } else {
                                                        System.out.println("The number you inputted is too large!\n");
                                                    }
                                                } else {
                                                    System.out.println("[Error] You should input positive integers!");
                                                }
                                            } catch (Exception e) {
                                                System.out.println("[Error] Please input an integer! (e.g. 1)");
                                            }
                                        }

                                    }
                                    
                                }
                            } else {
                                System.out.println("[Error] Invalid input!\n");
                            }
                        } else {
                            System.out.println("[Error] The book no. is invalid!\n");
                        }
                    } else {
                        System.out.println("[Error] Please input positive integers!");
                    }
                } catch (Exception e) {
                    System.out.println("[Error] Please input an integer! (e.g. 1)");
                }   
            }    
        }
        else {
            System.out.println("[Error] The Order ID can not be found!\n");
        }
    }
    
    private static void orderQuery(){
        System.out.print("Please Input Customer ID: ");
        Scanner scanner_customer_id = new Scanner(System.in);
        String customer_id = scanner_customer_id.next();
        List<String> c_id_list = new ArrayList<>();
        try {
            Statement stmt_c_id = conn.createStatement();
            String query = "SELECT customer_id FROM Customer";
            ResultSet rs_c_id = stmt_c_id.executeQuery(query);
            while (rs_c_id.next()){
                c_id_list.add(rs_c_id.getString("customer_id"));
            } 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        if (c_id_list.contains(customer_id)) {
            boolean correct_year = false;
            while (!correct_year) {
                try {
                    System.out.print("Please Input the Year: ");
                    Scanner scanner_year = new Scanner(System.in);
                    int year = scanner_year.nextInt();
                    correct_year = true;
                    Statement count_stmt_orders_query = conn.createStatement();
                    String cquery = "SELECT count(*) FROM Orders WHERE customer_id = \"" + customer_id+ "\" AND YEAR(o_date) = " + year;
                    ResultSet count_rs_orders_query = count_stmt_orders_query.executeQuery(cquery);
                    count_rs_orders_query.next();
                    int count_order_oid = count_rs_orders_query.getInt(1);
                    if (count_order_oid <= 0) {
                        System.out.println("No order from you in this year");
                    } else {
                        try {
                            int orders_count = 1;
                            Statement stmt_orders_query = conn.createStatement();
                            String query = "SELECT * FROM Orders WHERE customer_id = \"" + customer_id+ "\" AND YEAR(o_date) = " + year;
                            ResultSet rs_orders_query = stmt_orders_query.executeQuery(query);
                            while (rs_orders_query.next()) {
                                String orders_oid = rs_orders_query.getString("order_id");
                                java.sql.Date orders_date = rs_orders_query.getDate("o_date");
                                int orders_charge = rs_orders_query.getInt("charge");
                                String orders_ship_status = rs_orders_query.getString("shipping_status");
                                System.out.println("\nRecord : " + orders_count + "\nOrderID : " + orders_oid + "\nOrderDate : " + orders_date + "\ncharge : " + orders_charge + "\nshipping status : " + orders_ship_status);
                                orders_count++;
                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    
                } catch (Exception e) {
                    System.out.println("[Error] Please input year according to this format: YYYY! (e.g. 2010)");
                }
            }
        } else {
            System.out.println("[Error] The Customer ID does not exist!\n");
        }
    }
}
