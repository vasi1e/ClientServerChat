package Interfaces;

import Implementations.Chat;
import Implementations.Group;
import Implementations.UserThread;

public interface ChatServerInterface {
    public void broadcast(String message, UserThread excludeUser);
    public void messageInGroup(Group group, String message, UserThread excludeUser);
    public void messageInChat(Chat chat, String message, UserThread excludeUser);
}
