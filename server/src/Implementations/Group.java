package Implementations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Group {
    private Set<UserThread> users = new HashSet<>();

    public Group(Set<UserThread> users) {
        this.users = users;
    }

    public Set<UserThread> getUsers() {
        return users;
    }
}
