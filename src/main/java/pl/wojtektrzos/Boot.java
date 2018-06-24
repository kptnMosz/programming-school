package pl.wojtektrzos;

import pl.wojtektrzos.model.User;
import pl.wojtektrzos.model.UserGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class Boot {

    public static void main(String[] args) {

        try(Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab")){

           User user = User.findById(conn, 13);
           user.saveToDb(conn);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
