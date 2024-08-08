import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler {
    PrintWriter out; // Used for sending output through the socket to the client.
    Scanner in; // Used for receiving input through the socket from the client.

    // Handles the interaction with the client, processing the received commands, and sending back the responses.
    // inFromClient Input stream from the client
    // outToClient Output stream to the client
    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {

        // Create PrintWriter for writing to the client
        out=new PrintWriter(outToClient);

        // Create PrintWriter for writing to the client
        in=new Scanner(inFromclient);

        // Read the input from the client
        String text = in.next();

        // Split the input text by ',' to separate command and arguments
        String[] strings = text.split(",");

        // Check the command type (query or challenge)
        if (strings[0].equals("Q")) {
            // If is a query, extract the arguments and query the dictionary
            String[] queryArgs = Arrays.copyOfRange(strings, 1, strings.length);
            boolean result = DictionaryManager.get().query(queryArgs);
            // Send the query result back to the client
            out.println(result);

        } else { // Its challenge, extract the arguments and challenge the dictionary
            String[] challengeArgs = Arrays.copyOfRange(strings, 1, strings.length);
            boolean result = DictionaryManager.get().challenge(challengeArgs);
            // Send the challenge result back to the client
            out.println(result);
        }

        // Flush the PrintWriter to ensure the data is sent to the client, copied from thr main train
        out.flush();
    }

    // Close the Scanner and PrintWriter when the handler is closed
    @Override
    public void close() {
        in.close();
        out.close();
    }
}
