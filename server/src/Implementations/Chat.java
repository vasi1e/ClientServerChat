package Implementations;

public class Chat {
    private UserThread user1;
    private UserThread user2;

    public Chat(UserThread user1, UserThread user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public UserThread getUser1() {
        return user1;
    }

    public UserThread getUser2() {
        return user2;
    }
}
