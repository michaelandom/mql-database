import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
public class DatabaseTest {

    public String[] runScript(List<String> commands) throws Exception {

        ProcessBuilder processBuilder = new ProcessBuilder("./new_db.jar");
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
                "insert 1 user1 person1@example.com",
                "select"
        ));

        String[] expected = {
                "db > Executed.",
                "db > (1, user1, person1@example.com)",
                "Executed.",
                "db > "
        };

        assertArrayEquals(expected, result);

    }

}
