import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BorrowerScreen {
    
    private String username;
    private String password;

    private Console console = System.console();
    private SQLInterface dbConnector;
    private VehicleTable vTable;

    private String borrowerTableName;
    private HashMap<Integer, String> borrowerColumnNames;

    public BorrowerScreen(){

        dbConnector = new SQLInterface();
        vTable = new VehicleTable();
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
        
    }

    public void logIn(){
        clearScreen();
        int loopLimiter = 0;
        while (loopLimiter < 1000) {
            try {
                System.out.println("1. Sign In");
                System.out.println("2. Sign Up");
                
                int option = Integer.parseInt(console.readLine("Enter your option (1/2) : "));

                if(option == 1){
                    signIn();
                }

                else if(option == 2){
                    signUp();
                }

            } catch (Exception e) {
                console.readLine("Invalid Choice (Press Enter)  ");
                clearLine(4);
                loopLimiter++;
            }
            
        }

    }

    private void signIn(){
        clearScreen();
        int loopLimiter = 0;

        // TODO: Welcome Screen and terminate application

        while(loopLimiter < 1000){
            username = console.readLine("User-name: ");
            password = String.valueOf(console.readPassword("Password: "));

            ResultSet res = dbConnector.excecuteSelect(borrowerColumnNames.get(3), borrowerTableName, borrowerColumnNames.get(2)+" = '"+username+"'", null, null, null);

            try {
                if(res != null && res.next() && res.getString(1).equals(password)){
                    // TODO: Display Table as a grid
                    System.out.println("Successfully Logged In");
                    break;
                }

                else{
                    System.out.println("Invalid SignIn :(");
                    console.readLine("Press Enter to Continue: ");
                    clearLine(4);
                    loopLimiter++;
                }
            } catch (SQLException e) {
                System.out.println("Class: AdminScreen Method: signIn");
                clearLine(4);
                loopLimiter++;
            }
        }
    }

    private void signUp(){

        // TODO: Username < 50 password < 20 name < 100

        clearScreen();

        int loopLimiter = 0;

        while (loopLimiter < 1000) {
            System.out.println("Creating your new Account");


        }
    }

    private void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void clearLine(int lineCount){
        for (int i = 0; i < lineCount; i++) {
            System.out.print(String.format("\033[%dA",1));
            System.out.print("\033[2K");
        }
    }

}


// borrower_id
// borrower_username
// borrower_password
// borrower_name
// borrower_phone_number
// borrower_aadhar_number
// borrower_driving_licence
// borrower_address