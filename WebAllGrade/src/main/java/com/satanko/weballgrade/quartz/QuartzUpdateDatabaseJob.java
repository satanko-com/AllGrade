/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.quartz;

import com.satanko.weballgrade.controller.SourceDataProvider;
import com.satanko.weballgrade.controller.impl.LdapDataProvider;
import com.satanko.weballgrade.data.dao.ClassDAO;
import com.satanko.weballgrade.data.dao.StudentDAO;
import com.satanko.weballgrade.data.dao.TeacherDAO;
import com.satanko.weballgrade.data.dao.impl.hibernate.HibernateClassDAO;
import com.satanko.weballgrade.data.dao.impl.hibernate.HibernateStudentDAO;
import com.satanko.weballgrade.data.dao.impl.hibernate.HibernateTeacherDAO;
import com.satanko.weballgrade.data.model.Student;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

/**
 * This class is represents a complete database update which can be run multiple times to get
 * data from a {@link SourceDataProvider} and write it into a database.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * @author Julian Tropper (julian.tropper@gmail.com)
 * ATTENTION: If not otherwise speciefied, the author(s) of the class is also the author of all methods.
 * 
 * @version 1.0
 */
public class QuartzUpdateDatabaseJob implements InterruptableJob {
    
    /**
     * A boolean field indicating whether the job is currently running.
     */
    public static boolean IS_RUNNING = false;
    /**
     * A field holding the thread in which the job is running.
     */
    private Thread thread;
    
    private LdapDataProvider connector;
    /**
     * A field providing access to the database table that holds the information for classes
     */
    ClassDAO classDAO = new HibernateClassDAO();
    /**
     * A field providing access to the database table that holds the information for students
     */
    StudentDAO studentDAO = new HibernateStudentDAO();
    /**
     * A field providing access to the database table that holds the information for teachers
     */
    TeacherDAO teacherDAO = new HibernateTeacherDAO();

    /**
     * This method starts the job which updates all {@link Class}es, {@link Student}s and {@link Teacher}s in the database.
     
     * @param jec This parameter is not used
     * @throws JobExecutionException 
     */
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        IS_RUNNING = true;
        this.thread = Thread.currentThread();
        long starttime = Calendar.getInstance().getTimeInMillis();
        Logger.getLogger(QuartzUpdateDatabaseJob.class.getName()).log(Level.INFO,
                "Started at {0}", this.getCurrentTimeInfo());
        connector = new LdapDataProvider();
        ArrayList<String> classes = connector.getAllClasses();

        classDAO.updateAllClasses(classes);

        for (int i = 0; i < classes.size(); i++) {
            ArrayList<Student> students = connector.getAllStudents(classes.get(i));
            if (students.isEmpty()) {
                students = connector.getAllStudentsV2(classes.get(i));
            }
            studentDAO.updateStudents(students, classes.get(i));
        }
        ArrayList<String> teachers = connector.getAllTeachers();
        teacherDAO.updateTeachers(teachers);

        long endtime = Calendar.getInstance().getTimeInMillis();
        long worktime = (endtime-starttime)/1000;
        Logger.getLogger(QuartzUpdateDatabaseJob.class.getName()).log(Level.INFO,
                "Update completed without error on {0}.\nTime needed for completion was "+worktime+" seconds.", this.getCurrentTimeInfo());
        IS_RUNNING = false;
    }

    /**
     * This method interrupts the Job-Thread.
     * 
     * @throws UnableToInterruptJobException 
     */
    @Override
    public void interrupt() throws UnableToInterruptJobException {
        if(this.thread != null){
            thread.interrupt();
            IS_RUNNING = false;
            Logger.getLogger(QuartzUpdateDatabaseJob.class.getName()).log(Level.INFO, "Method interrupt() was called");
        }
    }
    
    /**
     * This method produces a String with a nice date and time format for
     * the usage in logging statements.
     * 
     * @return A String holding the time information
     */
    private String getCurrentTimeInfo() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss / dd.MM.yyyy");
        return sdf.format(Calendar.getInstance().getTime());
    }
}
