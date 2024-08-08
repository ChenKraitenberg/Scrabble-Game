import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class MyServer {
    private final int port; // The port on which the server socket listens.
    private final ClientHandler ch; // The handler responsible for handling client connections.
    private ServerSocket serverSocket; // The server socket that accepts client connections.

    public MyServer(int port, ClientHandler ch ) {
        this.port = port;
        this.ch = ch;
    }

    // Starts a server that handles client connections in a separate thread.
    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                while (!Thread.currentThread().isInterrupted()) {
                    Socket clientSocket = serverSocket.accept();
                    ch.handleClient(clientSocket.getInputStream(), clientSocket.getOutputStream());
                }
            } catch (SocketException e) {
                // ServerSocket was closed, expected behavior
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //Closes the server socket safely.
    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace(); // Prints the stack trace of the exception to the standard error stream (System.err).
        }
    }
}
