import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InputBuffer inputBuffer;
         Scanner scanner = new Scanner(System.in);
         while (true) {
             printPrompt();
             inputBuffer = readLine(scanner.nextLine());
             if (inputBuffer.getBuffer().equals(".exit")) {
                 break;
             } else {
                 System.out.println("Unrecognized command: " + inputBuffer.getBuffer());
             }
         }
    }

   static void printPrompt(){
        System.out.print("db > ");
    }

    static InputBuffer readLine(String input) {
        return new InputBuffer(input);
    }

}