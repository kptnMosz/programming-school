package pl.wojtektrzos.model;

import java.sql.Connection;

public class User_group {

    private String name;
    private int id;

    public User_group(String name) {
        this.name = name;
    }

    private User_group(){

    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public static User_group findUserGroup(Connection conn, int id) {

        return null;
    }
}
