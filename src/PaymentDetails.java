import java.io.Console;
import java.sql.ResultSet;

public class PaymentDetails {
    
    private SQLInterface dbConnector;

    private int LOOP_MAX_LIMIT = 1000;
    private Console console = System.console();
    private String paymentDetailTableName;

    public PaymentDetails(){
        dbConnector = new SQLInterface();
        paymentDetailTableName = "payment_details";
    }

    public String addBorrowerPayment(String borrowerId, String vehicle_id, int amount){
        try {
            String columnNames = "(payment_status, amount_paid, amount_pending, borrower_id, v_id)";
            String values = "( false, 0, "+amount+" ,"+borrowerId+", '"+vehicle_id+"'"+")";
            boolean isAdded = dbConnector.excecuteInsert(paymentDetailTableName, columnNames, values);

            if(isAdded){
                ResultSet getPaymentId = dbConnector.excecuteSelect("payment_id", paymentDetailTableName, "borrower_id = "+borrowerId, null, null, null);
                if(getPaymentId != null && getPaymentId.next())
                return getPaymentId.getString(1);
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public void displayAdminPaymentDetails(){

        clearScreen();
        
        int loopLimiter = 0;
        
        while (loopLimiter < LOOP_MAX_LIMIT) {
    
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("Displaying Payment Details");
            System.out.println();
            System.out.println();
            System.out.println();

            try {
                ResultSet paymentDetails = dbConnector.excecuteSelect("*", paymentDetailTableName, null, null, null, null) ;

                while(paymentDetails != null && paymentDetails.next()){
                    for (int i = 1; i <= 6; i++) {
                        System.out.print(paymentDetails.getString(i)+" ");
                    }

                    System.out.println();
                }

                char option = 'm';

                System.out.println();
                System.out.println();
                System.out.println("1. Process Payment (P/p) : ");
                System.out.println("2. Exit to Main Menu (M/n) : ");

                try {
                    String options = console.readLine("Enter your Option : ").toLowerCase();
                    if(options.length() != 1 || !"pm".contains(options)){
                        loopLimiter++;
                        clearScreen();
                        continue;
                    }
                    
                    option = options.charAt(0);
                    
                } catch (Exception e) {
                    loopLimiter++;
                    clearScreen();
                    continue;
                }
                
                if(option == 'p'){
                    try {
                        String paymentId = console.readLine("Enter the payment Id to be processed : ");

                        ResultSet checkPaymentId = dbConnector.excecuteSelect("*", paymentDetailTableName, "payment_id = "+paymentId, null, null, null);

                        if(checkPaymentId != null && checkPaymentId.next()){
                            String completeProcess = console.readLine("Are you sure about that ? (y/n)");

                            if(completeProcess.length() != 1 || !"yn".contains(completeProcess)){
                                loopLimiter++;
                                clearScreen();
                                continue;
                            }

                            if(completeProcess.equals("y")){
                                int amountPending = 0;
                                int paymentStatus = 0;

                                paymentStatus = Integer.parseInt(checkPaymentId.getString(2));
                                amountPending = Integer.parseInt(checkPaymentId.getString(4));

                                boolean updateAmountPending = dbConnector.excecuteUpdate(paymentDetailTableName, "amount_pending = 0", "payment_id = "+paymentId);
                                boolean updateAmountPaid = dbConnector.excecuteUpdate(paymentDetailTableName, "amount_pending = "+amountPending, "payment_id = "+paymentId);
                                boolean updatePaymentStatus = dbConnector.excecuteUpdate(paymentDetailTableName, "payment_status = true", "payment_id = "+paymentId);

                            }
                        }

                        else{
                            console.readLine("Invalid Payment Id :( .. Press Enter ... ");
                            loopLimiter++;
                            clearScreen();
                            continue;
                        }
                    } catch (Exception e) {
                        console.readLine("Something Went wrong Press Enter ... ");
                        loopLimiter++;
                        clearScreen();
                        continue;
                    }
                }

                else if(option == 'm'){
                    console.readLine("Press Enter to Exit ... ");
                    break;
                }
            } catch (Exception e) {
                
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
