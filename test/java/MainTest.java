import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    public static void listDirectory() {
        String currentPath = System.getProperty("user.dir");
        File currentDir = new File(currentPath);

        System.out.println("Current directory: " + currentPath);
        System.out.println("\nContents:");
        File[] files = currentDir.listFiles();

        if (files != null) {
            for (File file : files) {
                System.out.printf("%s  %s%n",
                        file.isDirectory() ? "DIR" : "FILE",
                        file.getName());
            }
        }
    }
    public String[] runScript(List<String> commands) throws Exception {
//        MainTest.listDirectory();
        ProcessBuilder processBuilder = new ProcessBuilder("./new_db");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        try (OutputStream outputStream = process.getOutputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            for (String command : commands) {
                outputStream.write((command + "\n").getBytes());
                outputStream.flush();
            }
            outputStream.write(".exit\n".getBytes());
            outputStream.flush();

            // Read entire output
            StringBuilder rawOutput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                rawOutput.append(line).append("\n");
            }
            System.out.println(rawOutput.toString());

            return rawOutput.toString().split("\n");
        }

    }


    @Test
    public void testInsertAndRetrieveRow() throws Exception {

        String[] result = runScript(List.of(
                "insert 1 2444 35555",
                "select"
        ));

        String[] expected = {
                "db > Executing insert.",
                "Executed.",
                "db > Executing select.",
                "id 1 username 2444 email 35555",
                "Executed.",
                "db > "
        };

        assertArrayEquals(expected, result);

    }

    @Test
    public void testInsert1400Rows() throws Exception {
        List<String> commands = new ArrayList<>();
        for (int i = 0; i < 1401; i++) {
            commands.add("insert " + i + " user"+ i+ " person#"+i+"@example.com");
        }
        String[] result = runScript(commands);
        String expected = "Table is full";
        assertEquals(expected, result[result.length-3]);
    }
}