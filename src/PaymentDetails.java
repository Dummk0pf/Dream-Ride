import java.sql.ResultSet;

public class PaymentDetails {
    
    private String paymentDetailTableName;
    private SQLInterface dbConnector;

    public PaymentDetails(){
        dbConnector = new SQLInterface();
        paymentDetailTableName = "payment_details";
    }

    public boolean addBorrowerPayment(String borrowerId, int amount){
        try {
            String columnNames = "(payment_status, amount_paid, amount_pending, borrower_id)";
            String values = "( false, 0, "+amount+" ,"+borrowerId+")";
            boolean isAdded = dbConnector.excecuteInsert(paymentDetailTableName, columnNames, values);

            return isAdded;
        } catch (Exception e) {
            return false;
        }
    }

    // public boolean checkBorrowerPaymentState(String borrowerId){
    //     try {
    //         ResultSet checkPayment = dbConnector.excecuteSelect("payment_details", borrowerId, borrowerId, borrowerId, borrowerId, borrowerId)

    //         return true;
    //     } catch (Exception e) {
            
    //         return false;
    //     }
    // }

}
