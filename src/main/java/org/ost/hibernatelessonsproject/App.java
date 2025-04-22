package org.ost.hibernatelessonsproject;

import org.ost.hibernatelessonsproject.dao.UserDAO;
import org.ost.hibernatelessonsproject.models.User;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    //использую один логер для классов App и UserDAO (чтобы сохранять все логи в один файл)
    private static final Logger logger = Logger.getGlobal();

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

        try {
            //файл для сохранения логов
            Handler fileHandler = new FileHandler("C:\\MyIntelliJIDEA\\Lesson3hwTests\\myLog.log");
            logger.addHandler(fileHandler); //добавляем файл в логгер
            logger.setUseParentHandlers(false); //отключаем вывод логов в консоль
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
        }

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
        String idUser = scanner.next();
        if (idVerification(idUser)) { //проверка id
            int id = Integer.parseInt(idUser);
            User user = userDAO.read(id);
            if (user != null)
                System.out.println("Данные пользователя: " + user);
        } else read(scanner);
    }

    private static void update(Scanner scanner) {
        System.out.println("Введите id пользователя:");
        String idUser = scanner.next();
        if (idVerification(idUser)) { //проверка id
            int id = Integer.parseInt(idUser);
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
        } else update(scanner);
    }

    private static void delete(Scanner scanner) {
        System.out.println("Введите id пользователя:");
        String idUser = scanner.next();
        if (idVerification(idUser)) { //проверка id
            int id = Integer.parseInt(idUser);
            User user = userDAO.delete(id);
            if (user != null)
                System.out.println("Был удален пользователь: " + user);
        } else delete(scanner);
    }

    //метод для проверки корректности введенных данных пользователя
    private static boolean dataVerification(String name, String email, String age) {

        if (name.length() > 100) {
            System.out.println("Имя ползователя должно быть не более 100 символов. \n");
            logger.log(Level.INFO, "Не корректное имя пользователя");
            return false;
        }

        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            System.out.println("Email должен быть формата: " +
                    "(локальная часть адреса электронной почты)@(имя домена).(домен верхнего уровня) \n" +
                    "Например: User@mail.com \n");
            logger.log(Level.INFO, "Не корректный email пользователя");
            return false;
        }

        try {
            int i = Integer.parseInt(age);
            if (!(i > -1 && i < 150)) {
                System.out.println("Возраст пользователя должен быть между 0 и 150 лет. \n");
                logger.log(Level.INFO, "Не корректный возраст пользователя");
                return false;
            }
        } catch(NumberFormatException e){
            System.out.println("Введите возраст пользователя цифрами. \n");
            logger.log(Level.INFO, "Не корректный id пользователя");
            return false;
        }

        return true;
    }

    //проверка id
    private static boolean idVerification(String id) {
        try {
            Integer.parseInt(id);
            return true;
        } catch(NumberFormatException e){
            System.out.println("Введите id пользователя цифрами. \n");
            logger.log(Level.INFO, "Не корректный id пользователя");
            return false;
        }
    }

}
