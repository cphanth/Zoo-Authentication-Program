/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phanthamany_final_authentication;

import java.io.File;   //includes io.File; io.FileNotFoundException; io.FileInputStream; io.IOException
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *This program is a user authentication system for a zoo. It will open, give the
 * user an option to log-in or exit, take-in the user's name and password for
 * credential verification (including MessageDigest5), and 
 * display the corresponding job role file if authenticated. It will log-out and
 * exit if after 3 unsuccessful attempts.
 * 
 * @author christina.pha_snhu Term19EW3 IT-145
 */
public class Phanthamany_Final_Authentication {
    
    /**
     * Gives user a choice to log-in or exit when opening the program
     * 
     * @param input is scanner object, command line argument
     * @param choicePrompt is message displayed (string source is from main)
     * @return an integer representing the user's choice
     */
    public static int loginChoice(Scanner input,String choicePrompt){
        int userChoiceInt = 0;      //user chooses to login or exit
        
        while((userChoiceInt != 1) && (userChoiceInt != 2)){
            System.out.println(choicePrompt);
            userChoiceInt = input.nextInt();
        }
        return userChoiceInt;
    }
    
    /**
     * Allows user 3 attempts for log-in until failure - leading to exiting
     * the program
     * 
     * @throws FileNotFoundException for using inputs/returned values from other
     * methods
     */
    
    public static void threeAttempts() throws FileNotFoundException{
        Scanner input = new Scanner(System.in);
        
        final String userNamePrompt = "Enter your username: ";
        final String userPasswordPrompt = "Enter you password: ";
        
        //user's input variables
        String userInputName;
        String userInputPassword;
        String userInputPasswordHashed;
        
        final int NUM_ATTEMPTS = 3; //max# of attempts
        int count = 0;              //counts # of attempts
        int i = 0;                  //iterations #; controls loop
        
        boolean programGo = false;     //variable for authenticated name/pw
        String userInput = "empty"; //initialize string
        
        count = 1;
        while (i < NUM_ATTEMPTS){
            //use other methods to retrieve username and password
            //assign to variables
            userInputName = promptForUserName(input, userNamePrompt);
            userInputPassword = promptForUserPassword(input, userPasswordPrompt);
            //pass inputPassword through MD5 to hash
            userInputPasswordHashed= returnUserInputPasswordHashed(userInputPassword);
            
            if (verifyInputs(userInputName,userInputPassword,userInputPasswordHashed) == true){
                programGo = true;       //username, pw authenticated!
            }
            else {
                programGo = false;      //username, pw not authenticated
            }
            
            if (programGo == false) {   //if username, pw not authenticated - display remaining attempts
                System.out.println("\tYou have " + (NUM_ATTEMPTS - count) + " attempt(s) left");
                i++;
                count++;
            }
            else if (programGo == true){    //if username, pw authenticated - break loop
                //System.out.println("successful login");
                break;
                //break out of loop --> go to next step in verify inputs:display roles
            }
            if (i == NUM_ATTEMPTS) {        //loop reaches max number of attempts
                System.out.println("\nYou had " + i + " unsuccessful login attempts.\n\nBetter luck next time!");
                System.exit(0);
            }
        }
    }
    
    /**
     * Prompts the user for their username. This method called within
     * threeAttempts()
     * 
     * @param input is scanner object (command line argument)
     * @param userNamePrompt string message source from main
     * @return string value of user's username
     */
    
    public static String promptForUserName(Scanner input, String userNamePrompt) {
        String inputUserName;
        
        System.out.println(userNamePrompt);
        inputUserName = input.nextLine();
        
        return inputUserName;
    }
    
    /**
     * Prompts user's password. This method called within threeAttempts()
     * 
     * @param input scanner object, command line argument
     * @param userPasswordPrompt string message source from main
     * @return  string value of user's password
     */
    
    public static String promptForUserPassword(Scanner input, String userPasswordPrompt) {
        String inputUserPassword;
        
        System.out.println(userPasswordPrompt);
        inputUserPassword = input.nextLine();
        
        return inputUserPassword;
    }
    
    /**
     * Passes the variable containing user's password and sends to external
     * message digest to hash. This method is called within threeAttempts()
     * 
     * @param inputPwString variable containing user's password, source from
     * the above method.
     * @return string value of user's hashed password
     */
    
    public static String returnUserInputPasswordHashed(String inputPwString){
        String inputUserPasswordHash;
        String inputUserPassword = inputPwString;   //pw is passed through argument
        
        MD5Hash userInputPwHash = new MD5Hash();    //call new MD5Hash object
        userInputPwHash.setPasswordHash(inputUserPassword);     //set password hashed
        inputUserPasswordHash = userInputPwHash.getPasswordHash();  //get password hashed
        
        return inputUserPasswordHash;               //return password hashed
    }
    
    /**
     * Passes input variables from threeAttempts and compares it to parsed
     * credential file. Also finds user's role and displays corresponding role
     * by calling another method, readFile().
     * This method is called from threeAttempts().
     * 
     * @param input1 username from threeAttempts()
     * @param input2 password from threeAttempts()
     * @param input3 password hashed from threeAttempts()
     * @return true or false value depending on matching inputs and credentials
     * @throws FileNotFoundException  draws info from outside method
     * threeAttempts()
     */
    
