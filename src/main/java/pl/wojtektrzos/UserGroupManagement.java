package pl.wojtektrzos;

import pl.wojtektrzos.model.UserGroup;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserGroupManagement {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");
             Scanner scan = new Scanner(System.in)) {
            while (true) {

                ArrayList<UserGroup> userGroups = UserGroup.loadAllUserGroups(conn);
                System.out.println("lista grup:");
                for (UserGroup userGroup : userGroups) {
                    System.out.println(userGroup);
                }
                System.out.println("Wybierz jedną z opcji: (add/edit/delete/quit)");
                String opcja = scan.nextLine();
                switch (opcja) {
                    case "add":
                        addUserGroup(conn);
                        break;
                    case "edit":
                        editUserGroup(conn);
                        break;
                    case "delete":
                        deleteUserGroup(conn);
                        break;
                    case "quit":
                        return;
                    default:
                        System.out.println("Nieprawidlowa opcja, sprobuj jeszcze raz");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteUserGroup(Connection conn) {
        int id;
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj id grupy do usuniecia");
        while (!scan.hasNextInt()) {
            System.out.println("podaj ID grupy");
            scan.nextLine();
        }
        id = Integer.parseInt(scan.nextLine());
        try {
            UserGroup userGroup = UserGroup.findUserGroup(conn, id);
            if (userGroup == null) {
                System.out.println("nie znaleziono grupy");
                return;
            }
            userGroup.deleteGroup(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editUserGroup(Connection conn) {
        String name;
        int id;

        System.out.println("Podaj id grupy:");
        Scanner scan = new Scanner(System.in);
        while (!scan.hasNextInt()) {
            System.out.println("podaj ID grupy");
            scan.nextLine();
        }
        id = Integer.parseInt(scan.nextLine());

        UserGroup userGroup;

        try {
            userGroup = UserGroup.findUserGroup(conn, id);
            System.out.println("Podaj nazwę:");
            name = scan.nextLine();

            userGroup.setName(name);
            userGroup.saveToDb(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addUserGroup(Connection conn) {
        String name;
        System.out.println("Podaj nazwę:");
        Scanner scan = new Scanner(System.in);
        name = scan.nextLine();

        UserGroup userGroup = new UserGroup(name);

        try {
            userGroup.saveToDb(conn);
        } catch (SQLException e) {
            System.out.println("Nie udało się");
            e.printStackTrace();
        }
    }

}
