import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        InputBuffer inputBuffer;
         Scanner scanner = new Scanner(System.in);
         while (true) {
             printPrompt();
             inputBuffer = readLine(scanner.nextLine().trim());
             if (inputBuffer.getBuffer().startsWith(".")) {
                 switch (do_meta_command(inputBuffer.getBuffer())) {
                     case META_COMMAND_SUCCESS:
                         continue;
                     case META_COMMAND_UNRECOGNIZED_COMMAND:
                         System.out.println("Unrecognized command '" + inputBuffer.getBuffer() + "'");
                         continue;
                 }
             }

             Statement statement = new Statement();

             switch (prepareStatement(inputBuffer.getBuffer())) {
                 case PREPARE_INSERT_SUCCESS:
                     statement.type = StatementType.STATEMENT_INSERT;
                     break;
                 case PREPARE_SELECT_SUCCESS:
                     statement.type = StatementType.STATEMENT_SELECT;
                     break;
                     case PREPARE_UNRECOGNIZED_STATEMENT:
                         System.out.println("Unrecognized statement '" + inputBuffer.getBuffer() + "'");
                         continue;
             }

             executeStatement(statement);
             System.out.println("Executed.");
         }
    }
    static void executeStatement(Statement statement) {
        switch (statement.type){
            case STATEMENT_INSERT:
                System.out.println("Insert statement");
                break;
                case STATEMENT_SELECT:
                    System.out.println("Select statement");
                    break;


        }
    }
    static PrepareResult prepareStatement(String command) {
        if (command.startsWith("insert")) {
                    return PrepareResult.PREPARE_INSERT_SUCCESS;
        }
        if (command.startsWith("select")) {
            return PrepareResult.PREPARE_SELECT_SUCCESS;
        }

        return  PrepareResult.PREPARE_UNRECOGNIZED_STATEMENT;
    }

    static MetaCommandResult do_meta_command(String command) {
        if (command.equals(".exit")) {
            System.exit(0);
            return MetaCommandResult.META_COMMAND_SUCCESS;
        } else {
            return MetaCommandResult.META_COMMAND_UNRECOGNIZED_COMMAND;
        }
    };

   static void printPrompt(){
        System.out.print("db > ");
    }

    static InputBuffer readLine(String input) {
        return new InputBuffer(input);
    }
}