package client;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter your name: ");
        String userName = scanner.next();
        client.setUserName(userName);
        writer.println(userName);

        String command;
        do {
            System.out.print("\nChoose action by writing:\n" +
                    "1) \"chat\" for creating a chat\n" +
                    "2) \"group\" for creating a group\n" +
                    "3) \"quit\" for quiting the server\n");
            command = scanner.next();
            writer.println(command);

            if(command.equals("chat")) {
                System.out.print("\nEnter name of the person you want to chat: ");
                String otherUserName = scanner.next();
                writer.println(otherUserName);
            }

            String text;
            do {
                text = scanner.nextLine();
                writer.println("[" + userName + "]: " + text);
            } while (!text.equals("bye"));
        } while (!command.equals("quit"));

        try {
            socket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
