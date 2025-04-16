package org.ost.hibernatelessonsproject;

import org.ost.hibernatelessonsproject.dao.UserDAO;
import org.ost.hibernatelessonsproject.models.User;

import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        UserDAO userDAO = new UserDAO();

        userDAO.create("Anton", "Anton@mail.ru", 28);
        userDAO.create("Bil", "Bil@mail.ru", 0);

        List<User> users = userDAO.readAll();
        System.out.println( users );

        User user = userDAO.update(3, "Tom", "Tom@mail.ru", 29);
        System.out.println( user );

        User user2 = userDAO.delete(8);
        System.out.println( user2 );
    }
}
