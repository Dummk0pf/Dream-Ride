import java.io.Console;

public class Main implements Screen {

    private static int LOOP_MAX_LIMIT = 1000;

    private static Console console = System.console();
    public static void main(String[] args) throws Exception {
        Main m = new Main();
        m.startApp();
    }

    public void startApp(){
        int loopLimiter = 0;
        while (loopLimiter < LOOP_MAX_LIMIT) {
            clearScreen();
            try{
    
                System.out.println("\r\n" + //
                    "   ___                       ___  _    __   \r\n" + //
                    "  / _ \\_______ ___ ___ _    / _ \\(_)__/ /__ \r\n" + //
                    " / // / __/ -_) _ `/  ' \\  / , _/ / _  / -_)\r\n" + //
                    "/____/_/  \\__/\\_,_/_/_/_/ /_/|_/_/\\_,_/\\__/ \r\n" + //
                    "                                            \r\n" + //
                    "");
                System.out.println("1. Admin");
                System.out.println("2. Renter");
                System.out.println("3. Exit");
                System.out.print("Enter your choice(1/2/3): ");
            
                int option = Integer.parseInt(console.readLine());
    
                
                if(option == 1){
                    // Admin Side
                    AdminScreen adminScreen = new AdminScreen();
                    adminScreen.logIn();
                }
                
                else if(option == 2){
                    // Renter Side
                    BorrowerScreen borrowerScreen = new BorrowerScreen();
                    borrowerScreen.logIn();
                }
        
                else if(option == 3){
                    console.readLine("Press Enter to Continue ... ");
                    break;
                }
                else {
                    console.readLine("Invalid Option Press Enter to Continue ... ");
                    loopLimiter++;
                }
            }

            catch(Exception e){
                console.readLine("Invalid Option Press Enter to Continue ... ");
                loopLimiter++;
            }
        }
    }
}