package org.ost.hibernatelessonsproject.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.ost.hibernatelessonsproject.models.User;

import java.util.List;

public class UserDAO {

    private SessionFactory createSessionFactory() {
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration().addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable exception) {
            System.out.println("Ошибка подключения к базе данных.");
        }
        return sessionFactory;
    }

    public boolean create(String name, String email, int age){
        SessionFactory sessionFactory = createSessionFactory();
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                User user = new User(name, email, age);
                session.save(user);
                session.getTransaction().commit();
                return true;
            } catch (ConstraintViolationException exception) {
                System.out.println("Возраст пользователя должен быть между 0 и 150 лет.");
            } catch (DataException exception) {
                System.out.println("Имя ползователя должно быть не более 100 символов.");
            }
        }
        return false;
    }

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
        if (user == null && sessionFactory != null) {
            System.out.println("Пользователя с этим id нет в базе данных. \n");
        }
        return user;
    }

    public List<User> readAll() {
        SessionFactory sessionFactory = createSessionFactory();
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                List<User> users = session.createQuery("select u from User u", User.class).getResultList();
                session.getTransaction().commit();
                return users;
            }
        }
        return null;
    }

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
            }
        }
        if (user == null && sessionFactory != null) {
            System.out.println("Пользователя с этим id нет в базе данных. \n");
        }
        return user;
    }

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
