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
    
  
 

