import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BorrowerScreen implements Screen{
    
    private int LOOP_MAX_LIMIT = 2000;
    private String userid;
    private String username;
    private String password;

    private Console console = System.console();
    private SQLInterface dbConnector;
    private VehicleTable vTable;
    private BorrowerCart bCart;

    private BorrowerTable bTable;
    private String borrowerTableName;
    private HashMap<Integer, String> borrowerColumnNames;
    private String vehicleTableName;

    public BorrowerScreen(){

        dbConnector = new SQLInterface();
        vTable = new VehicleTable();
        bTable = new BorrowerTable();

        userid = "";
        borrowerTableName = "borrower_accounts";
        vehicleTableName = "vehicle_details";
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
        int loopLimiter = 0;
        while (loopLimiter < LOOP_MAX_LIMIT) {
            try {
                clearScreen();
                System.out.println("1. Sign In");
                System.out.println("2. Sign Up");
                System.out.println("3. Exit ");
                
                int option = Integer.parseInt(console.readLine("Enter your option (1/2/3) : "));

                if(option == 1){
                    signIn();
                    borrowerActions();
                }

                else if(option == 2){
                    signUp();
                    console.readLine("Signed up successfully :) Sign In to Continue ... (Press Enter) ");
                    clearScreen();
                    loopLimiter++;
                    continue;
                }
                
                else if(option == 3){
                    console.readLine("Press Enter to Continue ... ");
                    clearScreen();
                    return;
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

        while(loopLimiter < LOOP_MAX_LIMIT){
            username = console.readLine("User-name: ");
            password = String.valueOf(console.readPassword("Password: "));

            ResultSet res = dbConnector.excecuteSelect(borrowerColumnNames.get(3)+", "+borrowerColumnNames.get(1), borrowerTableName, borrowerColumnNames.get(2)+" = '"+username+"'", null, null, null);

            try {
                if(res != null && res.next() && res.getString(1).equals(password)){
                    // TODO: Display Table as a grid
                    console.readLine("Successfully Logged In (Press Enter) : ");
                    userid = res.getString(2);
                    bCart = new BorrowerCart(userid);
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

        clearScreen();

        int loopLimiter = 0;

        while (loopLimiter < LOOP_MAX_LIMIT) {

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

    public void borrowerActions(){

        vTable.displayBorrowerTable();

        System.out.println();
        System.out.println();
        
        int loopLimiter = 0;
        char option = 'v';
        
        while (loopLimiter < LOOP_MAX_LIMIT) {
            boolean carSelectFlag = bCart.isCarPresent();
            boolean bikeSelectFlag = bCart.isBikePresent();
            int paymentStatus = bCart.getPaymentStatus();
            
            System.out.println("Add a vehicle to the Cart (A/a) : ");
            System.out.println("View your cart (C/c)            : ");
            System.out.println("View your account (V/v)         : ");
            System.out.println("Quit Application (Q/q)          : ");

            try {
                String options = console.readLine("Enter your Action: ").toLowerCase();
                if(options.length() != 1 || !"acvq".contains(options)){
                    console.readLine("Invalid Option :( (Press Enter) : ");
                    clearLine(6);
                    loopLimiter++;
                    continue;
                }
                option = options.charAt(0);
                
            } catch (Exception e) {
                console.readLine("Invalid Option :( (Press Enter) : ");
                clearLine(6);
                loopLimiter++;
                continue;
            }

            if(option == 'a' && paymentStatus == -1){

                int addChoice = 0;
                System.out.println();
                System.out.println();

                System.out.println("Adding Vehicle to your Cart .. ");

                try {
                    System.out.println("1. Car");
                    System.out.println("2. Bike");
                    addChoice = Integer.parseInt(console.readLine("Enter your choice (1/2) : "));

                    if(addChoice != 1 && addChoice != 2){
                        console.readLine("Invalid Choice :( (Press Enter) ");
                        loopLimiter++;
                        clearLine(12);
                        continue;
                    }
                    
                } catch (Exception e) {
                    console.readLine("Invalid Choice :( (Press Enter) ");
                    loopLimiter++;
                    clearLine(12);
                    continue;
                }

                System.out.println();

                if(addChoice == 1){
                    if(carSelectFlag){
                        char res = 'n';
                        System.out.println("Only one Car and one Bike can be rented at a time ... ");
                        try {
                            String response = console.readLine("Are you sure you want to replace your choice? (y/n) : ");
                            if(response.length() != 1 || !"yn".contains(response)){
                                console.readLine("Invalid Choice :( (Press Enter) ");
                                loopLimiter++;
                                clearLine(15);
                                continue;
                            }
                            res = response.charAt(0);
                        } catch (Exception e) {
                            console.readLine("Invalid Choice :( (Press Enter) ");
                            loopLimiter++;
                            clearLine(15);
                            continue;
                        }
                        
                        if(res == 'n'){
                            loopLimiter++;
                            clearLine(14);
                            continue;
                        }

                        else if(res == 'y'){
                            String addVehicleId = "";
                            try {
                                addVehicleId = console.readLine("Enter the vehicle Id: ");

                                ResultSet isPresent = dbConnector.excecuteSelect("v_id", vehicleTableName, "v_id = '"+addVehicleId+"' AND v_type = 'Car' AND v_service_state = true", null, null, null);

                                if(isPresent != null && isPresent.next()){
                                    boolean isAdded = bCart.addVehicle(0, addVehicleId);
                                    
                                    if(!isAdded){
                                        console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                                        clearLine(16);
                                        loopLimiter++;
                                        continue;
                                    }
                                }
                                
                                else{
                                    console.readLine("Vehicle Id not found :( (Press Enter) ");
                                    clearLine(16);
                                    loopLimiter++;
                                    continue;
                                }

                                
                            } catch (Exception e) {
                                console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                                clearLine(16);
                                loopLimiter++;
                                continue;
                            }

                            console.readLine("Vehicle Added to Cart Successfully :) (Press Enter) ");
                            clearLine(16);
                            continue;
                            
                        }
                    }

                    else{

                        
                        String addVehicleId = "";
                        
                        try {
                            addVehicleId = console.readLine("Enter the vehicle Id: ");
                            
                            ResultSet isPresent = dbConnector.excecuteSelect("v_id", vehicleTableName, "v_id = '"+addVehicleId+"' AND v_type = 'Car' AND v_service_state = true", null, null, null);
                            
                            if(isPresent != null && isPresent.next()){
                                boolean isAdded = bCart.addVehicle(0, addVehicleId);
                                
                                if(!isAdded){
                                    console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                                    clearLine(14);
                                    loopLimiter++;
                                    continue;
                                }
                            }
                            
                            else{
                                console.readLine("Vehicle Id Not found :( (Press Enter)");
                                clearLine(14);
                                loopLimiter++;
                                continue;
                            }
                            
                            
                        } catch (Exception e) {
                            console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                            clearLine(14);
                            loopLimiter++;
                            continue;
                        }

                        carSelectFlag = bCart.isCarPresent();
                        
                        console.readLine("Vehicle Added to Cart Successfully :) (Press Enter) ");
                        clearLine(14);
                        continue;

                    }
                }

                else if(addChoice == 2){

                    if(bikeSelectFlag){
                        char res = 'n';
                        System.out.println("Only one Car and one Bike can be rented at a time ... ");
                        try {
                            String response = console.readLine("Are you sure you want to replace your choice? (y/n) : ");
                            if(response.length() != 1 || !"yn".contains(response)){
                                console.readLine("Invalid Choice :( (Press Enter) ");
                                loopLimiter++;
                                clearLine(15);
                                continue;
                            }
                            res = response.charAt(0);
                        } catch (Exception e) {
                            console.readLine("Invalid Choice :( (Press Enter) ");
                            loopLimiter++;
                            clearLine(15);
                            continue;
                        }
                        
                        if(res == 'n'){
                            loopLimiter++;
                            clearLine(14);
                            continue;
                        }

                        else if(res == 'y'){
                            String addVehicleId = "";
                            try {
                                addVehicleId = console.readLine("Enter the vehicle Id: ");

                                ResultSet isPresent = dbConnector.excecuteSelect("v_id", vehicleTableName, "v_id = '"+addVehicleId+"' AND v_type = 'Bike' AND v_service_state = true", null, null, null);

                                if(isPresent != null && isPresent.next()){
                                    boolean isAdded = bCart.addVehicle(1, addVehicleId);
                                    
                                    if(!isAdded){
                                        console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                                        clearLine(16);
                                        loopLimiter++;
                                        continue;
                                    }
                                }
                                
                                else{
                                    console.readLine("Vehicle Id not found :( (Press Enter) ");
                                    clearLine(16);
                                    loopLimiter++;
                                    continue;
                                }
                                
                                
                            } catch (Exception e) {
                                console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                                clearLine(16);
                                loopLimiter++;
                                continue;
                            }

                            console.readLine("Vehicle Added to Cart Successfully :) (Press Enter) ");
                            clearLine(16);
                            continue;
                            
                        }
                    }

                    else{

                        bikeSelectFlag = bCart.isBikePresent();

                        String addVehicleId = "";

                        try {
                            addVehicleId = console.readLine("Enter the vehicle Id: ");

                            ResultSet isPresent = dbConnector.excecuteSelect("v_id", vehicleTableName, "v_id = '"+addVehicleId+"' AND v_type = 'Bike' AND v_service_state = true", null, null, null);

                            if(isPresent != null && isPresent.next()){
                                boolean isAdded = bCart.addVehicle(1, addVehicleId);
                                
                                if(!isAdded){
                                    console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                                    clearLine(14);
                                    loopLimiter++;
                                    continue;
                                }
                            }
                            
                            else{
                                console.readLine("Vehicle Id Not found :( (Press Enter)");
                                clearLine(14);
                                loopLimiter++;
                                continue;
                            }
                            
                            
                        } catch (Exception e) {
                            console.readLine("Addition of Vehicle Unsuccessfull :( (Press Enter)");
                            clearLine(14);
                            loopLimiter++;
                            continue;
                        }

                        console.readLine("Vehicle Added to Cart Successfully :) (Press Enter) ");
                        clearLine(14);
                        continue;
                    }

                }
                
            }

            else if(option == 'a' && paymentStatus == 0){
                System.out.println();
                System.out.println();
                console.readLine("Cannot Add vehicles when your payment is being Processed :( (Press Enter) ... ");
                clearLine(8);
            }
            
            else if(option == 'a' && paymentStatus == 1){
                System.out.println();
                System.out.println();
                console.readLine("Cannot Add vehicles when you have already rented vehicles :( (Press Enter) ... ");
                clearLine(8);
            }

            else if(option == 'c'){

                clearScreen();
                bCart.displayBorrowerCart();
                
                clearScreen();
                vTable.displayBorrowerTable();
                System.out.println();
                System.out.println();
                
            }

            else if(option == 'v'){

                clearScreen();
                boolean forceQuit = bTable.displayBorrowerAccount(userid);
                
                if(forceQuit)
                break;
                
                clearScreen();
                vTable.displayBorrowerTable();
                System.out.println();
                System.out.println();

            }

            else if(option == 'q'){
                break;
            }

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