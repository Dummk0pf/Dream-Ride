public interface Screen {
    
    default void clearScreen(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    default void clearLine(int lineCount){
        for (int i = 0; i < lineCount; i++) {
            System.out.print(String.format("\033[%dA",1));
            System.out.print("\033[2K");
        }
    }

}
