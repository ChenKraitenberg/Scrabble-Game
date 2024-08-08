import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class IOSearcher {
    public static boolean search(String word, String... fileNames) {
        for (String filename : fileNames) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(word)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred when trying to read the file: " + filename);
                e.printStackTrace(); // It helps to trace the exception
            }
        }
        return false;
    }
}
