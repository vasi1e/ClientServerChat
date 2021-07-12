package Implementations;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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
                        server.addChat(chat);
                    }

                    do {
                        clientMessage = reader.readLine();
                        server.messageInChat(chat, clientMessage, this);
                    } while (!clientMessage.equals("[" + userName + "]: bye"));

                    printUsers();
                } else if (option.equals("group")) {
                    String otherUserNames = reader.readLine();
                    otherUserNames += "," + userName;

                    Group group = server.findGroup(otherUserNames.split(","));
                    if(group == null) {
                        group = new Group(getUsersWithNames(otherUserNames.split(",")));
                        server.addGroup(group);
                    }

                    do {
                        clientMessage = reader.readLine();
                        server.messageInGroup(group, clientMessage, this);
                    } while (!clientMessage.equals("[" + userName + "]: bye"));

                    printUsers();
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

    private Set<UserThread> getUsersWithNames(String [] userNames) {
        return Arrays.stream(userNames).map(name -> server.findUserByName(name)).collect(Collectors.toSet());
    }

    public void sendMessage(String message) {
        writer.println(message);
    }
}
