package Implementations;

import Interfaces.ChatServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer implements ChatServerInterface {

    @Override
    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(1001)) {

            System.out.println("Chat Server is listening on port " + 1001);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New user connected");

                UserThread newUser = new UserThread(socket, this);
                newUser.start();
            }

        } catch (IOException ex) {
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void messageInGroup(Group group, String message, UserThread excludeUser) {
        for (UserThread aUser : group.getUsers()) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    @Override
    public void messageInChat(Chat chat, String message, UserThread excludeUser) {
        if(chat.getUser1() != excludeUser) {
            chat.getUser1().sendMessage(message);
        } else {
            chat.getUser2().sendMessage(message);
        }
    }
}
