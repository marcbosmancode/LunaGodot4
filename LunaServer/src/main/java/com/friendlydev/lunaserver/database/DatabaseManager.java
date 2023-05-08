package com.friendlydev.lunaserver.database;

import com.friendlydev.lunaserver.resources.models.Account;
import com.friendlydev.lunaserver.scheduling.FutureEventManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 *
 * @author Marc
 */
public class DatabaseManager implements Runnable {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    public static final int HEARTBEAT_DELAY_MS = 60 * 1000 * 30; // 30 minute delay
    
    public static void init() {
        logger.info("Loading hibernate");
        
        // Configure and load hibernate
        HibernateUtility.getSessionFactory();
        
        // Add a heartbeat to keep the database connection alive
        FutureEventManager.getExecutor().scheduleWithFixedDelay(new DatabaseManager(), 0, HEARTBEAT_DELAY_MS, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void run() {
        getFromDB(Account.class, 1);
    }
    
    public static void saveInDB(Object ob) {
        logger.info("Saving object " + ob + " in the database");
        
        // Make sure that no other threads can alter the object while it saves to the database
        synchronized (ob) {
            try (Session session = HibernateUtility.getSessionFactory().openSession()) {
                session.beginTransaction();
                session.save(ob);
                session.getTransaction().commit();
            }
        }
    }
    
    public static void updateInDB(Object ob) {
        logger.info("Updating object " + ob + " in the database");
        
        synchronized (ob) {
            try (Session session = HibernateUtility.getSessionFactory().openSession()) {
                session.beginTransaction();
                session.update(ob);
                session.getTransaction().commit();
            }
        }
    }
    
    public static void saveOrUpdateInDB(Object ob) {
        logger.info("Saving (or updating) object " + ob + " in the database");
        
        synchronized (ob) {
            try (Session session = HibernateUtility.getSessionFactory().openSession()) {
                session.beginTransaction();
                session.saveOrUpdate(ob);
                session.getTransaction().commit();
            }
        }
    }
    
    public static void deleteFromDB(Object ob) {
        logger.info("Deleting object " + ob + " from the database");
        
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(ob);
            session.getTransaction().commit();
        }
    }
    
    public static Object getFromDB(Class clazz, int id) {
        String tableName = clazz.getSimpleName();
        
        Object ob = null;
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query query = session.createQuery(String.format("from %s where id = :id", tableName));
            query.setParameter("id", id);
            List list = query.list();
            if (list.size() >= 1) ob = list.get(0);
            session.getTransaction().commit();
        }
        return ob;
    }
    
    public static Object getFromDB(Class clazz, String columnName, Object value) {
        String tableName = clazz.getSimpleName();
        
        Object ob = null;
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query query = session.createQuery(String.format("from %s where %s = :value", tableName, columnName));
            query.setParameter("value", value);
            List list = query.list();
            if (list.size() >= 1) ob = list.get(0);
            session.getTransaction().commit();
        }
        return ob;
    }
    
    public static List getAllFromDB(Class clazz) {
        String tableName = clazz.getSimpleName();
        
        List result = null;
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query query = session.createQuery(String.format("from %s", tableName));
            result = query.list();
            session.getTransaction().commit();
        }
        if (result == null) return new ArrayList();
        return result;
    }
    
    public static List getListFromDB(Class clazz, String columnName, Object value) {
        String tableName = clazz.getSimpleName();
        
        List result = null;
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            session.beginTransaction();
            Query query = session.createQuery(String.format("from %s where %s = :value", tableName, columnName));
            query.setParameter("value", value);
            result = query.list();
            session.getTransaction().commit();
        }
        if (result == null) return new ArrayList();
        return result;
    }
    
}
