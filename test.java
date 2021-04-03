import java.io.*;
import java.sql.*;
import java.util.Scanner;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class test {
    private static void insertData() {
        String[] files = {"book.txt", "customer.txt", "orders.txt", "ordering.txt", "book_author.txt"};
        System.out.println("Please enter the folder path");
        Scanner scanner = new Scanner(System.in);
        String folder_path = scanner.next();
        for (String i : files) {
            try {
                File file = new File(folder_path + "/" + i);
                Scanner myReader = new Scanner(file);
                while (myReader.hasNextLine()) {
                    String line = myReader.nextLine();
                    String[] columns = line.split("|");
                    for (int n =0; 0< columns.length; n++){
                        columns[n] = "'" + columns[n] + "'";
                    }
                    }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        insertData();
    }
}
