package Implementations;

import Interfaces.ChatServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer implements ChatServerInterface {
    private Map<String, UserThread> users= new HashMap();
    private Set<Chat> chats = new HashSet<>();

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

    public void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : users.values()) {
            if (aUser != excludeUser) {
                aUser.sendMessage("[Server] : " + message);
            }
        }
    }

    public UserThread findUserByName(String userName) {
        return users.get(userName);
    }

    public String findUserByThread(UserThread aUser) {
        return users.entrySet().stream().filter(entry -> aUser == entry.getValue()).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public Chat findChat(String userName1, String userName2) {
        return chats.stream().filter(chat -> (userName1.equals(findUserByThread(chat.getUser1())) &&
                                                userName2.equals(findUserByThread(chat.getUser2())))
                                            ||
                                                (userName1.equals(findUserByThread(chat.getUser2())) &&
                                                userName2.equals(findUserByThread(chat.getUser1()))))
                .findFirst().orElse(null);
    }

    public void addUser(String userName, UserThread aUser) {
        users.put(userName, aUser);
    }

    public void removeUser(String userName) {
        users.remove(userName);
        System.out.println("The user " + userName + " quited");
    }

    public boolean hasUsers() {
        return !this.users.isEmpty();
    }

    public Set<String> getUserNames() {
        return this.users.keySet();
    }
}
