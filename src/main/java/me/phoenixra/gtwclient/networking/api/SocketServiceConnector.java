package me.phoenixra.gtwclient.networking.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SocketServiceConnector {

    private final String SERVER_ADDRESS;
    private final int PORT;

    public SocketServiceConnector(String host, int port) {
        this.SERVER_ADDRESS = host;
        this.PORT = port;
    }

    public void sendAndRead(String message) {
        new Thread(() -> {
            try (
                    Socket socket = new Socket(SERVER_ADDRESS, PORT);

                    // Get the input and output streams for communication
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            ) {

                writer.println(message);
                writer.flush();

                // Read the response from the server
                Stream<String> response = reader.lines();
                System.out.println("Response from server: " + response);

                onResponseReceived(message, response.collect(Collectors.toList()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    protected abstract void onResponseReceived(String message, List<String> response);


}
