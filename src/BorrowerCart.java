import java.io.Console;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BorrowerCart {

    private SQLInterface dbConnector;
    private String vehicleTableName;
    private String borrowerCartTableName;
    private HashMap<Integer, String> borrowerCartColumnName;
    private Console console = System.console();
    private int LOOP_MAX_LIMIT = 2000;

    private String CarId;
    private String BikeId;
    private String borrowerId;
    
    public BorrowerCart(String borrowerId){

        dbConnector = new SQLInterface();
        vehicleTableName = "vehicle_details";
        borrowerCartTableName = "borrower_cart";
        CarId = "";
        BikeId = "";
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
                String values = "('"+vehicleId+"',"+borrowerId+","+"null,"+type+")";
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
                String values = "('"+vehicleId+"',"+borrowerId+","+"null,"+type+")";
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

    public void displayBorrowerCart(){

        int loopLimiter = 0;

        while (loopLimiter < LOOP_MAX_LIMIT) {
            
            ResultSet carDetail = dbConnector.excecuteSelect("v_id, v_name, v_numberplate, v_rent, v_security_deposit", vehicleTableName, "v_id = '"+CarId+"'", null, null, null);
            try {
                if(carDetail != null && carDetail.next()){
                    for (int i = 1; i <= 5; i++) {
                        System.out.print(carDetail.getString(i)+" ");
                    }
                    System.out.println();
                }
            } catch (SQLException e) {
                
            }

            ResultSet bikeDetail = dbConnector.excecuteSelect("v_id, v_name, v_numberplate, v_rent, v_security_deposit", vehicleTableName, "v_id = '"+BikeId+"'", null, null, null);
            try {
                if(carDetail != null && bikeDetail.next()){
                    for (int i = 1; i <= 5; i++) {
                        System.out.print(bikeDetail.getString(i)+" ");
                    }
                    System.out.println();
                }
            } catch (SQLException e) {
                
            }

            console.readLine("Press enter ... ");

            break;

        }
    }
}
