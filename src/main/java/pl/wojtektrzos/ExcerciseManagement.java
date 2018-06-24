package pl.wojtektrzos;

import pl.wojtektrzos.model.Excercise;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ExcerciseManagement {

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/programming_school?useSSL=false",
                "root",
                "coderslab");
             Scanner scan = new Scanner(System.in)) {
            while (true) {

                ArrayList<Excercise> excercises = Excercise.loadAll(conn);
                System.out.println("lista zadan:");
                for (Excercise excercise : excercises) {
                    System.out.println(excercise);
                }
                System.out.println("Wybierz jedną z opcji: (add/edit/delete/quit)");
                String opcja = scan.nextLine();
                switch (opcja) {
                    case "add":
                        addExcercise(conn);
                        break;
                    case "edit":
                        editExcercise(conn);
                        break;
                    case "delete":
                        deleteExcercise(conn);
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

    private static void deleteExcercise(Connection conn) {
        int id;
        Scanner scan = new Scanner(System.in);
        System.out.println("Podaj id zadania do usuniecia");
        while (!scan.hasNextInt()) {
            System.out.println("podaj ID zadania");
            scan.nextLine();
        }
        id = Integer.parseInt(scan.nextLine());
        try {
            Excercise excercise = Excercise.findById(conn, id);
            if (excercise == null) {
                System.out.println("nie znaleziono zadania");
                return;
            }
            excercise.deleteExcercise(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void editExcercise(Connection conn) {
        String title, description;
        int id;

        System.out.println("Podaj id zadania:");
        Scanner scan = new Scanner(System.in);
        while (!scan.hasNextInt()) {
            System.out.println("podaj ID zadania");
            scan.nextLine();
        }
        id = Integer.parseInt(scan.nextLine());

        Excercise excercise;

        try {
            excercise = Excercise.findById(conn, id);
            System.out.println("Podaj nazwę:");
            title = scan.nextLine();
            System.out.println("Podaj opis:");
            description = scan.nextLine();

            excercise.setTitle(title);
            excercise.setDescription(description);
            excercise.saveToDb(conn);
        } catch (SQLException e) {
            System.out.println("Nie znaleziono zadania");
            e.printStackTrace();
        }
    }

    private static void addExcercise(Connection conn) {
        String name, description;
        System.out.println("Podaj nazwę:");
        Scanner scan = new Scanner(System.in);
        name = scan.nextLine();

        System.out.println("Podaj opis:");
        description = scan.nextLine();

        Excercise excercise = new Excercise(name, description);

        try {
            excercise.saveToDb(conn);
        } catch (SQLException e) {
            System.out.println("Nie udało się");
            e.printStackTrace();
        }
    }
}
