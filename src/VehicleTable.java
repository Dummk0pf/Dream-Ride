import java.sql.ResultSet;

public class VehicleTable {
    private SQLInterface dbConnector;

    public VehicleTable(){
        dbConnector = new SQLInterface();
    }

    public void displayAdminTable(){

        clearScreen();

        ResultSet result = dbConnector.excecuteSelect("*", "vehicle_details", null, null, null, null);
        System.out.println("Displaying Table");


        try {

            // TODO: Implement Table Design
            
            while(result != null && result.next()){
                for (int i = 1; i <= 11; i++) {
                    System.out.print(result.getString(i)+" ");
                }
                System.out.println();
            }


        } catch (Exception e) {
            System.out.println("Class: VehicleTable Method: displayAdminTable");
        }


    }

    public void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
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