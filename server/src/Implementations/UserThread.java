package Implementations;

import java.io.*;
import java.net.Socket;

public class UserThread extends Thread{
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;

    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            server.addUser(userName, this);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String option;
            do {
                String clientMessage;
                option = reader.readLine();
                if (option.equals("chat")) {
                    String otherUserName = reader.readLine();

                    Chat chat = server.findChat(userName, otherUserName);
                    if(chat == null) {
                        chat = new Chat(this, getUserWithName(otherUserName));
                    }

                    do {
                        clientMessage = reader.readLine();
                        server.messageInChat(chat, clientMessage, this);
                    } while (!clientMessage.equals("bye"));

                } else if (option.equals("group")) {

                }
            } while(!option.equals("quit"));

            server.removeUser(userName);
            socket.close();

            serverMessage = userName + " has quited.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    private UserThread getUserWithName(String userName) {
        return server.findUserByName(userName);
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}