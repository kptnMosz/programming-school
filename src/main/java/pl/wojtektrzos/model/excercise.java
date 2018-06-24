package pl.wojtektrzos.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Excercise {

    private String title;
    private String description;
    private int id;

    public Excercise setName(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Excercise setDescription(String description) {
        this.description = description;
        return this;
    }



    private Excercise(){

    }
    public Excercise(String name, String description) {
        this.title = name;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public static Excercise findExcercise(Connection conn, int id) throws SQLException {
        String querry = "SELECT id, title, description FROM excecises WHERE id=?";
        PreparedStatement sql = conn.prepareStatement(querry);
        sql.setInt(1, id);
        ResultSet rs = sql.executeQuery();
        if (rs.next()) {
            Excercise exc = new Excercise();
            exc.title= rs.getString("title");
            exc.description = rs.getString("description");
            exc.id = rs.getInt("id");
            return exc;
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
        sql.setString(1,title);
        sql.setInt(2, this.id);
        sql.executeUpdate();
    }

    private void insert(Connection conn) throws SQLException {
        String querry = "INSERT INTO user_group(name) VALUES (?);";
        PreparedStatement sql = conn.prepareStatement(querry, new String[]{"id"});
        sql.setString(1,title);
        sql.executeUpdate();
        ResultSet rs = sql.getGeneratedKeys();
        if(rs.next()){
            id=rs.getInt(1);
        }
    }

    @Override
    public String toString() {
        return "Excercise{" +
                "name='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