    public static boolean verifyInputs(String input1, String input2, String input3) throws FileNotFoundException {
        File credentialsText = new File("src\\phanthamany_final_authentication\\credentials.txt");
        Scanner scnr = new Scanner(credentialsText);
        
        String line;                //the text in each line of file
        String selectedLine = "user's info line";        //the line with matched user info
        String credentialPassword;  //variable for password in a file's line
        
        int lineNumber = 1;         //the line number of each line
        int dblQuote1Location;      //finding quotes to parse password in file
        int dblQuote2Location;
        
        boolean inputMatchesCredentials = false;    //boolean variable indicating authentication
        
        String userName = input1;           //assign values for parameters
        String userPassword = input2;       //the method will pass input from where it's called from
        String userPasswordHash = input3;
        
        while(scnr.hasNextLine()) {         //reads text file while a next line exists
            line = scnr.nextLine();         //assign what is read to the string variable named line
            
            if(line.indexOf(userName) < 0) {        //if index is -1, username not in line
                inputMatchesCredentials = false;
                lineNumber++;                       //go to next line
            }
            else if(line.indexOf(userName) > -1) {           //if index > -1, username found in line
                dblQuote1Location = line.indexOf('"');       //therefore index quotes and create password substring
                dblQuote2Location = line.lastIndexOf('"');   //use a break to stop while loop when username matches
                                                             //break is found closer to end of method
                credentialPassword = line.substring(dblQuote1Location + 1,dblQuote2Location);
                
                //compare passwords
                if (credentialPassword.equals(userPassword)) {
                    //System.out.println("Password match found.");
                    
                    //compare hashes
                    if (line.contains(userPasswordHash)){
                        //System.out.println("Password hash match found.");
                        selectedLine = line;        //set variable in order to be used to print role file in next block
                        inputMatchesCredentials = true;
                    }
                    else {
                        //System.out.println("Password hashes do not match.");
                        inputMatchesCredentials = false;
                    }
                    
                }
                else {
                    inputMatchesCredentials = false;
                    //System.out.println("Username matches but password does not match");
                }
                break;
            }
        }
        
        if (inputMatchesCredentials == true) {
            System.out.println("\nWELCOME!!! Log-in was successfull.");
            
            //find what the role is in the selected line
            //call readFile method to display role text
            if (selectedLine.contains("admin")){
                readFile("admin");
                System.out.println("\n");
            }
            else if (selectedLine.contains("veterinarian")){
                readFile("veterinarian");
                System.out.println("\n");
            }
            else if (selectedLine.contains("zookeeper")){
                readFile("zookeeper");
                System.out.println("\n");
            }
            else {      //*** for new hires *** :)
                System.out.println("Sorry! We don't have a role for you yet!\n\n"
                        + "Please see your direct supervisor");
                System.out.println("\n\n");
            }
        }
        else {
            System.out.println("\nYour username or password is incorrect.");
        }
        return inputMatchesCredentials;
    }
    
    /**
     * Passes string variable provided from verifyInputs() and returns
     * appropriate job file
     * This method called from verifyInputs()
     * 
     * @param roleVariable string from verifyInputs()
     * @throws FileNotFoundException 
     */
    
    public static void readFile(String roleVariable) throws FileNotFoundException{
        //create new File object to get file from project folder
        //string variable is concatinated within the file's path
        File jobFile = new File("src\\phanthamany_final_authentication\\"+roleVariable+".txt");
        //create new scanner object to read file
        Scanner returnFile = new Scanner(jobFile);
        
        String line;        //variable represents each line in file
        int lineNumber = 1; //variable for number of line in file
        
        while(returnFile.hasNextLine()){    //while file has a next line
            line = returnFile.nextLine();   //assign string line with value
            System.out.println(line);       //then print that string
            
            lineNumber++;                   //continue to next line
        }
    }
    
    /**
     * Main method that while loops based on user's decision to log-in or exit.
     * Calls method loginChoice() and threeAttempts()
     * 
     * threeAttempts() leads to verifyInputs() which also includes
     * finding job roles; the find job role block also calls method
     * readFile()which reads/displays user's role
     * 
     * @param args
     * @throws FileNotFoundException 
     */
    
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scnr = new Scanner(System.in);  //this will scan inputs for my methods
        int choiceInt;                          //initialize for decision tree-->login/exit
        final String choicePrompt = "What would you like to do?\nEnter 1 to log-in\nEnter 2 to exit";
        final String goodbyeMessage = "You are now exiting the program! See you later!";
        
        choiceInt = loginChoice(scnr,choicePrompt);     //user chooses 1 or 2
        
        while ((choiceInt == 1) || (choiceInt == 2)) {  //while input is valid
                                                        //continue to decision branch
            if (choiceInt == 2) {                       //2 - user exits program
                System.out.println("\nUser chooses to exit");
                System.out.println(goodbyeMessage);
                System.exit(0);
            }
            else if (choiceInt == 1) {                  //1 - user elects to enter credentials
                System.out.println("\nUser chooses to sign-in.");
                
                /* give user three tries to input valid credentials
                - verifying credentials and reading files branch from this method
                - see above details
                */
                threeAttempts();  

                //give user choice to login again
                choiceInt = loginChoice(scnr,choicePrompt);
            }
        }        
    }
}
