package pl.wojtektrzos.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserGroup {

    private String name;
    private int id;

    public UserGroup(String name) {
        this.name = name;
    }

    private UserGroup(){

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
            update(conn, id);
        }
    }

    private void update(Connection conn, int id) throws SQLException {
        String querry = "UPDATE user_group SET name = ?, WHERE id=?;";
        PreparedStatement sql = conn.prepareStatement(querry);
        sql.setString(1,name);
        sql.setInt(2, this.id);
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
}
