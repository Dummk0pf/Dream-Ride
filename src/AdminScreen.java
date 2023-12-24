import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class AdminScreen {

    private String admin_username = "";
    private String admin_password = "";

    private SQLInterface dbConnector = null;
    private VehicleTable vTable = null;
    private Console console = System.console();

    private HashMap<Integer, String> adminColumnNames;
    private String tableName;

    public AdminScreen(){
        dbConnector = new SQLInterface();
        vTable = new VehicleTable();

        tableName = "vehicle_details";

        adminColumnNames = new HashMap<Integer, String>();

        adminColumnNames.put(1, "v_id");
        adminColumnNames.put(2, "v_name");
        adminColumnNames.put(3, "v_numberplate");
        adminColumnNames.put(4, "v_type");
        adminColumnNames.put(5, "v_rent");
        adminColumnNames.put(6, "v_total_distance");
        adminColumnNames.put(7, "v_security_deposit");
        adminColumnNames.put(8, "v_service_state");
        adminColumnNames.put(9, "v_borrower_id");
        adminColumnNames.put(10, "v_rented_date");
        adminColumnNames.put(11, "v_return_date");

    }
    
    public void signIn(){
        getInput();
        tableOptions();
    }

    private void getInput(){
        clearScreen();

        // TODO: Welcome Screen and terminate application

        admin_username = console.readLine("User-name: ");
        admin_password = String.valueOf(console.readPassword("Password: "));

        ResultSet res = dbConnector.excecuteSelect("admin_password", "admin_accounts", "admin_username = '"+admin_username+"'", null, null, null);

        try {
            if(res != null && res.next() && res.getString(1).equals(admin_password)){
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

    private void tableOptions() {
        int loopLimiter = 0;

        while (loopLimiter < 1000) {

            System.out.println();

            System.out.println("Search a Vehicle: (S/s)");
            System.out.println("Sort by : (T/t)");
            System.out.println("Add a Vehicle: (A/a)");
            System.out.println("Remove a Vehicle: (R/r)");
            System.out.println("Modify Vehicle Details: (M/m)");
            System.out.println("Quit Application: (Q/q)");

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

            } while (!("mqstar".contains(option+"")));
    
            if(option == 'a'){
                addVehicle();
                vTable.displayAdminTable();
            }
            
            else if(option == 'r'){
                removeVehicle();
                vTable.displayAdminTable();
            }
            
            else if(option == 'm'){
                modifyVehicle();
                vTable.displayAdminTable();
            }
    
            else if(option == 's'){
                searchVehicle();
                vTable.displayAdminTable();
            }
    
            else if(option == 't'){
                sortVehicles();
                vTable.displayAdminTable();
            }
    
            else if(option == 'q'){
                console.readLine("Press Enter to Continue ... ");
                break;
            }

            loopLimiter++;
        }

    }



    private void addVehicle() {
        
        System.out.println();

        System.out.println("Adding a Vehicle :) ");
        
        StringBuilder vehicleInfo = new StringBuilder();
        
        int loopLimiter = 0;
        
        while(loopLimiter < 1000){
            
            vehicleInfo.append("(");
            
            vehicleInfo.append("\""+console.readLine("Vehicle ID : ")+"\",");
            vehicleInfo.append("\""+console.readLine("Vehicle Name : ")+"\",");
            vehicleInfo.append("\""+console.readLine("Vehicle NumberPlate : ")+"\",");
            vehicleInfo.append("\""+console.readLine("Vehicle Type (Bike/Car) : ")+"\",");
            vehicleInfo.append(console.readLine("Vehicle Rent : ")+",");
            vehicleInfo.append("0"+",");
            vehicleInfo.append(console.readLine("Vehicle Security Deposit : ")+",");
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
            
            boolean result = dbConnector.excecuteInsert(tableName, vehicleInfo.toString());
            
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

        while(loopLimiter < 1000){

            int choiceLimiter = 0;

            String vehicle_id = "\'"+console.readLine("Enter the Vehicle ID of the vehicle: ")+"\'";
    
            ResultSet result = dbConnector.excecuteSelect("v_id", tableName, "v_id = "+vehicle_id, null, null, null);
            
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

            System.out.println("01. Vehicle Name");
            System.out.println("02. Vehicle NumberPlate");
            System.out.println("03. Vehicle Type");
            System.out.println("04. Vehicle Rent");
            System.out.println("05. Vehicle Distance travelled");
            System.out.println("06. Vehicle Security Deposit");
            System.out.println("07. Vehicle Service State");
            System.out.println("08. Vehicle Borrower ID");
            System.out.println("09. Vehicle Rented Date");
            System.out.println("10. Vehicle Return Date");
            System.out.println();

            int columnNumber = -1;

            while(choiceLimiter < 1000){
                try {

                    columnNumber = Integer.parseInt(console.readLine("Enter the Value to be changed (0 - 10) : "));
                    if(columnNumber > 0 && columnNumber < 11)
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

            String queryValue = adminColumnNames.get(columnNumber+1)+" = "+setValue;

            String conditionValue = "v_id = "+vehicle_id;

            boolean res = dbConnector.excecuteUpdate(tableName, queryValue, conditionValue);

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

        while(loopLimiter < 1000){

            String vehicle_id = "\'"+console.readLine("Enter the Vehicle ID of the vehicle: ")+"\'";
    
            ResultSet result = dbConnector.excecuteSelect("v_id", tableName, "v_id = "+vehicle_id, null, null, null);
            
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
                boolean res = dbConnector.excecuteDelete(tableName, "v_id = "+vehicle_id);
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

        System.out.println("1. Search By Name: ");
        System.out.println("2. Search by NumberPlate: ");

        
        int loopLimiter = 0;
        
        while(loopLimiter < 1000){

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
        
                ResultSet result = dbConnector.excecuteSelect("*", tableName, searchColumn+" = "+searchValue, null, null, null);


                if(result == null || !result.next()){
                    console.readLine("Vehicle is not available (Press enter): ");
                    clearLine(3);
                    loopLimiter++;
                    continue;
                }

                System.out.println();

                do {
                    for (int i = 0; i < 11; i++) {
                        System.out.print(result.getString(i+1)+" ");
                    }
                    System.out.println();
                } while (result.next());

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
    
        System.out.println("1. Sort By Name: ");
        System.out.println("2. Sort by NumberPlate: ");
    
        
        int loopLimiter = 0;
        
        while(loopLimiter < 1000){
    
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
        
                ResultSet result = dbConnector.excecuteSelect("*", tableName, null, null, null, sortColumn);
    
    
                if(result == null || !result.next()){
                    console.readLine("Vehicles are not available (Press enter): ");
                    clearLine(3);
                    loopLimiter++;
                    continue;
                }
    
                System.out.println();
    
                do {
                    for (int i = 0; i < 11; i++) {
                        System.out.print(result.getString(i+1)+" ");
                    }
                    System.out.println();
                } while (result.next());
    
                System.out.println();

                console.readLine("Press Enter ... ");
                break;
    
            } catch (Exception e) {
                clearLine(1);
                loopLimiter++;
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

