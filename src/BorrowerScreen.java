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
                    console.readLine("Sign In to Continue ... (Press Enter) ");
                    clearScreen();
                    loopLimiter++;
                    continue;
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
                    console.readLine("Successfully Logged In (Press Enter) : ");
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

        while (loopLimiter < 5000) {

            System.out.println("Creating your new Account");
            System.out.println();

            String b_username = "";
            String b_password = "";
            String b_name = "";
            String b_phn_number = "";
            String b_aadhar_number = "";
            String b_driving_licence = "";
            String b_address = "";


            // User-Name
            
            try {
                b_username = console.readLine("Enter a UserName (50 Characters Max): ");
                ResultSet userNameCheck = dbConnector.excecuteSelect(borrowerColumnNames.get(1), borrowerTableName, borrowerColumnNames.get(2)+" = '"+b_username+"'", null, null, null);
                if(userNameCheck.next() || b_username.length() == 0){
                    console.readLine("Sorry the User-name is already taken :( (Press Enter) ");
                    clearLine(4);
                    loopLimiter++;
                    continue;
                }
                if(b_username.length() != 0 && b_username.length() > 50){
                    console.readLine("Sorry User-name is invalid :( (Press Enter) ");
                    clearLine(4);
                    loopLimiter++;
                    continue;
                }
            } catch (Exception e) {
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(4);
                loopLimiter++;
                continue;
            }


            
            // Password
            
            try {
                b_password = console.readLine("Enter a password (20 Characters Max) : ");
                if(b_password.equals("") || b_password.length() > 20){
                    console.readLine("Sorry Your Password is invalid :( (Press Enter) ");
                    clearLine(5);
                    loopLimiter++;
                    continue;
                }
            } catch (Exception e) {
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(5);
                loopLimiter++;
                continue;
            }
            
            
            
            // Name
            
            try {
                b_name = console.readLine("Enter your Name (100 Characters Max) : ");
                if(b_name.equals("") || b_name.length() > 100){
                    console.readLine("Sorry Your Name is empty :( (Press Enter) ");
                    clearLine(6);
                    loopLimiter++;
                    continue;
                }
                
            } catch (Exception e) {
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(6);
                loopLimiter++;
                continue;
            }
            
            
            
            // Phone Number 
            
            try {
                b_phn_number = console.readLine("Enter your Phone Number (10 Characters Max) : ");
                if(b_phn_number.length() != 10){
                    console.readLine("Sorry Your Phone Number is invalid :( (Press Enter) ");
                    clearLine(7);
                    loopLimiter++;
                    continue;
                }
            } catch (Exception e) {
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(7);
                loopLimiter++;
                continue;
            }
            
            
            
            // Aadhar Number ** 
            
            try {
                b_aadhar_number = console.readLine("Enter your Aadhar Number (12 Characters) : ");

                ResultSet aadharCheck = dbConnector.excecuteSelect(borrowerColumnNames.get(1), borrowerTableName, borrowerColumnNames.get(6)+" = '"+b_aadhar_number+"'", null, null, null);

                if(aadharCheck.next() || b_aadhar_number.length() != 12){
                    console.readLine("Sorry Your Aadhar Number is invalid :( (Press Enter) ");
                    clearLine(8);
                    loopLimiter++;
                    continue;
                }

            } catch (Exception e) {
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(8);
                loopLimiter++;
                continue;
            }



            // Driving Licence ** 
            
            try {
                b_driving_licence = console.readLine("Enter your Driving licence Number (DL No) (16 Characters) : ");

                ResultSet drivingLisCheck = dbConnector.excecuteSelect(borrowerColumnNames.get(1), borrowerTableName, borrowerColumnNames.get(7)+" = '"+b_driving_licence+"'", null, null, null);

                if(drivingLisCheck.next() || b_driving_licence.length() != 16){
                    console.readLine("Sorry Your DL Number is invalid :( (Press Enter) ");
                    clearLine(9);
                    loopLimiter++;
                    continue;
                }

            } catch (Exception e) {
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(9);
                loopLimiter++;
                continue;
            }
            
            
            
            // Address ** 
            
            try {
                b_address = console.readLine("Enter your Current Address (300 Characters Max) : ");
                
                if(b_address.length() == 0 && b_address.length() > 300){
                    console.readLine("Sorry Your Address is invalid :( (Press Enter) ");
                    clearLine(10);
                    loopLimiter++;
                    continue;
                }
            

            } catch (Exception e) {
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(10);
                loopLimiter++;
                continue;
            }

            String columnNames = "(";

            for (int i = 2; i < 8; i++) {
                columnNames += borrowerColumnNames.get(i)+",";
            }

            columnNames += borrowerColumnNames.get(8)+")";
            
            String values = "('"+b_username+"' , '"+b_password+"' , '"+b_name+"' , '"+b_phn_number+"' , '"+b_aadhar_number+"' , '"+b_driving_licence+"' , '"+b_address+"'"+")";
            
            boolean result = dbConnector.excecuteInsert(borrowerTableName, columnNames, values);
            
            if(result){
                break;
            }
            else{
                console.readLine("Something's Wrong Please try again (Press Enter) ");
                clearLine(11);
                loopLimiter++;
                continue;
            }
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