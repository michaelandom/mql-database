import java.util.Arrays;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Table table = new Table();
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
                     Scanner command_scanner = new Scanner(inputBuffer.getBuffer());
                     String command_break_down = command_scanner.next();
                     if (!command_break_down.equals("insert")) {
                         System.out.println("Syntax Statement '" + inputBuffer.getBuffer() + "'");
                         continue;
                     }
                     statement.type = StatementType.STATEMENT_INSERT;
                     statement  = extract_data(command_scanner,statement,inputBuffer);
                     if(statement == null){
                         continue;
                     }
                     break;
                 case PREPARE_SELECT_SUCCESS:
                     statement.type = StatementType.STATEMENT_SELECT;
                     break;
                     case PREPARE_UNRECOGNIZED_STATEMENT:
                         System.out.println("Unrecognized statement '" + inputBuffer.getBuffer() + "'");
                         continue;
             }

             if(executeStatement(statement,table)) {
                 System.out.println("Executed.");
             }
         }
    }
    static Statement extract_data(Scanner command_scanner,Statement statement,InputBuffer inputBuffer ) {
        int id;
        String email;
        String username;
        if (command_scanner.hasNextInt()){
            id = command_scanner.nextInt();
        } else {
            System.out.println("Syntax Statement '" + inputBuffer.getBuffer() + "'");
            return null;
        }

        if (command_scanner.hasNext()){
            username = command_scanner.next();
        } else {
            System.out.println("Syntax Statement '" + inputBuffer.getBuffer() + "'");
            return null;

        }

        if (command_scanner.hasNext()){
            email = command_scanner.next();
        } else {
            System.out.println("Syntax Statement '" + inputBuffer.getBuffer() + "'");
            return null;
        }

        statement.row =new  Row(
                id,
                username,
                email
        );

        return statement;
    }
    static void execute_insert(Statement statement,Table table){
        System.out.println("Executing insert.");
        table.addRow(statement.row);
    }

    static void execute_select(Statement statement,Table table){
        System.out.println("Executing select.");
        int total_number_page = (table.getNumRows() + table.getRowsPerPage()  - 1) / table.getRowsPerPage();
        for(int i =0 ; i < total_number_page ;i++) {
            Row[] rows = table.getAllRowsFromPage(i);
            Arrays.stream(rows).iterator().forEachRemaining(row -> {
                if (row != null) {
                    System.out.println("id " + row.getId() + " username " + row.getUsername() + " email " + row.getEmail());
                }
            });
        }
    }


    static boolean executeStatement(Statement statement,Table table) {
        switch (statement.type){
            case STATEMENT_INSERT:
                execute_insert(statement, table);
                return true;
                case STATEMENT_SELECT:
                     execute_select(statement, table);
                    return true;
        }
        return false;
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