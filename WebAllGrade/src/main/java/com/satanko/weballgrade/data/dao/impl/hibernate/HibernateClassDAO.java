/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao.impl.hibernate;

import com.satanko.weballgrade.data.dao.ClassDAO;
import com.satanko.weballgrade.data.model.Clazz;
import com.satanko.weballgrade.service.PreparedServerResponses;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import org.hibernate.SessionFactory;

/**
 * This class provides an implementation of the {@link ClassDAO} 
 * interface using the Hibernate Framework to perform interactions with a single database table. 
 * 
 * @author Julian Tropper (julian.tropper@gmail.com)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class HibernateClassDAO implements ClassDAO{
    
    /**
     * A field providing methods for interactions with the database via Hibernate<p>
     * Additional Information <a href=https://community.jboss.org/wiki/SessionsAndTransactions>...jboss.org/wiki/SessionsAndTransactions</a>
     * 
     * @see AllGradeDBUtil
     */
    private SessionFactory sf = AllGradeDBUtil.getSessionFactory();
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void updateAllClasses(ArrayList<String> classes) {
        try {
            sf.getCurrentSession().beginTransaction();
            for (int i = 0; i < classes.size(); i++) {
                sf.getCurrentSession().saveOrUpdate(new Clazz(classes.get(i)));
            }
            sf.getCurrentSession().getTransaction().commit();
        } catch (RuntimeException re) {
            sf.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(HibernateStudentDAO.class.getName()).log(Level.SEVERE, re.getMessage());
            throw new WebApplicationException(PreparedServerResponses.ERROR_DATABASE);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<Clazz> selectClassList() {
        try {
            sf.getCurrentSession().beginTransaction();

            List<Clazz> classList = sf.getCurrentSession()
                    .createSQLQuery("SELECT classToken FROM AG_Class")
                    .addEntity(Clazz.class)
                    .list();
                    
            sf.getCurrentSession().getTransaction().commit();
            return classList;
        } catch (RuntimeException re) {
            sf.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(HibernateStudentDAO.class.getName()).log(Level.SEVERE, re.getMessage());
            throw new WebApplicationException(PreparedServerResponses.ERROR_DATABASE);
        }
    }
}
