package com.codecool.restauratio.dao;

import com.codecool.restauratio.customException.ConnectToDBFailed;
import com.codecool.restauratio.models.users.User;
import com.codecool.restauratio.utils.DatabaseUtility;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class UserDao {
    private EntityManager em;
    private EntityTransaction transaction;

    public UserDao() {
        this.em  = DatabaseUtility.getEntityManager("restaurantPU");
        this.transaction = em.getTransaction();
    }

    public UserDao(EntityManager em) {
        this.em = em;
        this.transaction = em.getTransaction();
    }

    public List<User> getAll() throws ConnectToDBFailed {
        try {
            return em.createNamedQuery("getAllUser").getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConnectToDBFailed(e.getMessage());
        }
    }

    public User getById(Integer userId) throws ConnectToDBFailed {
        try {
            return em.find(User.class, userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConnectToDBFailed(e.getMessage());
        }
    }

    public void add(User user) throws ConnectToDBFailed {
        try {
            em.persist(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConnectToDBFailed(e.getMessage());
        }
    }

    public void remove(User user) throws ConnectToDBFailed {
        try {
            em.remove(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConnectToDBFailed(e.getMessage());
        }
    }
}
