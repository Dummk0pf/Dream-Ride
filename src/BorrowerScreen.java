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

    private HashMap<Integer, String> borrowerColumnNames;

    public BorrowerScreen(){
        dbConnector = new SQLInterface();
        vTable = new VehicleTable();

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

    public void signIn(){
        getInput();
    }

    private void getInput(){
        clearScreen();

        // TODO: Welcome Screen and terminate application

        username = console.readLine("User-name: ");
        password = String.valueOf(console.readPassword("Password: "));

        ResultSet res = dbConnector.excecuteSelect("admin_password", "admin_accounts", "admin_username = '"+username+"'", null, null, null);

        try {
            if(res != null && res.next() && res.getString(1).equals(password)){
                // TODO: Display Table as a grid
                vTable.displayAdminTable();
            }

            else{
                System.out.println("Invalid SignIn :(");
                console.readLine("Press Enter to Continue: ");
                getInput();
            }
        } catch (SQLException e) {
            System.out.println("Class: AdminScreen Method: getInput");
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