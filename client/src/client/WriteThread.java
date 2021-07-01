package client;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;

    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;

        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        Console console = System.console();

        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);

        String command;
        do {
            command = console.readLine("\nChoose action by writing:\n" +
                    "1) \"chat\" for creating a chat\n" +
                    "2) \"group\" for creating a group\n" +
                    "3) \"quit\" for quiting the server\n");
            writer.println(command);

            if(command.equals("chat")) {
                String otherUserName = console.readLine("\nEnter name of the person you want to chat: ");
                writer.println(otherUserName);
            }

            String text;
            do {
                text = console.readLine("[" + userName + "]: ");
                writer.println(text);

            } while (!text.equals("bye"));
        } while (!command.equals("quit"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
