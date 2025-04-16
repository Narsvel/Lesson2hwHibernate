package org.ost.hibernatelessonsproject.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.hibernate.service.spi.ServiceException;
import org.ost.hibernatelessonsproject.models.User;

import java.util.List;

public class UserDAO {

    private SessionFactory createSessionFactory() {
        SessionFactory sessionFactory = null;
        try {
            Configuration configuration = new Configuration().addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
        } catch (ServiceException exception) {
            System.out.println("Ошибка подключения к базе данных.");
        }
        return sessionFactory;
    }

    public void create(String name, String email, int age){
        SessionFactory sessionFactory = createSessionFactory();
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                User user = new User(name, email, age);
                session.save(user);
                session.getTransaction().commit();
            } catch (ConstraintViolationException exception) {
                System.out.println("Возраст пользователя должен быть между 0 и 150 лет.");
            } catch (DataException exception) {
                System.out.println("Имя ползователя должно быть не более 100 символов.");
            }
        }
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
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                User user = session.get(User.class, id);
                user.setName(name);
                user.setEmail(email);
                user.setAge(age);
                session.getTransaction().commit();
                return user;
            } catch (NullPointerException exception) {
                System.out.println("Пользователя с этим id нет в базе данных.");
            }
        }
        return null;
    }

    public User delete(int id) {
        SessionFactory sessionFactory = createSessionFactory();
        if (sessionFactory != null) {
            try(sessionFactory) {
                Session session = sessionFactory.getCurrentSession();
                session.beginTransaction();
                User user = session.get(User.class, id);
                session.delete(user);
                session.getTransaction().commit();
                return user;
            } catch (IllegalArgumentException exception) {
                System.out.println("Пользователя с этим id нет в базе данных.");
            }
        }
        return null;
    }



}
