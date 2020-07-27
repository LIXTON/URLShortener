package com.lxn.urlshortener;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UrlShortcutDAO {
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory(UrlShortcut.class);
    private static final Session SESSION = SESSION_FACTORY.openSession();
    
    public static List<UrlShortcut> getAll() {
        return SESSION.createQuery("from UrlShortcut", UrlShortcut.class).list();
    }
    
    public static UrlShortcut findByAlias(String alias) {
        return SESSION.get(UrlShortcut.class, alias);
    }
    public static UrlShortcut findByUrl(String url) {
        List<UrlShortcut> urlShortcutList = SESSION.createQuery("from UrlShortcut where url=:url", UrlShortcut.class)
                .setParameter("url", url)
                .list();
        
        return urlShortcutList.isEmpty() ? null:urlShortcutList.get(0);
    }
    
    public static boolean save(UrlShortcut urlShortcut) {
        Transaction transaction = null;
        boolean result = true;
        try {
            transaction = SESSION.beginTransaction();
            SESSION.save(urlShortcut);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            result = false;
            e.printStackTrace();
        }
        
        return result;
    }
    
    private static SessionFactory buildSessionFactory(Class c) {
        return new Configuration()
                .configure()
                .addAnnotatedClass(c)
                .buildSessionFactory();
    }
}
