package org.ost.hibernatelessonsproject.dao;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.ost.hibernatelessonsproject.models.User;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserDAO {

    private static final Logger logger = Logger.getGlobal();

    //метод предоставляет SessionFactory и обрабатывает ошибки связанные с подключением к бд
    private SessionFactory createSessionFactory() {
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration().addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable exception) {
            System.out.println("Ошибка подключения к базе данных.");
            logger.log(Level.SEVERE, "Exception: ", exception);
        }
        return sessionFactory;
    }

    //метод сохранения объекта в бд, возвращает true если объект был сохранен в бд
    public boolean create(String name, String email, int age){
        SessionFactory sessionFactory = createSessionFactory();
        if (sessionFactory != null) {  //если нет проблем с соединением с бд
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();                //начало транзакции
                User user = new User(name, email, age);
                session.save(user);
                session.getTransaction().commit();         //завершение транзакции
                return true;
            } catch (ConstraintViolationException | DataException exception) {
                System.out.println("Введены не корректные данные пользователя.");
                logger.log(Level.WARNING, "Exception: ", exception);
            }
        }
        return false;
    }

    //метод возвращает по id объект из бд
    public User read(int id) {
        SessionFactory sessionFactory = createSessionFactory();
        User user = null;
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                user = session.get(User.class, id);
                session.getTransaction().commit();
            }
        }
        if (user == null && sessionFactory != null) { //если было устновлено соединение с бд, но поьзователь не был получен
            System.out.println("Пользователя с этим id нет в базе данных. \n");
        }
        return user;
    }

    //метод возвращает все объекты которые есть в бд
    public List<User> readAll() {
        SessionFactory sessionFactory = createSessionFactory();
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                //HQL запрос всех строк из таблици
                List<User> users = session.createQuery("select u from User u", User.class).getResultList();
                session.getTransaction().commit();
                return users;
            }
        }
        return null;
    }

    //метод обновляет данные объекта в бд по id, возвращает обновленного пользователя
    public User update(int id, String name, String email, int age) {
        SessionFactory sessionFactory = createSessionFactory();
        User user = null;
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                user = session.get(User.class, id);
                if (user != null) {
                    user.setName(name);
                    user.setEmail(email);
                    user.setAge(age);
                }
                session.getTransaction().commit();
            } catch (ConstraintViolationException | DataException exception) {
                System.out.println("Введены не корректные данные пользователя.");
                logger.log(Level.WARNING, "Exception: ", exception);
            }
        }
        if (user == null && sessionFactory != null) {
            System.out.println("Пользователя с этим id нет в базе данных. \n");
        }
        return user;
    }

    //метод удаляет данные объекта в бд по id, возвращает данные удаленного пользователя
    public User delete(int id) {
        SessionFactory sessionFactory = createSessionFactory();
        User user = null;
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                user = session.get(User.class, id);
                if (user != null)
                    session.delete(user);
                session.getTransaction().commit();
            }
        }
        if (user == null  && sessionFactory != null) {
            System.out.println("Пользователя с этим id нет в базе данных. \n");
        }
        return user;
    }



}
