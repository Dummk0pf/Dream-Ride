import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class BorrowerCart implements Table{

    private SQLInterface dbConnector;
    private PaymentDetails payment;
    private Console console = System.console();
    private RentedVehiclesTable rTable;

    private int LOOP_MAX_LIMIT = 2000;
    private String vehicleTableName;
    private String borrowerCartTableName;
    private HashMap<Integer, String> borrowerCartColumnName;

    private String CarId;
    private String BikeId;
    private String borrowerId;
    
    private int paymentStatus;
    
    public BorrowerCart(String borrowerId){

        dbConnector = new SQLInterface();
        payment = new PaymentDetails();
        rTable = new RentedVehiclesTable();

        vehicleTableName = "vehicle_details";
        borrowerCartTableName = "borrower_cart";
        CarId = "";
        BikeId = "";
        paymentStatus = -1;
        this.borrowerId = borrowerId;
        initializeCart();

        borrowerCartColumnName = new HashMap<Integer,String>();

        borrowerCartColumnName.put(1, "v_id");
        borrowerCartColumnName.put(2, "borrower_id");
        borrowerCartColumnName.put(3, "payment_id");

    }

    public boolean isCarPresent(){
        return !CarId.equals("");
    }

    public boolean isBikePresent(){
        return !BikeId.equals("");
    }

    public int getPaymentStatus(){
        paymentStatus = checkBorrowerPaymentState(borrowerId);
        return paymentStatus;
    }

    public void initializeCart(){
        try {
            ResultSet vehicles = dbConnector.excecuteSelect("v_id, v_type", borrowerCartTableName, "borrower_id = "+borrowerId, null, null, null);
            while (vehicles != null && vehicles.next()) {
                String vId = vehicles.getString(1);
                if(vehicles.getString(2).equals("0")){
                    CarId = vId;
                }
                else if(vehicles.getString(2).equals("1")){
                    BikeId = vId;
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public boolean addVehicle(int type, String vehicleId){

        if(type == 0){
            if(CarId.equals("")){
                CarId = vehicleId;
                String values = "('"+vehicleId+"',"+borrowerId+","+type+", "+"false "+")";
                boolean isAdded = dbConnector.excecuteInsert(borrowerCartTableName, values);
                return isAdded;            
            }
    
            else{
                boolean isAdded = dbConnector.excecuteUpdate(borrowerCartTableName, borrowerCartColumnName.get(1)+" = '"+vehicleId+"'", borrowerCartColumnName.get(1)+" = '"+CarId+"' AND "+borrowerCartColumnName.get(2)+" = "+borrowerId);
                CarId = vehicleId;
                return isAdded;
            }
        }

        else{
            if(BikeId.equals("")){
                BikeId = vehicleId;
                String values = "('"+vehicleId+"',"+borrowerId+","+type+", "+"false "+")";
                boolean isAdded = dbConnector.excecuteInsert(borrowerCartTableName, values);
                return isAdded;            
            }
    
            else{
                boolean isAdded = dbConnector.excecuteUpdate(borrowerCartTableName, borrowerCartColumnName.get(1)+" = '"+vehicleId+"'", borrowerCartColumnName.get(1)+" = '"+BikeId+"' AND "+borrowerCartColumnName.get(2)+" = "+borrowerId);
                BikeId = vehicleId;
                return isAdded;
            }
        }
        
    }

    private boolean removeVehicle(String vehicleId){
        try {
            return dbConnector.excecuteDelete(borrowerCartTableName, "v_id = '"+vehicleId+"'");
        } catch (Exception e) {
            return false;
        }
    }

    private boolean bookVehicles(){



        int cautionDeposit = 30_000;

        int totalSecurityDeposit = 0;
        int bikeSecurityDeposit = 0;
        int carSecurityDeposit = 0;

        int totalRent = 0;
        int bikeRent = 0;
        int carRent = 0;

        // Caution Deposit

        try {
            ResultSet bDeposit = dbConnector.excecuteSelect("borrower_deposit", "borrower_accounts", "borrower_id = "+borrowerId, null, null, null);
            if(bDeposit != null && bDeposit.next()){
                int borrowerDeposit = Integer.parseInt(bDeposit.getString(1));
                if(borrowerDeposit > cautionDeposit){
                    dbConnector.excecuteUpdate("borrower_accounts", "borrower_deposit = "+(borrowerDeposit - cautionDeposit), "borrower_id = "+borrowerId);
                    cautionDeposit = 0;
                }
                else{
                    dbConnector.excecuteUpdate("borrower_accounts", "borrower_deposit = 0", "borrower_id = "+borrowerId);
                    cautionDeposit -= borrowerDeposit;
                }
            }
        } catch (Exception e) {
            
        }

        // totalSecurity Deposit and rent

        try {
            if(!CarId.equals("")){
                ResultSet bCar = dbConnector.excecuteSelect("v_rent, v_security_deposit", vehicleTableName, "v_id = '"+CarId+"'", null, null, null);
                if(bCar != null && bCar.next()){
                    carRent = Integer.parseInt(bCar.getString(1));
                    carSecurityDeposit = Integer.parseInt(bCar.getString(2));
                    totalRent += carRent;
                    totalSecurityDeposit += carSecurityDeposit;
                }
            }
            
            if(!BikeId.equals("")){
                ResultSet bBike = dbConnector.excecuteSelect("v_rent, v_security_deposit", vehicleTableName, "v_id = '"+BikeId+"'", null, null, null);
                if(bBike != null && bBike.next()){
                    bikeRent = Integer.parseInt(bBike.getString(1));
                    bikeSecurityDeposit = Integer.parseInt(bBike.getString(2));
                    totalRent += bikeRent;
                    totalRent += bikeSecurityDeposit;
                }
            }

        } catch (Exception e) {
            
        }
        System.out.println();
        System.out.println();
        System.out.println("Your Total Bill : ");
        System.out.println();
        System.out.println();

        System.out.println("Caution Deposit: "+cautionDeposit);

        if(!CarId.equals("")){
            System.out.println("Car Rent: "+carRent);
            System.out.println("Car Security Deposit: "+carSecurityDeposit);
        }
        if(!BikeId.equals("")){
            System.out.println("Bike Rent: "+bikeRent);
            System.out.println("Bike Security Deposit: "+bikeSecurityDeposit);
        }

        System.out.println("Total Rent: "+(carRent + bikeRent));
        System.out.println("Total Security Deposit: "+(carSecurityDeposit + bikeSecurityDeposit));

        System.out.println();
        System.out.println("Total Amount : "+(cautionDeposit + totalRent + totalSecurityDeposit));

        String option = console.readLine("Are you sure about that ? (Type yes) : ").toLowerCase();

        if(option.equals("yes")){
            if(cautionDeposit != 0){
                String cautionPaymentId = payment.addBorrowerPayment(borrowerId, null, cautionDeposit);
                updateAllTableDetails(cautionPaymentId, 0);
            }

            if(!CarId.equals("")){
                String carPaymentId = payment.addBorrowerPayment(borrowerId, CarId, carRent + carSecurityDeposit);
                updateAllTableDetails(carPaymentId, 1);
            }

            if(!BikeId.equals("")){
                String bikePaymentId = payment.addBorrowerPayment(borrowerId, BikeId, bikeRent + bikeSecurityDeposit);
                updateAllTableDetails(bikePaymentId, 2);
            }

            return true;
        }

        else{
            return false;
        }
    }

    public void updateAllTableDetails(String paymentId, int type){

        // Updating borrower cart 

        try {
            dbConnector.excecuteUpdate(borrowerCartTableName, "payment_status = true", "borrower_id = "+borrowerId);
        } catch (Exception e) {
            
        }


        // Updating Vehicle Details Table
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        String tommorrow = sdf.format(new Date(System.currentTimeMillis() + 86400000));

        try {
            if(type == 1){
                dbConnector.excecuteUpdate("vehicle_details", "v_borrower_id = "+borrowerId, "v_id = '"+CarId+"'");
                dbConnector.excecuteUpdate("vehicle_details", "v_rented_date = '"+today+"'", "v_id = '"+CarId+"'");
                dbConnector.excecuteUpdate("vehicle_details", "v_return_date = '"+tommorrow+"'", "v_id = '"+CarId+"'");
            }
            
            if(type == 2){
                dbConnector.excecuteUpdate("vehicle_details", "v_borrower_id = "+borrowerId, "v_id = '"+BikeId+"'");
                dbConnector.excecuteUpdate("vehicle_details", "v_rented_date = '"+today+"'", "v_id = '"+BikeId+"'");
                dbConnector.excecuteUpdate("vehicle_details", "v_return_date = '"+tommorrow+"'", "v_id = '"+BikeId+"'");
            }
        } catch (Exception e) {
            
        }



        // Updating Rented Vehicle Details

        try {
            if(type == 1){
                String CarValues = "('"+CarId+"', "+borrowerId+", "+"0, 0, 2, 0)";
                dbConnector.excecuteInsert("rented_vehicles", CarValues);
            }

            if(type == 2){
                String bikeValues = "('"+BikeId+"', "+borrowerId+", "+"0, 0, 2, 0)";
                dbConnector.excecuteInsert("rented_vehicles", bikeValues);
            }
        } catch (Exception e) {
            
        }
    }

    public void reupdateAllTables(String vehicleId){

        // Updating borrower cart
        removeVehicle(vehicleId);
        
        if(BikeId.equals(vehicleId))
        BikeId = "";
        if(CarId.equals(vehicleId))
        CarId = "";

        // removing Payment Id
        try {
            dbConnector.excecuteDelete("payment_details", "borrower_id = "+borrowerId+" AND v_id = '"+vehicleId+"'");
        } catch (Exception e) {
            
        }

        if(BikeId.equals("") && CarId.equals("")){
            try {
                ResultSet cautionDepositId = dbConnector.excecuteSelect("payment_id", "payment_details", "borrower_id = "+borrowerId+" AND v_id = 'null'", null, null, null);
                if(cautionDepositId != null && cautionDepositId.next()){
                    dbConnector.excecuteDelete("payment_details", "payment_id = "+cautionDepositId.getString(1));
                }
            } catch (Exception e) {
                
            }
        }
            

        // Updating vehicle Details Table

        try {
            dbConnector.excecuteUpdate("vehicle_details", "v_borrower_id = null", "v_id = '"+vehicleId+"'");
            dbConnector.excecuteUpdate("vehicle_details", "v_rented_date = null", "v_id = '"+vehicleId+"'");
            dbConnector.excecuteUpdate("vehicle_details", "v_return_date = null", "v_id = '"+vehicleId+"'");
            
        } catch (Exception e) {
            
        }

        // Updating Rented Vehicles

        try {
            dbConnector.excecuteUpdate("rented_vehicles", "rented_returned = 3", "v_id = '"+vehicleId+"'");
        } catch (Exception e) {
            
        }
    }

    public int checkBorrowerPaymentState(String borrowerId){

        // Not paid -1
        // Paid but processing 0
        // Paid fully 1

        try {
            ResultSet checkPayment = dbConnector.excecuteSelect("payment_id, payment_status", "payment_details", "borrower_id = "+borrowerId, null, null, null);
            boolean isPaid = false;
            while(checkPayment != null && checkPayment.next()){
                String paymentId = checkPayment.getString(1);
                String paymentStatus = checkPayment.getString(2);

                if(paymentId.equals("") && paymentStatus.equals("")){
                    return -1;
                }

                else if(paymentStatus.equals("0")){
                    return 0;
                }

                else if(paymentStatus.equals("1")){
                    isPaid = true;
                }

            }
            if(isPaid){
                return 1;
            }
            else{
                return -1;
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public void displayBorrowerCart(){

        paymentStatus = checkBorrowerPaymentState(borrowerId);

        // Not Paid
        if(paymentStatus == -1){
            int loopLimiter = 0;
    
            while (loopLimiter < LOOP_MAX_LIMIT) {
                
                boolean printResultCar = false;
                boolean printResultBike = false;

                ResultSet carDetail = dbConnector.excecuteSelect("v_id, v_name, v_numberplate, v_rent, v_security_deposit, v_type", vehicleTableName, "v_id = '"+CarId+"'", null, null, null);
                try {
                    printResultCar = displayTable(carDetail);

                    if(!printResultBike && !printResultCar){
                        clearScreen();
                    }

                    // if(carDetail != null && carDetail.next()){
                    //     for (int i = 1; i <= 6; i++) {
                    //         System.out.print(carDetail.getString(i)+" ");
                    //     }
                    //     System.out.println();
                    // }
                } catch (SQLException e) {
                    
                }
                
                ResultSet bikeDetail = dbConnector.excecuteSelect("v_id, v_name, v_numberplate, v_rent, v_security_deposit, v_type", vehicleTableName, "v_id = '"+BikeId+"'", null, null, null);
                try {
                    printResultBike = displayTable(bikeDetail);
                    
                    if(!printResultBike && !printResultCar){
                        clearScreen();
                    }

                    // if(carDetail != null && bikeDetail.next()){
                    //     for (int i = 1; i <= 6; i++) {
                    //         System.out.print(bikeDetail.getString(i)+" ");
                    //     }
                    //     System.out.println();
                    // }
                } catch (SQLException e) {
                    
                }
    
                System.out.println();
                System.out.println();
                System.out.println();
                
                System.out.println("1. Remove a vehicle from the Cart (R/r)");
                System.out.println("2. Book your current Vehicles (B/b)");
                System.out.println("3. Exit to Main Menu (M/m) ");
    
                String option = console.readLine("Enter your option : ").toLowerCase();
    
                if(option.length() != 1 || !"rbm".contains(option)){
                    clearScreen();
                    loopLimiter++;
                    continue;
                }
    
                char choice = option.charAt(0);
    
                if(choice == 'r'){

                    if(!CarId.equals("") || !BikeId.equals("")){
    
                        String vehicleId = console.readLine("Enter the Vehicle Id : ");
        
                        ResultSet isPresent = dbConnector.excecuteSelect("v_id", borrowerCartTableName, "v_id = '"+vehicleId+"' AND borrower_id = "+borrowerId, null, null, null);
        
                        try {
                            if(isPresent != null && isPresent.next()){
                                boolean resultDelete = removeVehicle(vehicleId);
                                if(resultDelete){
                                    if(BikeId.equals(vehicleId))
                                    BikeId = "";
                                    if(CarId.equals(vehicleId))
                                    CarId = "";
                                    console.readLine("Vehicle Removed from cart ... (Press Enter)");
                                }
                                else{
                                    console.readLine("Sorry Please Try Again ... (Press Enter)");
                                    loopLimiter++;
                                }
                                clearScreen();
                                continue;
                            }
        
                            else{
                                console.readLine("Vehicle Id is Invalid .. (Press Enter)");
                                clearScreen();
                                loopLimiter++;
                                continue;
                            }
                        } catch (Exception e) {
                            console.readLine("Vehicle Id is Invalid .. (Press Enter)");
                            clearScreen();
                            loopLimiter++;
                            continue;
                        }
                    }

                    else{
                        console.readLine("No vehicles in your cart Press Enter ... ");
                        clearScreen();
                        loopLimiter++;
                        continue;
                    }
                }
    
                else if(choice == 'b'){
                    if(!CarId.equals("") || !BikeId.equals("")){
                        boolean result = bookVehicles();
                        if(result){
                            console.readLine("Booked Successfully :) Press enter to Exit ... ");
                            break;
                        }
                        else{
                            clearScreen();
                            loopLimiter++;
                            continue;
                        }
                    }

                    else{
                        console.readLine("No vehicles in your cart Press Enter ... ");
                        clearScreen();
                        loopLimiter++;
                        continue;
                    }
                }
    
                else if(choice == 'm'){
                    console.readLine("Press enter to Exit ... ");
                    break;
                }
    
            }
        }

        // Paid but processing
        else if(paymentStatus == 0){
            clearScreen();
            System.out.println("Your Payment is being Processed please wait :) .... ");
            console.readLine("Press Enter to Return to Main Menu .... ");
            return;
        }
        
        
        // Paid fully
        else if(paymentStatus == 1){
            int loopLimiter = 0;
            int returnStatus = 0;

            boolean printResultCar = false;
            boolean printResultBike = false;
            
            while (loopLimiter < LOOP_MAX_LIMIT) {
                ResultSet carDetail = dbConnector.excecuteSelect("v_id, v_name, v_numberplate, v_rent, v_security_deposit, v_type", vehicleTableName, "v_id = '"+CarId+"'", null, null, null);
                try {
                    printResultCar = displayTable(carDetail);

                    if(!printResultCar && !printResultBike){
                        clearScreen();
                        break;
                    }
                    // if(carDetail != null && carDetail.next()){
                        //     for (int i = 1; i <= 6; i++) {
                    //         System.out.print(carDetail.getString(i)+" ");
                    //     }
                    //     System.out.println();
                    // }
                } catch (SQLException e) {
                    
                }
                
                ResultSet bikeDetail = dbConnector.excecuteSelect("v_id, v_name, v_numberplate, v_rent, v_security_deposit, v_type", vehicleTableName, "v_id = '"+BikeId+"'", null, null, null);

                try {
                    printResultBike = displayTable(bikeDetail);

                    if(!printResultBike && !printResultCar){
                        clearScreen();
                        break;
                    }
                    // if(carDetail != null && bikeDetail.next()){
                    //     for (int i = 1; i <= 6; i++) {
                    //         System.out.print(bikeDetail.getString(i)+" ");
                    //     }
                    //     System.out.println();
                    // }
                } catch (SQLException e) {
                    
                }
                 
                
                System.out.println();
                System.out.println("1. Return your Vehicles ");
                System.out.println("2. Extend your rent ");
                System.out.println("3. Exit to Main Menu ");
                System.out.println();
                
                int option = 0;
                
                try {
                    option = Integer.parseInt(console.readLine("Enter your option (1/2/3) : "));
                    if(option != 1 && option != 2 && option != 3){
                        console.readLine("Invalid choice (Press Enter ...) ");
                        clearScreen();
                        loopLimiter++;
                    }
                } catch (Exception e) {
                    console.readLine("Invalid choice (Press Enter ...) ");
                    clearScreen();
                    loopLimiter++;
                }
                                
                // Return 
                
                if(option == 1){
                    
                    System.out.println();
                    String vehicleId = console.readLine("Enter the vehicle id: ");
                    ResultSet result = dbConnector.excecuteSelect("v_id", borrowerCartTableName, "v_id = '"+vehicleId+"'", null, null, null);
                    try {
                        if(result != null && result.next()){
                            returnStatus = rTable.checkRentedReturnStatus(vehicleId);

                            if(returnStatus == 0){
                                boolean updateReturnStatus = dbConnector.excecuteUpdate("rented_vehicles", "rented_returned = 1", "v_id = '"+vehicleId+"' AND rented_returned != 3");
                                if(updateReturnStatus){
                                    console.readLine("Vehicle Successfully returned Processing extra charges ... (Press Enter)");
                                    clearScreen();
                                    continue;
                                }
                            }

                            else if(returnStatus == 1){
                                console.readLine("Extra charges are being processed Press Enter .. ");
                                clearScreen();
                                loopLimiter++;
                                continue;
                            }

                            else if(returnStatus == 2){
                                try {
                                    ResultSet fineParameters = dbConnector.excecuteSelect("damage_level, distance_travelled, extension", "rented_vehicles", "v_id = '"+vehicleId+"'", null, null, null);

                                    while(fineParameters != null && fineParameters.next()){
                                        int damageLevel = Integer.parseInt(fineParameters.getString(1));
                                        int distanceTravelled = Integer.parseInt(fineParameters.getString(2));
                                        int extension = Integer.parseInt(fineParameters.getString(3));

                                        boolean fineResult = fineAmountPayment(vehicleId, damageLevel, distanceTravelled, extension);
                                        if(!fineResult){
                                            clearScreen();
                                            loopLimiter++;
                                            continue;
                                        }
                                    }
                                } catch (Exception e) {
                                    
                                }

                                // Reupdate All tables
                                reupdateAllTables(vehicleId);
                                break;
                            }
                            
                        }
                        else{
                            console.readLine("Invalid Vehicle Id (Press Enter ... )");
                            clearScreen();
                            loopLimiter++;
                            continue;
                        }
                    } catch (Exception e) {
                        console.readLine("Invalid Vehicle Id (Press Enter ... )");
                        clearScreen();
                        loopLimiter++;
                        continue;
                    }

                }

                // Extend 
                
                else if(option == 2){
                    
                    System.out.println();
                    String vehicleId = console.readLine("Enter the vehicle id: ");
                    ResultSet result = dbConnector.excecuteSelect("v_id", borrowerCartTableName, "v_id = '"+vehicleId+"'", null, null, null);
                    try {
                        if(result != null && result.next()){
                            returnStatus = rTable.checkRentedReturnStatus(vehicleId);

                            if(returnStatus == 0){
                                ResultSet extensionCount = dbConnector.excecuteSelect("extension", "rented_vehicles", "v_id = '"+vehicleId+"'", null, null, null);
                                while(extensionCount != null && extensionCount.next()){
                                    int extenCount = Integer.parseInt(extensionCount.getString(1));
                                    if(extenCount <= 0){
                                        console.readLine("Cannot extend beyond 2 times Please Return the vehicle ... (Press Enter)");
                                        clearScreen();
                                        loopLimiter++;
                                        continue;
                                    }
    
                                    dbConnector.excecuteUpdate("rented_vehicles", "extension = "+(extenCount-1), "v_id = '"+vehicleId+"'");
    
                                    console.readLine("Extra rent charges will be added to your final payment :) Enjoy your journey (Press Enter ... )");
                                    clearScreen();
                                    break;
                                }                              
                            }

                            else if(returnStatus == 1 || returnStatus == 2){
                                console.readLine("Vehicle Already Returned :( (Press Enter) ... ");
                                clearScreen();
                                loopLimiter++;
                                continue;
                            }

                        }

                        else{
                            console.readLine("Invalid Vehicle Id (Press Enter ... )");
                            clearScreen();
                            loopLimiter++;
                            continue;
                        }
                    } catch(Exception e){

                    }
                    
                }

                else if(option == 3){
                    console.readLine("Press Enter to Continue ... ");
                    clearScreen();
                    break;
                }
                
                else{
                    console.readLine("Invalid choice (Press Enter ...) ");
                    clearScreen();
                    loopLimiter++;
                    continue;
                }
            }
        }

    }

    public boolean fineAmountPayment(String vehicleId, int damageLevel, int distanceTravelled, int extension){
        int loopLimiter = 0;

        while (loopLimiter < LOOP_MAX_LIMIT) {
            int rent = 0;
            int security_deposit = 0;
            int totalCharge = 0;
            int returnAmount = 30_000;
    
            try {
                ResultSet vehicle = dbConnector.excecuteSelect("v_rent, v_security_deposit", vehicleTableName, "v_id = '"+vehicleId+"'", null, null, null);
                while (vehicle != null && vehicle.next()) {
                    rent = Integer.parseInt(vehicle.getString(1));
                    rent = (3 - extension) * rent;
                    security_deposit = Integer.parseInt(vehicle.getString(2));
                    totalCharge = rent + security_deposit;
                }
            } catch (Exception e) {
                console.readLine("Vehicle Id does not exist (Press enter ... )");
                return false;
            }

            try {
                ResultSet deposit = dbConnector.excecuteSelect("borrower_deposit", "borrower_accounts", "borrower_id = "+borrowerId, null, null, null);
                if(deposit != null && deposit.next()){
                    int oldDeposit = Integer.parseInt(deposit.getString(1));
                    returnAmount = oldDeposit == 0 ? 30_000 : oldDeposit;
                }
            } catch (Exception e) {
                
            }
    
            
            // Damage Amount Calculation
            if(damageLevel == 1){
                totalCharge = (int) (totalCharge * 0.2);
            }
            else if(damageLevel == 2){
                totalCharge = (int) (totalCharge * 0.5);
            }
            else if(damageLevel == 3){
                totalCharge = (int) (totalCharge * 0.75);
            }
    
            // Distance Travelled
            if(distanceTravelled > 500){
                totalCharge += (int) (totalCharge * 0.15);
            }
    
            System.out.println("Total Fine Amount : "+totalCharge);
    
            
            System.out.println();
            System.out.println();
            try {
                String options = console.readLine("Pay by Cash/Security Deposit (c/s) : ").toLowerCase();
                if(options.length() != 1 || !("c/s".contains(options))){
                    console.readLine("Invalid Choice (Press Enter) .. ");
                    return false;
                }

                if(options.equals("c")){
                    console.readLine("Cash Paid Successfully ... :) (Press Enter)");
                    clearScreen();
                }
                
                else if(options.equals("s")){
                    if(totalCharge< returnAmount){
                        returnAmount -= totalCharge;
                    }
                    else{
                        console.readLine("Total Fine Exceeds deposit Amount Remaining Amount of : "+(totalCharge - returnAmount)+" is to be paid in Cash (Press Enter to Continue ... )");
                        returnAmount = 0;
                    }
                    clearScreen();
                }
                
                boolean updateDeposit = dbConnector.excecuteUpdate("borrower_accounts", "borrower_deposit = "+returnAmount, "borrower_id = "+borrowerId);
                return updateDeposit;

            } catch (Exception e) {
                
            }
            System.out.println();
            System.out.println();
            
        }

        return true;
    }

}
