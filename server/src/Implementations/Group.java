package Implementations;

import java.util.HashSet;
import java.util.Set;

public class Group {
    private Set<UserThread> users = new HashSet<>();

    public Set<UserThread> getUsers() {
        return users;
    }
}
