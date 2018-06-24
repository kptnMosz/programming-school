package pl.wojtektrzos;

import pl.wojtektrzos.model.User;
import pl.wojtektrzos.model.UserGroup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserManagement {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");
             Scanner scan = new Scanner(System.in)) {
            while (true) {

                ArrayList<User> users = User.loadAllUsers(conn);
                System.out.println("lista userów:");
                for (User user : users) {
                    System.out.println(user);
                }
                System.out.println("Wybierz jedną z opcji: (add/edit/delete/quit)");
                String opcja = scan.nextLine();
                switch (opcja) {
                    case "add":
                        addUser(conn);
                        break;
                    case "edit":
                        editUser(conn);
                        break;
                    case "delete":
                        deleteUser(conn);
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

    private static void deleteUser(Connection conn) {
        int id;
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj id uzytkownika do zmiany");
        while (!scan.hasNextInt()) {
            System.out.println("podaj ID użytkownika");
            scan.nextLine();
        }
        id = Integer.parseInt(scan.nextLine());
        try {
            User user = User.findById(conn, id);
            if (user == null) {
                System.out.println("nie znaleziono użytkownika");
                return;
            }
            user.deleteUser(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editUser(Connection conn) {
        String name, email, password;
        int id, group;
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj id uzytkownika do zmiany");
        while (!scan.hasNextInt()) {
            System.out.println("podaj ID użytkownika");
            scan.nextLine();
        }
        id = Integer.parseInt(scan.nextLine());
        User user;
        try {
            user = User.findById(conn, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        if (user == null) {
            System.out.println("nie znaleziono użytkownika");
            return;
        }
        System.out.println(user);

        System.out.println("Podaj nazwisko lub <enter> aby zachować starą wartosc:");
        name = scan.nextLine();
        if (!name.equals("")) {
            user.setUsername(name);
        }
        System.out.println("Podaj nowe haslo lub <enter> aby zachować starą wartosc:");
        password = scan.nextLine();
        if (!password.equals("")) {
            user.setPassword(password);
        }
        System.out.println("Czy zmienic e-mail? (t/n)");
        String kontrolkaMail = scan.nextLine();
        if ("t".equals(kontrolkaMail)) {
            email = getEmail(conn, scan, user.getId());
            user.setEmail(name);
        }
//        System.out.println("Podaj numer grupy:");
        group = getGroup(conn, scan);
        user.setGroupId(group);
        System.out.println("nowe dane: " + user + "\nKontynuować? t/n");
        String exec = scan.nextLine();
        if ("t".equals(exec)) {

            try {
                user.saveToDb(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static void addUser(Connection conn) {
        String name, email, password;
        int group;

        System.out.println("Podaj nazwisko:");
        Scanner scan = new Scanner(System.in);
        name = scan.nextLine();

        email = getEmail(conn, scan, -1);

        System.out.println("Podaj haslo:");
        password = scan.nextLine();

        group = getGroup(conn, scan);

        User user = new User(name, email, group, password);
        try {
            user.saveToDb(conn);
        } catch (SQLException e) {
            System.out.println("Błąd bazy danych");
            e.printStackTrace();
        }


    }

    private static int getGroup(Connection conn, Scanner scan) {
        System.out.println("Podaj id grupy:");

        while (!scan.hasNextInt()) {
            System.out.println("podaj ID grupy");
            scan.nextLine();
        }
        int group = Integer.parseInt(scan.nextLine());
        try {
            while (!UserGroup.groupExists(conn, group)) {
                System.out.println("podana grupa nie istnieje, sprobuj jeszcze raz");
                while (!scan.hasNextInt()) {
                    System.out.println("podaj ID grupy");
                    scan.nextLine();
                }
                group = Integer.parseInt(scan.nextLine());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return group;
    }

    private static String getEmail(Connection conn, Scanner scan, int id) { //dla nowych uzytkownikow podajemy -1
        System.out.println("Podaj e-mail:");
        String email = scan.nextLine();
        try {
            while (!(User.emailCheck(conn, email) == -1 || User.emailCheck(conn, email) == id)) {
                System.out.println("Podany mail istnieje juz w bazie, sproboj ponownie");
                email = scan.nextLine();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }
}
