import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Bookstore {

    private static boolean quit = false;
    private static Connection conn = Main.connect();
    private static Scanner scanner = new Scanner (System.in);

    public static void init() {
        if (BookSystem.system_date == null) {
            System.out.println("[Error] Please enter the system date first!");
        } else {
            do{
                 // to ensure the input for bookstore interface is an integer
                boolean c = false;
                while (!c){
                    try{
                        System.out.println("<This is the bookstore interface.>");
                        System.out.println("----------------------------------");
                        System.out.println("1. Order Update.");
                        System.out.println("2. Order Query.");
                        System.out.println("3. N most Popular Book Query.");
                        System.out.println("4. Back to main menu.");
                        System.out.println("");
                        System.out.print("What is your choice??..");
                        Scanner scanner = new Scanner (System.in);
                        int action = scanner.nextInt();
                        while (action < 1 || action > 4){
                            System.out.println("[ERROR] Invalid input. Please enter a number between 1-4.");
                            action = scanner.nextInt();
                        }
                        c = true;
                        switch(action){
                            case 1:
                                OrderUpdate();
                                break;
                            case 2:
                                OrderQuery();
                                break;
                            case 3:
                                NPopular();
                                break;
                            case 4:
                                quit = true;
                                break;
                            default:
                                System.out.println("[ERROR] Invalid input. Please input a number between 1-4.");
                        }
                    } catch (Exception e) {
                        System.out.println("[ERROR] Invalid input. Please try again.");
                    } 
                }
            } while (!quit);
        }
    }

    private static void OrderUpdate(){
        int quan;
        char status, updateYN;
        String oid, inputYN;
        System.out.print("Please input the order ID: ");
        // check if the input order can be updated
        do{
            Scanner oidscanner = new Scanner (System.in);
            oid = oidscanner.next();
            while (!validID(oid)){
                // let users exit in case they do not know any valid order id
                if (oid.equals("EXIT")){
                    break;
                }
                System.out.println("[ERROR] Invalid Order ID. Please input the Order ID again or type \"EXIT\" to quit.");
                oid = oidscanner.next();
            }
            if (oid.equals("EXIT")){
                break;
            }
            // return the shipping status and quantity of the input order id
            status = ShippingStatus(oid);
            quan = OrderQuantity(oid);
            if (status == 'E' || quan == -1 ){
                oid = "EXIT";
                break;
            }
            System.out.println("the shipping status of " + oid + " is " + status + " and " + quan + " books ordered");
            if (!(status == 'N' && quan > 0))
                System.out.println("[ERROR] Update is not allowed for this order. Please input another Order ID.");
        } while (!(status == 'N' && quan > 0));
        
        if (!oid.equals("EXIT")){
            // update the shipping status as requested
            System.out.print("Are you sure to update the shipping status? (Yes=Y) ");
            do{
                Scanner YNscanner = new Scanner (System.in);
                inputYN = YNscanner.next();
                while (!(inputYN.length() == 1)){
                    System.out.print("Invalid Input. Please try again (Y or N): ");
                    inputYN = YNscanner.next();
                }
                updateYN = inputYN.charAt(1);
                if (!(updateYN == 'Y' || updateYN == 'N'))
                    System.out.println("[ERROR] Invalid input. Please input Y or N.");
            } while (!(updateYN == 'Y' || updateYN == 'N'));
        
            if (updateYN == 'Y'){
                updateShippingStatus(oid);
                System.out.println("Updated shipping status.");
            }
            else {
                System.out.println("Shipping status NOT updated. Return to Bookstore Interface");
            }
        }
    }
    
    private static void OrderQuery(){
        System.out.print("Please input the Month for Order Query (e.g.2005-09): ");
        Scanner datescanner = new Scanner (System.in);
        String InputDate = datescanner.next();
        while (!(InputDate.length() == 7)){
            System.out.print("Invalid Input. Please try again: ");
            InputDate = datescanner.next();
        }
        // find the input year and input month
        String[] parts = InputDate.split("-");
        int InputYear = Integer.parseInt(parts[0]);
        int InputMonth = Integer.parseInt(parts[1]);
        System.out.println("");
        //print all orders within the input month
        PrintOrderQuery(InputYear, InputMonth);
        
    }

    private static void NPopular(){
        System.out.print("Please input the N popular books number: ");
        Scanner nscanner = new Scanner (System.in);
        int n = nscanner.nextInt();
        while (n <= 0){
            System.out.print("[ERROR] Invalid input. Please input a number larger than 0.");
            n = nscanner.nextInt();
        }
        // find the sum of ordered quantity of each book
        ArrayList<Integer> ordered = new ArrayList<Integer>();
        try{
            String quanSQL = "SELECT SUM(quantity) FROM Ordering GROUP BY ISBN";
            Statement stmt = conn.createStatement();
            ResultSet quanList = stmt.executeQuery(quanSQL);
            while (quanList.next()){
                ordered.add(quanList.getInt(1));
            }
        } catch (SQLException se){
            se.printStackTrace();        
        } catch (Exception e){
            e.printStackTrace();
        }
        // sort the ordered quantites
        ArrayList<Integer> sortedOrdered = new ArrayList<Integer>(ordered);
        Collections.sort(sortedOrdered);
        Collections.reverse(sortedOrdered);
       
        // find the max number of books to be printed
        int nonzeros = 0;
        for (int i = 0; i < ordered.size(); i++){
            if (ordered.get(i) > 0)
                nonzeros++;
        }
        // find the actual number of books to be printed
        int fn = 0;
        if (n > nonzeros){
            fn = nonzeros-1;
        } else {
            fn = n-1;
        }
        // print the books with details
        System.out.println("ISBN            Title             copies");
        try{
            String popularSQL = "SELECT O.ISBN, B.title, SUM(O.quantity) FROM Ordering O, Book B WHERE O.ISBN=B.ISBN GROUP BY O.ISBN ORDER BY SUM(O.quantity) DESC";
            Statement stmt = conn.createStatement();
            ResultSet popularList = stmt.executeQuery(popularSQL);
            while (popularList.next()){
                if (popularList.getInt(3)>=sortedOrdered.get(fn)){
                    System.out.println(popularList.getString(1) + "   " + popularList.getString(2) + "   " + popularList.getInt(3));
                }
            }
        } catch (SQLException se){
            se.printStackTrace();        
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static boolean validID(String id){
        // check if the order id is in the table
        ArrayList<String> validList = new ArrayList <>();
        try{
            String validIDSQL = "SELECT * FROM Ordering WHERE order_id = \"" + id + "\"";
            Statement stmt = conn.createStatement();
            ResultSet valid = stmt.executeQuery(validIDSQL);
            while(valid.next()){
                String info = valid.getString("order_id");
                validList.add(info);
            }
            valid.close();
        } catch(SQLException se){
            se.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
        if (validList.contains(id))
            return true;
        else
            return false;
    }

    private static char ShippingStatus(String id){
        char s = 'Y';
        try{
            String ShippingStatusSQL = "SELECT * FROM Orders WHERE order_id = \"" + id + "\"";
            ShippingStatusSQL = String.format(ShippingStatusSQL, id);
            Statement stmt = conn.createStatement();
            ResultSet status = stmt.executeQuery(ShippingStatusSQL);
            status.next();
            s = status.getString("shipping_status").charAt(0);
        }  catch(SQLException se){
            System.out.println("Invalid Order ID for update due to invalid shipping status. Please try again.");
            s = 'E';
        } catch(Exception e){
            System.out.println("Invalid Order ID for update due to invalid shipping status. Please try again.");
            s = 'E';
        }
        return s;
    }

    private static int OrderQuantity(String id){
        int q = 0;
        try{
            String OrderQuantitySQL = "SELECT * FROM Ordering WHERE order_id = \"" + id + "\"";
            Statement stmt = conn.createStatement();
            ResultSet status = stmt.executeQuery(OrderQuantitySQL);
            status.next();
            q = status.getInt("quantity");
        } catch(SQLException se){
            System.out.println("Invalid Order ID for update due to invalid quantity. Please try again.");
            q = -1;
        } catch(Exception e){
            System.out.println("Invalid Order ID for update due to invalid quantity. Please try again.");
            q = -1;
        }
        return q;
    }

    private static void updateShippingStatus(String id){
        try{
            String updateSQL = "UPDATE Orders SET shipping_status = \'Y\' WHERE order_id = \"" + id + "\"";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(updateSQL);
        } catch(SQLException se){
            se.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void PrintOrderQuery(int year, int month){
        try{
            String QuerySQL = "SELECT * FROM Orders WHERE YEAR(o_date) = " + year + " AND MONTH(o_date) = " + month;
            Statement stmt = conn.createStatement();
            ResultSet QueryRecords = stmt.executeQuery(QuerySQL);
            int cnt = 1;
            int TotalCharge = 0;
            while (QueryRecords.next()){
                String orderID = QueryRecords.getString("order_id");
                String customerID = QueryRecords.getString("customer_id");
                String date = QueryRecords.getString("o_date");
                int charge = QueryRecords.getInt("charge");
                System.out.println("");
                System.out.println("Record : " + cnt);
                System.out.println("order_id : " + orderID);
                System.out.println("customer_id : " + customerID);
                System.out.println("date : " + date);
                System.out.println("charge : " + charge);
                System.out.println("");
                TotalCharge += charge;
                cnt += 1;
            }
            QueryRecords.close();
            System.out.println("");
            if (TotalCharge > 0){
                System.out.println("Total charge of the month is " + TotalCharge);
            } else {
                System.out.println("No record found for the input month.");
            }
            
        } catch (SQLException se){
            se.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

