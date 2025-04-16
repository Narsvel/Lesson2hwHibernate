package org.ost.hibernatelessonsproject;

import org.ost.hibernatelessonsproject.dao.UserDAO;
import org.ost.hibernatelessonsproject.models.User;
import java.util.List;
import java.util.Scanner;

public class App {

    private static final String FIRST_MESSAGE = "Введите одну из команд: \n" +
            "create - создать пользователя \n" +
            "read - вывести данные определенного пользователя из базы данных \n" +
            "readAll - вывести данные всех пользователей из базы данных \n" +
            "update - обновить данные пользователея \n" +
            "delete - удалить пользователя из базы данных \n" +
            "exit - выйти из программы";

    private static final UserDAO userDAO = new UserDAO();
    private static boolean exit = true;

    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);

        while (exit) {
            System.out.println(FIRST_MESSAGE);
            String command = scanner.next();

            switch (command) {
                case "create": create(scanner);
                    break;
                case "read": read(scanner);
                    break;
                case "readAll": readAll();
                    break;
                case "update": update(scanner);
                    break;
                case "delete": delete(scanner);
                    break;
                case "exit": exit = false;
                    break;
                default:
                    System.out.println("Введена не корректная команда!");
            }
        }
    }

    private static void create(Scanner scanner) {
        System.out.println("Введите имя пользователя:");
        String name = scanner.next();
        System.out.println("Введите email пользователя:");
        String email = scanner.next();
        System.out.println("Введите возраст пользователя:");
        int age = scanner.nextInt();
        boolean create = userDAO.create(name, email, age);
        if (create)
            System.out.println("Пользователь добавлен в базу данных \n");
    }

    private static void readAll() {
        List<User> users = userDAO.readAll();
        System.out.println("Список пользователей: \n" + users + "\n");
    }

    private static void read(Scanner scanner) {
        System.out.println("Введите id пользователя:");
        int id = scanner.nextInt();
        User user = userDAO.read(id);
        if (user != null)
            System.out.println("Данные пользователя: " + user);
    }

    private static void update(Scanner scanner) {
        System.out.println("Введите id пользователя:");
        int id = scanner.nextInt();
        System.out.println("Введите имя пользователя:");
        String name = scanner.next();
        System.out.println("Введите email пользователя:");
        String email = scanner.next();
        System.out.println("Введите возраст пользователя:");
        int age = scanner.nextInt();
        User user = userDAO.update(id, name, email, age);
        if (user != null)
            System.out.println("Измененный пользователь: " + user);
    }

    private static void delete(Scanner scanner) {
        System.out.println("Введите id пользователя:");
        int id = scanner.nextInt();
        User user = userDAO.delete(id);
        if (user != null)
            System.out.println("Был удален пользователь: " + user);
    }

}
