import java.io.Console;
import java.sql.ResultSet;

public class RentedVehiclesTable implements Table{

    private SQLInterface dbConnector;

    private String rentedVehiclesTableName;
    private int LOOP_MAX_LIMIT = 1000;
    private Console console = System.console();
    
    public RentedVehiclesTable(){
        dbConnector = new SQLInterface();

        rentedVehiclesTableName = "rented_vehicles";

    }

    public void displayAdminRentedTable(){
        clearScreen();

        int loopLimiter = 0;

        while (loopLimiter < LOOP_MAX_LIMIT) {

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Displaying Rented Vehicles");
            System.out.println();
            System.out.println();
            System.out.println();

            try {
                ResultSet rentedVehicles = dbConnector.excecuteSelect("*", rentedVehiclesTableName, "rented_returned != 3", null, null, null);
    
                boolean printResult = displayTable(rentedVehicles);

                if(!printResult){
                    clearScreen();
                    console.readLine("Something went wrong Press Enter  ");
                    continue;
                }
                

                System.out.println();
                System.out.println("1. Calculate Fine Amount for Returned Vehicle (F/f) : ");
                System.out.println("2. Exit to MainMenu (M/m) : ");
                System.out.println();

                char choice = 'm';
                
                try {
                    String options = console.readLine("Enter your choice : ").toLowerCase();

                    if(options.length() != 1 || !"mef".contains(options)){
                        console.readLine("Invalid choice (Press Enter) .. ");
                        loopLimiter++;
                        clearScreen();
                        continue;
                    }
                    
                    choice = options.charAt(0);
                    
                } catch (Exception e) {
                    console.readLine("Something Went wrong (Press Enter) .. ");
                    loopLimiter++;
                    clearScreen();
                    continue;
                }
                
                if(choice == 'f'){

                    String vehicleId = console.readLine("Enter a rented Vehicle Id : ");

                    try {
                        ResultSet checkVehicle = dbConnector.excecuteSelect("*", "rented_vehicles", "v_id = '"+vehicleId+"' AND rented_returned != 3", null, null, null);
                        
                        if(checkVehicle != null && checkVehicle.next()){
                            int returnStatus = checkRentedReturnStatus(vehicleId);
                            if(returnStatus == 0){
                                console.readLine("Vehicle Not returned yet (Press Enter) .. ");
                                clearScreen();
                                loopLimiter++;
                                continue;
                            }

                            else if(returnStatus == 1){
                                String damage = "";
                                int damageLevel = 0;
                                int distanceTravelled = 0;
                                
                                System.out.println();
                                System.out.println();
                                
                                damage = console.readLine("Enter the Damage Level as Low/Medium/High (l/m/h) : ").toLowerCase();
    
                                if(damage.length() != 1 && !"lmh".contains(damage)){
                                    clearScreen();
                                    loopLimiter++;
                                    continue;
                                }
                                
                                damageLevel = damage.equals("l") ? 1 : damageLevel;
                                damageLevel = damage.equals("m") ? 2 : damageLevel;
                                damageLevel = damage.equals("h") ? 3 : damageLevel;
    
                                distanceTravelled = Integer.parseInt(console.readLine("Enter the distance for the current trip : "));
    
                                String check = console.readLine("Are you sure about the details ? (y/n) : ");
    
                                if(check.equals("y")){
                                    boolean updateDamageLevel = dbConnector.excecuteUpdate(rentedVehiclesTableName, "damage_level = "+damageLevel+", "+"distance_travelled = "+distanceTravelled+", "+"rented_returned = 2", "v_id = '"+vehicleId+"' AND rented_returned != 3");
                                    if(updateDamageLevel){
                                        console.readLine("Successfully Entered :) Press Enter");
                                        break;
                                    }
                                    else{
                                        console.readLine("Something Went wrong Sorry :( Press Enter");
                                        clearScreen();
                                        loopLimiter++;
                                        continue;
                                    }
                                }
                                else{
                                    clearScreen();
                                    loopLimiter++;
                                    continue;
                                }
                            }

                            else if(returnStatus == 2){
                                console.readLine("Fine already calculated (Press Enter to Continue ... )");
                                clearScreen();
                                loopLimiter++;
                                continue;
                            }

                            else{
                                console.readLine("Invalid Vehicle Id :( (Press Enter)");
                                clearScreen();
                                loopLimiter++;
                                continue;
                            }

                        }

                        else{
                            console.readLine("Invalid Vehicle Id :( Press Enter");
                            clearScreen();
                            loopLimiter++;
                            continue;
                        }

                    } catch (Exception e) {
                        console.readLine("Something went wrong try again :( Press Enter");
                        clearScreen();
                        loopLimiter++;
                        continue;
                    }

                    

                }
                
                else if(choice == 'm'){
                    console.readLine("Press enter to exit ... ");
                    break;
                }
                
            } catch (Exception e) {
                console.readLine("Something Went wrong (Press Enter) .. ");
                loopLimiter++;
                clearScreen();
                continue;
                
            }
        }


    }

    public void displayReturnedVehicles(){
        clearScreen();

        try {
            ResultSet resultSet = dbConnector.excecuteSelect("*", rentedVehiclesTableName, "rented_returned = 3", null, null, null);

            boolean printResult = displayTable(resultSet);

            if(!printResult){
                clearScreen();
                console.readLine("Something Went wrong please try again .. (Press Enter))");
                
            }

            console.readLine("Press Enter to exit  .. ");
        } catch (Exception e) {
            clearScreen();
            console.readLine("Something Went wrong please try again .. (Press Enter))");
        }

    }

    public int checkRentedReturnStatus(String vehicleId){

        // Rented 0
        // Return processing 1
        // Return processed 2
        // Rented previously 3

        try {
            ResultSet check = dbConnector.excecuteSelect("rented_returned", "rented_vehicles", "v_id = '"+vehicleId+"' AND rented_returned != 3", null, null, null);
            if(check != null && check.next()){
                int status = Integer.parseInt(check.getString(1));
                return status;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}
