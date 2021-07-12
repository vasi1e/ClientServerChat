package Implementations;

import Interfaces.ChatServerInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer implements ChatServerInterface {
    private int port;
    private Map<String, UserThread> users= new HashMap();
    private Set<Chat> chats = new HashSet<>();
    private Set<Group> groups = new HashSet<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Chat Server is listening on port " + port);

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

    @Override
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

    public Group findGroup(String [] userNames) {
        Set<UserThread> userThreads = new HashSet<>();
        Arrays.stream(userNames).forEach(name -> userThreads.add(findUserByName(name)));

        return groups.stream().filter(group -> group.getUsers().containsAll(userThreads) &&
                                                userThreads.containsAll(group.getUsers()))
                .findFirst().orElse(null);
    }

    public void addUser(String userName, UserThread aUser) {
        users.put(userName, aUser);
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public void addGroup(Group group) {
        groups.add(group);
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

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        ChatServer server = new ChatServer(port);
        server.execute();
    }
}
