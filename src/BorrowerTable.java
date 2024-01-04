import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BorrowerTable implements Table{

    private int LOOP_MAX_LIMIT = 2000;
    private SQLInterface dbConnnector;
    private String borrowerTableName;
    private HashMap<Integer,String> borrowerColumnNames;
    private Console console = System.console();

    public BorrowerTable(){
        dbConnnector = new SQLInterface();
        borrowerTableName = "borrower_accounts";

        borrowerColumnNames = new HashMap<Integer, String>();

        borrowerColumnNames.put(1, "borrower_id");
        borrowerColumnNames.put(2, "borrower_username");
        borrowerColumnNames.put(3, "borrower_password");
        borrowerColumnNames.put(4, "borrower_name");
        borrowerColumnNames.put(5, "borrower_phone_number");
        borrowerColumnNames.put(6, "borrower_aadhar_number");
        borrowerColumnNames.put(7, "borrower_driving_licence");
        borrowerColumnNames.put(8, "borrower_address");
        borrowerColumnNames.put(9, "borrower_deposit");
    }

    public void displayAllBorrowers(){
        clearScreen();

        ResultSet allBorrowers = dbConnnector.excecuteSelect("*", borrowerTableName, null, null, null, null);

        try {
            displayTable(allBorrowers);
            // while(allBorrowers != null && allBorrowers.next()){
            //     for(int i=1;i<=8;i++){
            //         System.out.print(allBorrowers.getString(i)+" ");
            //     }
            //     System.out.println();
            // }
        } catch (SQLException e) {
            console.readLine("Sorry Please try again :( (Press enter) ");
            return;
        }

        int loopLimiter = 0;

        while (loopLimiter < LOOP_MAX_LIMIT) {
            
            System.out.println("Return to Main Screen (M/m) : ");
            System.out.println("Delete an Account From Server (D/d) : ");
            
            char option = 'm';
            
            try {
                String options = console.readLine("Enter your choice : ").toLowerCase();
                if(options.length() != 1 || !"md".contains(options)){
                    console.readLine("Invalid choice (Press enter)");
                    clearLine(4);
                    loopLimiter++;
                    continue;
                }

                option = options.charAt(0);
                
            } catch (Exception e) {
                console.readLine("Invalid choice (Press enter)");
                clearLine(4);
                loopLimiter++;
                continue;
            }

            if(option == 'm'){
                return;
            }

            else if(option == 'd'){

                String borrowerId = console.readLine("Enter the borrower Id : ");

                try {
                    ResultSet checkBorrower = dbConnnector.excecuteSelect(borrowerId, borrowerTableName, borrowerColumnNames.get(1)+" = "+borrowerId, null, null, null);

                    if(!checkBorrower.next() || checkBorrower.getString(1).equals("")){
                        console.readLine("Invalid Borrower Id :( (Press Enter) ");
                        clearLine(5);
                        loopLimiter++;
                        continue;
                    }

                    String choice = console.readLine("Are you sure about that ? (Type yes) : ").toLowerCase();

                    if(!choice.equals("yes")){
                        clearLine(5);
                        loopLimiter++;
                        continue;
                    }

                    boolean isDeleted = dbConnnector.excecuteDelete(borrowerTableName, borrowerColumnNames.get(1)+" = "+borrowerId);

                    if(isDeleted){
                        console.readLine("Account deleted Successfully :) (Press Enter) ");
                        break;
                    }

                    else{
                        console.readLine("Sorry something went wrong :( ");
                        clearLine(6);
                        loopLimiter++;
                        continue;
                    }
                } catch (Exception e) {
                    console.readLine("Sorry something went wrong :( ");
                    clearLine(6);
                    loopLimiter++;
                    continue;
                }

            }
        }
    }

    public boolean displayBorrowerAccount(String borrowerId){
        int loopLimiter = 0;

        ResultSet accountInfo = dbConnnector.excecuteSelect("*", borrowerTableName, borrowerColumnNames.get(1)+" = "+borrowerId, null, null, null);

        try {
            boolean printResult = displayTable(accountInfo);

            if(!printResult){
                console.readLine("Something went wrong Press Enter ");
                clearScreen();
                return false;
            }
            // if(accountInfo.next()){
            //     System.out.println("UserName: "+accountInfo.getString(2));
            //     System.out.println("Password: "+accountInfo.getString(3));
            //     System.out.println("Name: "+accountInfo.getString(4));
            //     System.out.println("Phone Number: "+accountInfo.getString(5));
            //     System.out.println("Aadhar Number: "+accountInfo.getString(6));
            //     System.out.println("Driving Licence Number: "+accountInfo.getString(7));
            //     System.out.println("Address: "+accountInfo.getString(8));
            //     System.out.println("Deposit: "+accountInfo.getString(9));
            // }
        } catch (SQLException e) {
            console.readLine("Sorry Please try again (Press Enter) ");
            clearLine(1);
            return false;
        }

        char choice = 'm';

        while (loopLimiter < LOOP_MAX_LIMIT) {
            try {
            
                System.out.println();
                System.out.println();
                System.out.println("Go Back to Main Screen (M/m) : ");
                System.out.println("Delete Your Account (D/d) : ");

                String options = console.readLine("Enter your Choice : ").toLowerCase();

                if(options.length() != 1 && !"md".contains(options)){
                    console.readLine("Invalid Choice (Press Enter) ");
                    clearLine(6);
                    loopLimiter++;
                    continue;
                }

                choice = options.charAt(0);
                
            } catch (Exception e) {
                console.readLine("Invalid Choice (Press Enter) ");
                clearLine(6);
                loopLimiter++;
                continue;
            }

            if(choice == 'm'){
                clearScreen();
                return false;
            }

            else if(choice == 'd'){

                String option = console.readLine("Are you sure you want to delete your Account? (type delete) : ");
                if(option.equals("delete")){

                    boolean isDeleted = dbConnnector.excecuteDelete(borrowerTableName, borrowerColumnNames.get(1)+" = "+borrowerId);

                    if(isDeleted){
                        console.readLine("Successfully Deleted Your account :) (Press Enter)");
                        return true;
                    }
                    else{
                        console.readLine("Deletion unsuccessfull Please try again :) (Press Enter) ");
                        clearLine(7);
                        loopLimiter++;
                        continue;
                    }
                }
                else{
                    clearLine(6);
                    loopLimiter++;
                    continue;
                }
                
            }

        }
        return false;
    }

}
