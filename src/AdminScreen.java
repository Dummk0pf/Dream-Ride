import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AdminScreen implements Table{

    private int LOOP_MAX_LIMIT = 2000;
    private String admin_username = "";
    private String admin_password = "";

    private SQLInterface dbConnector = null;
    private VehicleTable vTable = null;
    private BorrowerTable bTable = null;
    private RentedVehiclesTable rTable = null;
    private PaymentDetails pTable = null;
    private Console console = System.console();

    private HashMap<Integer, String> vehicleTableColumns;
    private String vehicleTableName;
    private String adminTableName;

    public AdminScreen(){
        dbConnector = new SQLInterface();
        vTable = new VehicleTable();
        bTable = new BorrowerTable();
        pTable = new PaymentDetails();
        rTable = new RentedVehiclesTable();

        vehicleTableName = "vehicle_details";
        adminTableName = "admin_accounts";

        vehicleTableColumns = new HashMap<Integer, String>();

        vehicleTableColumns.put(1, "v_id");
        vehicleTableColumns.put(2, "v_name");
        vehicleTableColumns.put(3, "v_numberplate");
        vehicleTableColumns.put(4, "v_type");
        vehicleTableColumns.put(5, "v_rent");
        vehicleTableColumns.put(6, "v_total_distance");
        vehicleTableColumns.put(7, "v_security_deposit");
        vehicleTableColumns.put(8, "v_service_state");
        vehicleTableColumns.put(9, "v_borrower_id");
        vehicleTableColumns.put(10, "v_rented_date");
        vehicleTableColumns.put(11, "v_return_date");

    }
   
    
    public void logIn(){
        signIn();
        tableOptions();
    }


    private void signIn(){
        clearScreen();

        // TODO: Welcome Screen and terminate application

        admin_username = console.readLine("User-name: ");
        admin_password = String.valueOf(console.readPassword("Password: "));

        ResultSet res = dbConnector.excecuteSelect("admin_password", adminTableName, "admin_username = '"+admin_username+"'", null, null, null);

        try {
            if(res != null && res.next() && res.getString(1).equals(admin_password)){
                // TODO: Display Table as a grid
                vTable.displayAdminTable();
            }

            else{
                System.out.println("Invalid SignIn :(");
                console.readLine("Press Enter to Continue: ");
                signIn();
            }
        } catch (SQLException e) {
            System.out.println("Class: AdminScreen Method: signIn");
        }

    }


    private void tableOptions() {
        int loopLimiter = 0;

        while (loopLimiter < LOOP_MAX_LIMIT) {

            System.out.println();

            System.out.println("Search a Vehicle          (S/s) : ");
            System.out.println("Sort by                   (T/t) : ");
            System.out.println("Add a Vehicle             (A/a) : ");
            System.out.println("Modify Vehicle Details    (M/m) : ");
            System.out.println("Remove a Vehicle          (R/r) : ");
            System.out.println("View Previous Rented Cars (V/v) : ");
            System.out.println("View Current Rented Cars  (C/c) : ");
            System.out.println("View Borrowers            (B/b) : ");
            System.out.println("View Payment Records      (P/p) : ");
            System.out.println("View Unserviced Vehicles  (U/u) : ");
            System.out.println("Quit Application          (Q/q) : ");

            System.out.println();
    
            char option = '1';
            String action = "";
            System.out.println();

            do {
                
                clearLine(1);
                action = console.readLine("Enter an action to perform: ");
                if(action.equals("") || action.length() > 1)
                continue;
                option = Character.toLowerCase(action.charAt(0));

            } while (!("mqstarvcbpu".contains(option+"")));
    
            if(option == 'a'){
                addVehicle();
            }
            
            else if(option == 'r'){
                removeVehicle();
            }
            
            else if(option == 'm'){
                modifyVehicle();
            }
    
            else if(option == 's'){
                searchVehicle();
            }
    
            else if(option == 't'){
                sortVehicles();
            }
            
            else if(option == 'v'){
                rTable.displayReturnedVehicles();
            }
            
            else if(option == 'c'){
                // TODO: bug
                rTable.displayAdminRentedTable();
            }
            
            else if(option == 'b'){
                bTable.displayAllBorrowers();
            }

            else if(option == 'p'){
                pTable.displayAdminPaymentDetails();
            }

            else if(option == 'u'){
                vTable.displayUnServicedTable();
            }
            
            else if(option == 'q'){
                console.readLine("Press Enter to Continue ... ");
                break;
            }
            
            vTable.displayAdminTable();

            loopLimiter++;
        }

    }

    private void addVehicle() {
        
        System.out.println();

        System.out.println("Adding a Vehicle :) ");
        
        
        int loopLimiter = 0;
        
        while(loopLimiter < LOOP_MAX_LIMIT){
            StringBuilder vehicleInfo = new StringBuilder();
            
            vehicleInfo.append("(");
            
            vehicleInfo.append("\""+console.readLine("Vehicle ID              : ")+"\",");
            vehicleInfo.append("\""+console.readLine("Vehicle Name            : ")+"\",");
            vehicleInfo.append("\""+console.readLine("Vehicle NumberPlate     : ")+"\",");
            vehicleInfo.append("\""+console.readLine("Vehicle Type (Bike/Car) : ")+"\",");
            vehicleInfo.append(console.readLine("Vehicle Rent              : ")+",");
            vehicleInfo.append("0"+",");
            vehicleInfo.append(console.readLine("Vehicle Security Deposit  : ")+",");
            vehicleInfo.append("true"+",");
            vehicleInfo.append("null"+",");
            vehicleInfo.append("null"+",");
            vehicleInfo.append("null");
            
            vehicleInfo.append(")");
            
            char mistake = console.readLine("Are you sure about that? (y/n): ").charAt(0);
            
            if(mistake == 'n'){
                clearLine(7);
                loopLimiter++;
                continue;
            }
            
            boolean result = dbConnector.excecuteInsert(vehicleTableName, vehicleInfo.toString());
            
            if(result){
                console.readLine("Vehicle Added Sucessfully (Press Enter)...");
                clearLine(8);
                break;
            }
            
            else{
                console.readLine("Sorry Invalid Vehicle ID or Details Please Try Again (Press Enter) : ");
                clearLine(9);
            }
            
            loopLimiter++;
            
        }
        
    }
    
    private void modifyVehicle() {

        System.out.println();

        int loopLimiter = 0;

        while(loopLimiter < LOOP_MAX_LIMIT){

            int choiceLimiter = 0;

            String vehicle_id = "\'"+console.readLine("Enter the Vehicle ID of the vehicle: ")+"\'";
    
            ResultSet result = dbConnector.excecuteSelect("v_id", vehicleTableName, "v_id = "+vehicle_id, null, null, null);
            
            try {
                if(result == null || !result.next()){
                    console.readLine("Vehicle ID is not available (Press enter): ");
                    clearLine(2);
                    loopLimiter++;
                    continue;
                }
            } catch (SQLException e) {
                System.out.println("Class AdminScreen Method: modifyVehicle");
                e.printStackTrace();
            }

            // TODO: rented date and return date problems
            // Value of rented date cannot be reset as a usual method
            // Similarly for rent and return date and for any other value with a null value

            System.out.println("01. Vehicle Name");
            System.out.println("02. Vehicle NumberPlate");
            System.out.println("03. Vehicle Type");
            System.out.println("04. Vehicle Rent");
            System.out.println("05. Vehicle Distance travelled");
            System.out.println("06. Vehicle Security Deposit");
            System.out.println("07. Vehicle Service State");
            System.out.println("08. Vehicle Borrower ID");
            System.out.println();

            int columnNumber = -1;

            while(choiceLimiter < 1000){
                try {

                    columnNumber = Integer.parseInt(console.readLine("Enter the Value to be changed (0 - 10) : "));
                    if(columnNumber > 0 && columnNumber < 9)
                    break;
                    else{
                        System.out.println("Invalid Number (Press Enter): ");
                        clearLine(1);
                        choiceLimiter++;
                    }
                    
                } catch (Exception e) {
                    System.out.println("Invalid Number (Press Enter): ");
                    clearLine(1);
                    choiceLimiter++;
                }
    
            }
            
            String setValue = console.readLine("Enter the Value: ");

            if(columnNumber < 4){
                setValue = "\'" + setValue + "\'";
            }

            String queryValue = vehicleTableColumns.get(columnNumber+1)+" = "+setValue;

            String conditionValue = "v_id = "+vehicle_id;

            boolean res = dbConnector.excecuteUpdate(vehicleTableName, queryValue, conditionValue);

            if(res){
                console.readLine("Vehicle Information updated successfully.. (Press Enter)");
                break;
            }

            else{
                console.readLine("Error in updating information please try again (Press enter)");
                clearLine(14);
            }

        }

    }

    private void removeVehicle() {

        // TODO: Try to add isdeleted column and change the value as yes or no

        System.out.println();

        int loopLimiter = 0;

        while(loopLimiter < LOOP_MAX_LIMIT){

            String vehicle_id = "\'"+console.readLine("Enter the Vehicle ID of the vehicle: ")+"\'";
    
            ResultSet result = dbConnector.excecuteSelect("v_id", vehicleTableName, "v_id = "+vehicle_id, null, null, null);
            
            try {
                if(result == null || !result.next()){
                    console.readLine("Vehicle ID is not available (Press enter): ");
                    clearLine(2);
                    loopLimiter++;
                    continue;
                }
            } catch (SQLException e) {
                System.out.println("Class AdminScreen Method: removeVehicle");
                e.printStackTrace();
            }

            String choice = console.readLine("Are you sure about removing the vehicle (y/n) : ").toLowerCase();

            if(choice.charAt(0) == 'y'){
                boolean res = dbConnector.excecuteDelete(vehicleTableName, "v_id = "+vehicle_id);
                if(res){
                    console.readLine("Successfully removed vehicle... (Press Enter)");
                    break;
                }
                
                else{
                    console.readLine("Please try again... (Press Enter)");
                    loopLimiter++;
                    clearLine(4);
                }
            }

            else{
                break;
            }

        }
    }

    public void searchVehicle(){

        System.out.println();

        System.out.println("1. Search By Name        : ");
        System.out.println("2. Search by NumberPlate : ");

        
        int loopLimiter = 0;
        
        while(loopLimiter < LOOP_MAX_LIMIT){

            try {
            
                int choice = Integer.parseInt(console.readLine("Enter your choice (1/2) : "));

                String searchColumn = "";
                String searchValue = "";
    
                if(choice == 1){
                    searchColumn = "v_name";
                    searchValue = "'"+console.readLine("Enter the Name of the vehicle: ")+"'";
                }

                else if(choice == 2){
                    searchColumn = "v_numberplate";
                    searchValue = "'"+console.readLine("Enter the Number Plate of the vehicle: ")+"'";
                }

                else{
                    console.readLine("Invalid Choice.. (Press Enter)");
                    clearLine(2);
                    loopLimiter++;
                    continue;
                }
        
                ResultSet result = dbConnector.excecuteSelect("*", vehicleTableName, searchColumn+" = "+searchValue, null, null, null);

                boolean printResult = displayTable(result);

                if(!printResult){
                    console.readLine("Vehicle is not available (Press enter): ");
                    clearLine(3);
                    loopLimiter++;
                    continue;
                }

                System.out.println();

                console.readLine("Press Enter ... ");
                break;

            } catch (Exception e) {
                clearLine(1);
                loopLimiter++;
            }

        }
        
    }
      
    public void sortVehicles(){

        System.out.println();
    
        System.out.println("1. Sort By Name        : ");
        System.out.println("2. Sort by NumberPlate : ");
    
        
        int loopLimiter = 0;
        
        while(loopLimiter < LOOP_MAX_LIMIT){
    
            try {
            
                int choice = Integer.parseInt(console.readLine("Enter your choice (1/2) : "));
    
                String sortColumn = "";
    
                if(choice == 1){
                    sortColumn = "v_name";
                }
    
                else if(choice == 2){
                    sortColumn = "v_numberplate";
                }
    
                else{
                    console.readLine("Invalid Choice.. (Press Enter)");
                    clearLine(2);
                    loopLimiter++;
                    continue;
                }
        
                ResultSet result = dbConnector.excecuteSelect("*", vehicleTableName, null, null, null, sortColumn);
    
                boolean printResult = displayTable(result);
    
                if(!printResult){
                    console.readLine("Vehicles are not available (Press enter): ");
                    clearLine(3);
                    loopLimiter++;
                    continue;
                }
    
                System.out.println();

                console.readLine("Press Enter ... ");
                break;
    
            } catch (Exception e) {
                clearLine(1);
                loopLimiter++;
            }
    
        }
    }
}

