/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao.impl.hibernate;

import com.satanko.weballgrade.data.model.Subject;
import com.satanko.weballgrade.data.model.Teacher;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * This class is the "heart" of the Hibernate Framework in WebAllGrade. 
 * <p>
 * The class initialises and maintains the connetion to the server with the configuration found in <b>hibernate.cfg.xml</b>.
 * It does its initialization mainly in a static context. It also adds some data to the database in a static way.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class AllGradeDBUtil {

    /**
     * A field holding the session factory for the database connection.
     * <p>The field is initialized in a static way which means it should never be null.
     */
    private static SessionFactory sessionFactory;
    
    /**
     * As the name suggests, this field holds the last two digits of the current year.
     */
    private static final int yearInTwoDigits = Calendar.getInstance().get(Calendar.YEAR)%100;
    
    /**
     * A field that holds the url to the database.
     */
    private static final String databaseUrl = "jdbc:mysql://localhost:3306/";
    
    /**
     * This field is a teacher for testing purposes.<p>
     * To add the teacher to the database, either add the teacher manually to the database, 
     * or you can insert the following statement in the <b>addStaticDatabaseContent</b> method:<br>
     * <code> sessionFactory.getCurrentSession().saveOrUpdate(testTeacher); </code><p>
     * ATTENTION: Only add this statement once! If you attempt to insert the teacher a second time it will 
     * break all lessons in the database asociated with this teacher! 
     */
    private static final Teacher testTeacher = new Teacher("noi");
    
    static {
        try {
            String databaseName;
            
            if(Calendar.getInstance().get(Calendar.MONTH)>Calendar.SEPTEMBER)
            {
                databaseName = "DB_AllGrade_"+yearInTwoDigits+"_"+(yearInTwoDigits+1);
            }else
            {
                databaseName = "DB_AllGrade_"+(yearInTwoDigits-1)+"_"+yearInTwoDigits;
            }
            
            // Create the SessionFactory from the standard (hibernate.cfg.xml) config file.
            Configuration configuration = new Configuration()
                    .setProperty("hibernate.connection.url", databaseUrl+databaseName+"?createDatabaseIfNotExist=true")
                    .configure();
            
            sessionFactory = configuration.buildSessionFactory(
                    new ServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .buildServiceRegistry());
            
            addStaticDatabaseContent();
        } catch (Throwable ex) {
            Logger.getLogger(AllGradeDBUtil.class.getName()).log(Level.SEVERE, "Initial SessionFactory creation failed.{0}", ex.getMessage());
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * A method providing a session factory for the database connection.
     * 
     * @return A Hibernate {@link SessionFactory} object.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * This method holds statements to add content to the database.
     * The method is executed after the connection to the database is established.
     */
    private static void addStaticDatabaseContent()
    {
        try{
            sessionFactory.getCurrentSession().beginTransaction();
            
//            sessionFactory.getCurrentSession().saveOrUpdate(testTeacher);
            
            
            //ATTENTION: This is not a complete list of subjects and by all means not the best way
            //to add subjects to the database. Also the list is specific to a certain school.
            //Changing the following lines is recommended.
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("ADAT", "Angewandte Datentechnik"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("AM", "Angewandte Mathematik"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("APH", "Angewandte Physik"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("BESP", "Bewegung und Sport"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("BO", "Betriebliche Organisation"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("E", "Englisch"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("PRE", "Projektentwicklung"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("PRR", "Prozessregelung und Rechnerverbunde"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("PUC", "Programmieren"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("RW", "Rechnungswesen"));
            sessionFactory.getCurrentSession().saveOrUpdate(new Subject("SEP", "System- und Einsatzplanung"));
            
            sessionFactory.getCurrentSession().getTransaction().commit();
        }catch(RuntimeException re)
        {
            sessionFactory.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(AllGradeDBUtil.class.getName()).log(Level.SEVERE, "Runtime Exeption in method \"addStaticDatabaseContent()\" {0}", re.getMessage());
        }
    }
}