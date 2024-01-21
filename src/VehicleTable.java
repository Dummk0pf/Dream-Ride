import java.io.Console;
import java.sql.ResultSet;
import java.util.HashMap;

public class VehicleTable implements Table{
    private SQLInterface dbConnector;
    private String vehicleTableName;
    private Console console = System.console();
    private HashMap<Integer, String> vehicleTableColumns;
    private int LOOP_MAX_LIMIT = 1000;

    public VehicleTable(){
        dbConnector = new SQLInterface();

        vehicleTableName = "vehicle_details";
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

    public void displayAdminTable(){

        clearScreen();

        try {
            // TODO: Implement Table Design

            ResultSet resultSet = dbConnector.excecuteSelect("*", vehicleTableName, null, null, null, null);   
            
            System.out.println("Displaying Table");
            
            displayTable(resultSet);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Class: VehicleTable Method: displayAdminTable");
        }
        
    }
    
    public void displayBorrowerTable(){
        clearScreen();
        
        try {
            
            String columnNames = "";
            
            for (int i = 1; i < 6; i++) {
                columnNames += vehicleTableColumns.get(i)+",";
            }
            
            columnNames += vehicleTableColumns.get(7);
            
            String whereCondition = "v_service_state = true AND v_borrower_id is null";
            
            ResultSet result = dbConnector.excecuteSelect(columnNames, vehicleTableName, whereCondition, null, null, null);

            boolean printResult = displayTable(result);

            if(!printResult){
                console.readLine("Something went wrong Press Enter  ");
                clearScreen();
            }
            
        } catch (Exception e) {
            System.out.println("Class: VehicleTable Method: displayBorrowerTable");
            
        }

    }

    public void displayUnServicedTable(){
        int loopLimiter = 0;

        while(loopLimiter < LOOP_MAX_LIMIT){

            clearScreen();
            
            try {
                ResultSet vehicles = dbConnector.excecuteSelect("v_id, v_name, v_numberplate, v_type", vehicleTableName, "v_service_state = false", null, null, null);
                
                System.out.println("Displaying Unserviced vehicles ");

                displayTable(vehicles);

                System.out.println("1. Service Vehicles : ");
                System.out.println("2. Exit             : ");

                int choice = Integer.parseInt(console.readLine("Enter your choice (1/2) : "));

                if(choice == 1){
                    String vehicleId = console.readLine("Enter the vehicle id : ");

                    ResultSet checkVId = dbConnector.excecuteSelect("v_id", vehicleTableName, "v_id = '"+vehicleId+"' and v_service_state = false", null, null, null);

                    if(checkVId != null && checkVId.next()){
                        
                        dbConnector.excecuteUpdate(vehicleTableName, "v_total_distance = 0", "v_id = '"+vehicleId+"'");
                        dbConnector.excecuteUpdate(vehicleTableName, "v_service_state = true", "v_id = '"+vehicleId+"'");

                        console.readLine("Vehicle Serviced Successfully :) (Press Enter) ");
                        continue;
                    }
                    else{
                        console.readLine("Invalid Vehicle Id (Press Enter) .. ");
                        loopLimiter++;
                        continue;
                    }
                }
                
                else if(choice == 2){
                    return;
                }

                else{
                    console.readLine("Invalid Choice (Press Enter) .. ");
                    loopLimiter++;
                    continue;
                }
                    
            } catch (Exception e) {
                console.readLine("Error Occurred in database (Press Enter) : ");
            }    
        }
    }
}





/* Vehicle table
 * v_id
 * v_name
 * v_numberplate
 * v_type
 * v_rent
 * v_total_distance
 * v_security_deposit
 * v_service_state
 * v_borrower_id
 * v_rented_date
 * v_return_date
 */

/*
 * ("00001", "Chevrolet Impala", "PD04BK5050", "Car", 4000, 50000, 10000, true, null, null, null),
 * ("00002", "Chevrolet Impala", "PD04BK5051", "Car", 4000, 50000, 10000, true, null, null, null),
 * ("00003", "Chevrolet Impala", "PD04BK5052", "Car", 4000, 50000, 10000, true, null, null, null),
 * ("00004", "Ford Cortina", "PD04GF5053", "Car", 3500, 45000, 10000, true, null, null, null),
 * ("00005", "Ford Cortina", "PD04GF5054", "Car", 3500, 45000, 10000, true, null, null, null),
 * ("00006", "Nissan Skyline", "PD04SK5055", "Car", 5500, 60000, 10000, true, null, null, null),
 * ("00007", "Nissan Skyline", "PD04SK5056", "Car", 5500, 60000, 10000, true, null, null, null),
 * ("00008", "Nissan Skyline", "PD04SK5057", "Car", 7500, 40000, 10000, true, null, null, null),
 * ("00009", "Lanica O37", "PD04CA5058", "Car", 6500, 55000, 10000, true, null, null, null),
 * ("00010", "Lanica O37", "PD04CA5059", "Car", 6500, 55000, 10000, true, null, null, null),
 * ("00100", "Honda CB Shine", "TP04JC4040", "Bike", 300, 70000, 3000, true, null, null, null),
 * ("00101", "Honda CB Shine", "TP04JC4041", "Bike", 300, 70000, 3000, true, null, null, null),
 * ("00102", "Honda CB Shine", "TP04JC4042", "Bike", 300, 70000, 3000, true, null, null, null),
 * ("00103", "Suzuki Zeus 150", "TP04RH4043", "Bike", 400, 50000, 3000, true, null, null, null),
 * ("00104", "Suzuki Zeus 150", "TP04RH4043", "Bike", 400, 50000, 3000, true, null, null, null),
 * ("00105", "Hero Glamour", "TP04JM4044", "Bike", 250, 80000, 3000, true, null, null, null),
 * ("00106", "Hero Glamour", "TP04JM4045", "Bike", 250, 80000, 3000, true, null, null, null),
 * ("00107", "Hero Glamour", "TP04JM4046", "Bike", 250, 80000, 3000, true, null, null, null),
 * ("00108", "Bajaj CT 100", "TP04ST4047", "Bike", 150, 70000, 3000, true, null, null, null),
 * ("00109", "Bajaj CT 100", "TP04ST4048", "Bike", 150, 70000, 3000, true, null, null, null),
*/