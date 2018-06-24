package pl.wojtektrzos.model;

import java.sql.*;
import java.util.ArrayList;

public class Solutions {

    private int id;
    private Date created;
    private Date updated;
    private String description;
    private int excerciseId;
    private int userId;

//    ------------=========konstryktory=============--------------------

    public Solutions(Date created, Date updated, String description, int excerciseId, int userId) {
        this.created = created;
        this.updated = updated;
        this.description = description;
        this.excerciseId = excerciseId;
        this.userId = userId;
    }

    private Solutions(){

    }

//    --------------==========gettery i setery============-----------------

    public int getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getDescription() {
        return description;
    }

    public int getExcerciseId() {
        return excerciseId;
    }

    public int getUserId() {
        return userId;
    }

    public Solutions setUpdated(Date updated) {
        this.updated = updated;
        return this;
    }

    public Solutions setDescription(String description) {
        this.description = description;
        return this;
    }

//    ---------------===========ładowanie z bazy===========-----------------
public static Solutions findById(Connection conn, int id) throws SQLException {
    String querry = "SELECT id, created, updated, description, excercise_id, user_id FROM solutions WHERE id=?";
    PreparedStatement sql = conn.prepareStatement(querry);
    sql.setInt(1, id);
    ResultSet rs = sql.executeQuery();
    if (rs.next()) {
        Solutions solution = new Solutions();
        solution.created = rs.getDate("created");
        solution.updated = rs.getDate("updated");
        solution.id = rs.getInt("id");
        solution.description = rs.getString("description");
        solution.excerciseId= rs.getInt("excercise_id");
        solution.userId= rs.getInt("user_id");
        return solution;
    }else {
        return null;
    }

}

    public static ArrayList<Solutions> loadAllUsers(Connection conn) throws SQLException {
        ArrayList<Solutions> solutions= new ArrayList<>();
        PreparedStatement sql = conn.prepareStatement("SELECT id, created, updated, description, excercise_id, user_id FROM solutions;");
        ResultSet rs = sql.executeQuery();
        while (rs.next()){
            Solutions solution = new Solutions();
            solution.created = rs.getDate("created");
            solution.updated = rs.getDate("updated");
            solution.id = rs.getInt("id");
            solution.description = rs.getString("description");
            solution.excerciseId= rs.getInt("excercise_id");
            solution.userId= rs.getInt("user_id");
            solutions.add(solution);
        }
        return solutions;
    }


//    -----------------------=====operacje na bazie danych=====--------------------
    public void saveToDb(Connection conn) throws SQLException {
        if (id == 0) {
            insert(conn);
        } else {
            update(conn);
        }
    }

    private void update(Connection conn) throws SQLException {
        String querry = "UPDATE solutions SET created = ?, updated = ?, description=?, excercise_id=?, user_id=? WHERE id=?;";
        PreparedStatement sql = conn.prepareStatement(querry);
        sql.setDate(1,created);
        sql.setDate(2,updated);
        sql.setString(3,description);
        sql.setInt(4,excerciseId);
        sql.setInt(5,userId);
        sql.executeUpdate();
    }

    private void insert(Connection conn) throws SQLException {
        String querry = "INSERT INTO solutions(created, updated, description, excercise_id, user_id) VALUES (?,?,?,?,?);";
        PreparedStatement sql = conn.prepareStatement(querry, new String[]{"id"});
        sql.setDate(1,created);
        sql.setDate(2,updated);
        sql.setString(3,description);
        sql.setInt(4,excerciseId);
        sql.setInt(5,userId);
        sql.executeUpdate();
        ResultSet rs = sql.getGeneratedKeys();
        if(rs.next()){
            id=rs.getInt(1);
        }
    }

    public void deleteSolutions(Connection conn) throws SQLException {
        if(id!=0){
            PreparedStatement sql = conn.prepareStatement("DELETE FROM solutions WHERE id=" + this);
            sql.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Solutions{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", description='" + description + '\'' +
                ", excerciseId=" + excerciseId +
                ", userId=" + userId +
                '}';
    }

}
