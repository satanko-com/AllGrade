/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao.impl.hibernate;

import com.satanko.weballgrade.data.dao.SubjectDAO;
import com.satanko.weballgrade.data.model.Subject;
import com.satanko.weballgrade.service.PreparedServerResponses;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import org.hibernate.SessionFactory;

/**
 * This class provides an implementation of the {@link SubjectDAO} 
 * interface using the Hibernate Framework to perform interactions with a single database table. 
 * 
 * @author Julian Tropper (julian.tropper@gmail.com)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 * @see SubjectDAO
 */
public class HibernateSubjectDAO implements SubjectDAO{
    
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
    public List<Subject> getSubjectList() {
        try {
            sf.getCurrentSession().beginTransaction();

            List<Subject> subjectList = sf.getCurrentSession()
                    .createSQLQuery("SELECT * FROM AG_Subject")
                    .addEntity(Subject.class)
                    .list();
                    
            sf.getCurrentSession().getTransaction().commit();
            return subjectList;
        } catch (RuntimeException re) {
            sf.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(HibernateStudentDAO.class.getName()).log(Level.SEVERE, re.getMessage());
            throw new WebApplicationException(PreparedServerResponses.ERROR_DATABASE);
        }
    }
    
}