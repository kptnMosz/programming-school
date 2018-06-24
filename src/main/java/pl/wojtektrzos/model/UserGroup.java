package pl.wojtektrzos.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserGroup {
    private String name;
    private int id;

//    ----------===========konstruktory=========--------------
    public UserGroup(String name) {
        this.name = name;
    }

    private UserGroup(){
    }

//    -----------===============getery i settery========------------------
    public UserGroup setName(String name) {
        this.name = name;
        return this;
    }
    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }



    public static UserGroup findUserGroup(Connection conn, int id) throws SQLException {
        String querry = "SELECT id, name FROM user_group WHERE id=?";
        PreparedStatement sql = conn.prepareStatement(querry);
        sql.setInt(1, id);
        ResultSet rs = sql.executeQuery();
        if (rs.next()) {
            UserGroup group = new UserGroup();
            group.name = rs.getString("name");
            group.id = rs.getInt("id");
            return group;
        }else {
            return null;
        }
    }

    public void saveToDb(Connection conn) throws SQLException {
        if (id == 0) {
            insert(conn);
        } else {
            update(conn);
        }
    }

    private void update(Connection conn) throws SQLException {
        String querry = "UPDATE user_group SET name = ? WHERE id=?;";
        PreparedStatement sql = conn.prepareStatement(querry);
        sql.setString(1,name);
        sql.setInt(2, id);
        sql.executeUpdate();
    }

    private void insert(Connection conn) throws SQLException {
        String querry = "INSERT INTO user_group(name) VALUES (?);";
        PreparedStatement sql = conn.prepareStatement(querry, new String[]{"id"});
        sql.setString(1,name);
        sql.executeUpdate();
        ResultSet rs = sql.getGeneratedKeys();
        if(rs.next()){
            id=rs.getInt(1);
        }
    }

    public void deleteGroup(Connection conn) throws SQLException {
        if(id!=0){
            PreparedStatement sql = conn.prepareStatement("DELETE FROM user_group WHERE id=" + this.id);
            sql.executeUpdate();
        }
    }

    public static ArrayList<UserGroup> loadAllUserGroups(Connection conn) throws SQLException {
        ArrayList<UserGroup> userGroups= new ArrayList<>();
        PreparedStatement sql = conn.prepareStatement("SELECT name, id FROM user_group;");
        ResultSet rs = sql.executeQuery();
        while (rs.next()){
            UserGroup userGroup = new UserGroup();
            userGroup.name = rs.getString("name");
            userGroup.id = rs.getInt("id");
            userGroups.add(userGroup);
        }
        return userGroups;
    }

    public static boolean groupExists(Connection conn, int id) throws SQLException {
        PreparedStatement sql = conn.prepareStatement("SELECT id FROM user_group WHERE id = ?");
        sql.setInt(1, id);
        ResultSet rs = sql.executeQuery();
        if(rs.next()){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
