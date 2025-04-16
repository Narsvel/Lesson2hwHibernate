package org.ost.hibernatelessonsproject;

import org.ost.hibernatelessonsproject.dao.UserDAO;
import org.ost.hibernatelessonsproject.models.User;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        //цикл работы CRUD программы
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
                    System.out.println("Введена не корректная команда! \n");
            }
        }
    }

    private static void create(Scanner scanner) {
        System.out.println("Введите имя пользователя:");
        String name = scanner.next();
        System.out.println("Введите email пользователя:");
        String email = scanner.next();
        System.out.println("Введите возраст пользователя:");
        String age = scanner.next();
        if (dataVerification(name, email, age)) { //если данные корректны
            boolean create = userDAO.create(name, email, Integer.parseInt(age));
            if (create)
                System.out.println("Пользователь добавлен в базу данных. \n");
        } else create(scanner);
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
        String age = scanner.next();
        if (dataVerification(name, email, age)) { //если данные корректны
            User user = userDAO.update(id, name, email, Integer.parseInt(age));
            if (user != null)
                System.out.println("Измененный пользователь: " + user);
        } else update(scanner);
    }

    private static void delete(Scanner scanner) {
        System.out.println("Введите id пользователя:");
        int id = scanner.nextInt();
        User user = userDAO.delete(id);
        if (user != null)
            System.out.println("Был удален пользователь: " + user);
    }

    //метод для проверки корректности введенных данных пользователя
    private static boolean dataVerification(String name, String email, String age) {

        if (name.length() > 100) {
            System.out.println("Имя ползователя должно быть не более 100 символов. \n");
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Email должен быть формата: " +
                    "(локальная часть адреса электронной почты)@(имя домена).(домен верхнего уровня) \n" +
                    "Например: User@mail.com \n");
            return false;
        }

        try {
            int i = Integer.parseInt(age);
            if (!(i > -1 && i < 150)) {
                System.out.println("Возраст пользователя должен быть между 0 и 150 лет. \n");
                return false;
            }
        } catch(NumberFormatException e){
            System.out.println("Введите возраст пользователя цифрами. \n");
            return false;
        }

        return true;
    }

}
