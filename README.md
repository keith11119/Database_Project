# Database_Project
CUHK 3170 Project Group 3
Wu Cheuk Kin 1155108909


Compilation: 
  compile the java file by command "javac --release 8 xxx.java" to generate the class file 
  xxx is the java file name and we are using Java version 1.8
  transfer all the java files, class files and testing data set to linux pc 
  run the program by command "java -cp .:mysql-connector-java-5.1.47.jar Main"
  
Running:
  build Tables and set up system date first before any actions (default System date is 0000-00-00)
  1. run the System interface 
  2. delete and build new tables
  3. insert data and input the data path 
  4. set the system date (system date has to be later than the latest order date and the original system date)
    -if the latest order date in data set is NULL, there is an error message "[Error] The lastest date in orders is NULL "
    -if the data format is not correct, there is an error message "Incorrect format."
    -if the data is set backward (earlier than the latest order date or the original system date), there is an error 
    message "[Error] Invalid Input! It is set backward!"


    Customer Interface:

Book Search 
a. Query by ISBN
You are required to input the ISBN of the book. Exact match is required, otherwise nothing can be searched. This function will return "Book Title", "ISBN", "Unit Price", "No. of Copies Available" and "A List of Authors" 
b. Query by Book Title
This supports both partial and exact match. You can include either '_' or '%' right before or after the search string. This function will return "Book Title", "ISBN", "Unit Price", "No. of Copies Available" and "A List of Authors" 
c. Query by Author Name
This supports both partial and exact match. You can include either '_' or '%' right before or after the search string. This function will return "Book Title", "ISBN", "Unit Price", "No. of Copies Available" and "A List of Authors"


Order Creation 
- You can create new order here. You need to input the correct customer ID and then the ISBN of the Books that you want to order. You can press 'L' to see the order list and 'F' to finish the ordering.

Order Altering 
- You can either Add or Remove the orders here that are not yet shipped. You have to input the correct Order ID. Only exact match is supported here. After you input the Order ID, you will see the order list and you need to select which books you want to alter. Then, type 'add' or 'remove', followed by an integer to change the quantity of the books that you ordered.

Order Query 
- You can see the list of orders of a user in a particularly year. A correct customer ID is required and only exact match is supported here.
Bookstore Interface:

Order Update 
- Input an order ID, the system will check if the order's shipping status could be updated and update it as requested 
- if the input is not a valid order ID(an existing order ID in the database), the system will require another input or input EXIT to quit 
- the ID is valid, check if the shipping status could be updated (is a 'N') and quantity (if it is > 0), prompt the user and require another input if the order cannot be updated. 
- if the shipping status or quantity is invalid (shipping status is neither Y or N or quantity is not a number), quit the function

Order Query 
- return the details of all shipped orders and the total charge in the input month - if the input month does not have any record of orders, it will return "No record found for the input month."

N Most Popular Book Query 
- if the input N is larger the number of books with quantity > 0, return all books with quantity > 0 ii. if the books have the same total number of order copies as the N-th book, it will return those books as well
    
  
 

