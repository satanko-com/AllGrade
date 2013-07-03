/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao.impl.hibernate;

import com.satanko.weballgrade.data.dao.GradeLogDAO;
import com.satanko.weballgrade.data.model.GradeLog;
import com.satanko.weballgrade.data.model.Lesson;
import com.satanko.weballgrade.data.model.Student;
import com.satanko.weballgrade.service.PreparedServerResponses;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import org.hibernate.SessionFactory;

/**
 * This class provides an implementation of the {@link GradeLogDAO} 
 * interface using the Hibernate Framework to perform interactions with a single database table. 
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class HibernateGradeLogDAO implements GradeLogDAO{

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
    public void insertNewGradeLog(long paramTimestamp, String paramClass, String paramTeacher, String paramStudent, 
    String paramSubject, String paramType, int paramGrading, String paramComment) {
        try {
            sf.getCurrentSession().beginTransaction();
            Lesson lesson = (Lesson) sf.getCurrentSession()
                    .createSQLQuery("SELECT * FROM AG_Lesson l INNER JOIN JOIN_Teacher_Lesson tl "
                    + "ON(l.lessonId = tl.lessonId_fk) WHERE tl.teacherToken_fk = :teacherToken AND "
                    + "l.lessonClassToken_fk = :classToken AND l.lessonSubjectToken_fk = :subjectToken")
                    .addEntity(Lesson.class)
                    .setParameter("teacherToken", paramTeacher)
                    .setParameter("classToken", paramClass)
                    .setParameter("subjectToken", paramSubject)
                    .uniqueResult();

            Student student = (Student) sf.getCurrentSession().get(Student.class, paramStudent);
            
            GradeLog gradelog = new GradeLog(new Timestamp(paramTimestamp), lesson, student, paramComment, paramType, paramGrading);
            sf.getCurrentSession().save(gradelog);
            
            sf.getCurrentSession().getTransaction().commit();
        } catch (RuntimeException re) {
            sf.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(HibernateStudentDAO.class.getName()).log(Level.SEVERE, re.getMessage());
            throw new WebApplicationException(PreparedServerResponses.ERROR_DATABASE);
        }
    }
    
    /**
     * <b>WRITTEN BY: Julian Tropper (julian.tropper@gmail.com)</b><p>
     * {@inheritDoc }
     */
    @Override
    public List<GradeLog> selectGradeLogs(String paramTeacher, String paramClass, String paramSubject) {
        try {
            sf.getCurrentSession().beginTransaction();

            List<GradeLog> gradeLogList;

            gradeLogList = sf.getCurrentSession()
                    .createSQLQuery(
                    "SELECT  * " +
                    "FROM AG_Gradelog " +
                    "INNER JOIN JOIN_Teacher_Lesson ON ( AG_Gradelog.gradelogLessonFk = JOIN_Teacher_Lesson.lessonId_fk ) " +
                    "INNER JOIN AG_Lesson ON ( AG_Gradelog.gradelogLessonFk = AG_Lesson.lessonId ) " +
                    "WHERE JOIN_Teacher_Lesson.teacherToken_fk =  :teacherToken " +
                    "AND AG_Lesson.lessonClassToken_fk =  :classToken " +
                    "AND AG_Lesson.lessonSubjectToken_fk =  :subjectToken " +
                    "ORDER BY DATE( AG_Gradelog.gradelogTimestamp ) , gradelogStudentFK, TIME( AG_Gradelog.gradelogTimestamp ) ASC")
                    .addEntity(GradeLog.class)
                    .setParameter("teacherToken", paramTeacher)
                    .setParameter("classToken", paramClass)
                    .setParameter("subjectToken", paramSubject)
                    .list();

            sf.getCurrentSession().getTransaction().commit();
            return gradeLogList;
        } catch (RuntimeException re) {
            sf.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(HibernateStudentDAO.class.getName()).log(Level.SEVERE, re.getMessage());
            throw new WebApplicationException(PreparedServerResponses.ERROR_DATABASE);
        }
    }   
    
}