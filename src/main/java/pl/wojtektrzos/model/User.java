package pl.wojtektrzos.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    private int id;
    private String username;
    private String email;
    private String password;
    private int groupId;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        setPassword(password);
    }

    private User() {

    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        //ToDo add lib to hash
        // this.password = password;
        System.out.println("zahaszuj mnie jeszcze!");
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public static User findById(Connection conn, int id) throws SQLException {
        String querry = "SELECT username, email, id, password FROM users WHERE id=?";
        PreparedStatement sql = conn.prepareStatement(querry);
        sql.setInt(1, id);
        ResultSet rs = sql.executeQuery();
        if (rs.next()) {
            User user = new User();
            user.username = rs.getString("username");
            user.email = rs.getString("email");
            user.id = rs.getInt("id");
            user.password = rs.getString("password");
            return user;
        }else {
            return null;
        }

    }

    public void deleteUser(Connection conn) throws SQLException {
        if(id>0){
            PreparedStatement sql = conn.prepareStatement("DELETE FROM users WHERE id="+id);
            sql.executeUpdate();
            id=0;
        }
    }

    public static ArrayList<User> loadAllUsers(Connection conn) throws SQLException {
        ArrayList<User> users= new ArrayList<>();
        PreparedStatement sql = conn.prepareStatement("SELECT username, email, id, password FROM users;");
        ResultSet rs = sql.executeQuery();
        while (rs.next()){
            User user = new User();
            user.username = rs.getString("username");
            user.email = rs.getString("email");
            user.id = rs.getInt("id");
            user.password = rs.getString("password");
            users.add(user);
        }
        return users;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public void saveToDb(Connection conn) throws SQLException {
        if (id == 0) {
            insert(conn);
        } else {
            update(conn);
        }
    }

    private void update(Connection conn) throws SQLException {
        String querry = "UPDATE users SET username = ?, email=?, password=? WHERE id=?;";
        PreparedStatement sql = conn.prepareStatement(querry);
        sql.setString(1,username);
        sql.setString(2,email);
        sql.setString(3,password);
        sql.setInt(4,id);
        sql.executeUpdate();
    }

    private void insert(Connection conn) throws SQLException {
        String querry = "INSERT INTO users(username, email, password) VALUES (?,?,?);";
        PreparedStatement sql = conn.prepareStatement(querry, new String[]{"id"});
        sql.setString(1,username);
        sql.setString(2,email);
        sql.setString(3,password);
        sql.executeUpdate();
        ResultSet rs = sql.getGeneratedKeys();
        if(rs.next()){
            id=rs.getInt(1);
        }
    }

}
