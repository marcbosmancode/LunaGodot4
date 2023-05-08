package com.friendlydev.lunaserver.database;

import com.friendlydev.lunaserver.resources.models.Account;
import com.friendlydev.lunaserver.resources.models.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Marc
 */
public class HibernateUtility {
    private static SessionFactory sessionFactory;
    
    // SessionFactory singleton
    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.configure();
            
            configuration.addAnnotatedClass(Account.class);
            configuration.addAnnotatedClass(Player.class);
            
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
    
}
