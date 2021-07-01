package Interfaces;

import Implementations.Chat;
import Implementations.Group;
import Implementations.UserThread;

public interface ChatServerInterface {
    public void execute();
    public void messageInGroup(Group group, String message, UserThread excludeUser);
    public void messageInChat(Chat chat, String message, UserThread excludeUser);
}
