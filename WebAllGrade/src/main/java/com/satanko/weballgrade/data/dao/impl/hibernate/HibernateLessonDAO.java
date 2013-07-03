/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.data.dao.impl.hibernate;

import com.satanko.weballgrade.data.dao.LessonDAO;
import com.satanko.weballgrade.data.model.*;
import com.satanko.weballgrade.service.PreparedServerResponses;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import org.hibernate.SessionFactory;

/**
 * This class provides an implementation of the {@link LessonDAO} 
 * interface using the Hibernate Framework to perform interactions with a single database table. 
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class HibernateLessonDAO implements LessonDAO {

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
    public List<Lesson> selectLessonList(String paramTeacher, String paramClass) {
        try {
            sf.getCurrentSession().beginTransaction();

            List<Lesson> lessonList;
            if (paramClass.isEmpty()) {
                //PART JULI!!!!!!
                lessonList = sf.getCurrentSession()
                        .createSQLQuery("SELECT * FROM AG_Lesson l INNER JOIN JOIN_Teacher_Lesson tl "
                        + "ON(l.lessonId = tl.lessonId_fk) WHERE tl.teacherToken_fk = :teacherToken")
                        .addEntity(Lesson.class)
                        .setParameter("teacherToken", paramTeacher)
                        .list();
            } else {
                lessonList = sf.getCurrentSession()
                        .createSQLQuery("SELECT * FROM AG_Lesson l INNER JOIN JOIN_Teacher_Lesson tl "
                        + "ON(l.lessonId = tl.lessonId_fk) WHERE tl.teacherToken_fk = :teacherToken AND l.lessonClassToken_fk = :classToken")
                        .addEntity(Lesson.class)
                        .setParameter("teacherToken", paramTeacher)
                        .setParameter("classToken", paramClass)
                        .list();
            }

            sf.getCurrentSession().getTransaction().commit();
            return lessonList;
        } catch (RuntimeException re) {
            sf.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(HibernateStudentDAO.class.getName()).log(Level.SEVERE, re.getMessage());
            throw new WebApplicationException(PreparedServerResponses.ERROR_DATABASE);
        }
    }

    /**
     * <b>WRITTEN BY: <br>Julian Tropper (julian.tropper@gmail.com) <br>Philipp Sommersguter (philipp.sommersguter@satanko.at)</b><p>
     * <i>Inserts a new {@link Lesson} into the database.
     * <br> If the {@link Lesson} does not exist in the database, a new entry in the {@link Lesson}-table and the 
     * joined table of {@link Lesson} and {@link Teacher} is made. If it does exist, it only 
     * creates a new entry in the joined table for each missing {@link Teacher}.
     * 
     * @param paramClass A String representing a class
     * @param paramSubject A String representing a subject
     * @param teacherTokens A String representing a teacher
     * @see LessonDAO
     */
    private void insertNewLesson(String paramClass, String paramSubject, ArrayList<String> teacherTokens) {
        try {
            sf.getCurrentSession().beginTransaction();

            Clazz clazz = (Clazz) sf.getCurrentSession().load(Clazz.class, paramClass);
            Subject subject = (Subject) sf.getCurrentSession().load(Subject.class, paramSubject);
            Lesson lesson = new Lesson(clazz, subject);
            for (int i = 0; i < teacherTokens.size(); i++) {
                Teacher teacher = (Teacher) sf.getCurrentSession().load(Teacher.class, teacherTokens.get(i));
                lesson.getTeachers().add(teacher);
            }

            Lesson checkedLesson = (Lesson) sf.getCurrentSession()
                    .createSQLQuery("SELECT * FROM AG_Lesson WHERE lessonClassToken_fk = :classToken AND lessonSubjectToken_fk = :subjectToken")
                    .addEntity(Lesson.class)
                    .setParameter("classToken", paramClass)
                    .setParameter("subjectToken", paramSubject)
                    .uniqueResult();

            ArrayList<String> teachersNotInTable = new ArrayList<>();

            if (checkedLesson != null) {
                for (int i = 0; i < teacherTokens.size(); i++) {
                    Object joinedLesson = sf.getCurrentSession()
                            .createSQLQuery("SELECT * FROM JOIN_Teacher_Lesson WHERE lessonId_fk = :lessonId_fk AND teacherToken_fk = :teacherToken_fk")
                            .setParameter("lessonId_fk", checkedLesson.getId())
                            .setParameter("teacherToken_fk", teacherTokens.get(i))
                            .uniqueResult();

                    if (joinedLesson == null) {
                        teachersNotInTable.add(teacherTokens.get(i));
                    }
                }
            }

            if (checkedLesson == null && teachersNotInTable.isEmpty()) {
                sf.getCurrentSession().save(lesson);
            } else if (checkedLesson != null && !teachersNotInTable.isEmpty()) {
                for (int i = 0; i < teachersNotInTable.size(); i++) {
                    sf.getCurrentSession().
                            createSQLQuery("INSERT INTO JOIN_Teacher_Lesson (teacherToken_fk, lessonId_fk) VALUES (:teacherToken_fk, :lessonId_fk)")
                            .setParameter("teacherToken_fk", teachersNotInTable.get(i))
                            .setParameter("lessonId_fk", checkedLesson.getId())
                            .executeUpdate();
                }
            }
            sf.getCurrentSession().getTransaction().commit();
        } catch (RuntimeException re) {
            sf.getCurrentSession().getTransaction().rollback();
            Logger.getLogger(HibernateStudentDAO.class.getName()).log(Level.SEVERE, re.getMessage());
            throw new WebApplicationException(PreparedServerResponses.ERROR_DATABASE);
        }
    }
    /**
     * <b>WRITTEN BY: Julian Tropper (julian.tropper@gmail.com)</b><p>
     * <i> Deletes a {@link Lesson} from the database.
     * <br>Note that only the entry in the joined table of {@link Lesson} and {@link Teacher} is deleted, not the actual
     * {@link Lesson} entry.
     * 
     * @param paramClass A string representing a class
     * @param paramSubject A string representing a subject
     * @param teacherToken A string representing a teacher
     */
    private void deleteLesson(String paramClass, String paramSubject, String teacherToken) {
        try {
            sf.getCurrentSession().beginTransaction();

            Clazz clazz = (Clazz) sf.getCurrentSession().load(Clazz.class, paramClass);
            Subject subject = (Subject) sf.getCurrentSession().load(Subject.class, paramSubject);
            Lesson lesson = new Lesson(clazz, subject);
            Teacher teacher = (Teacher) sf.getCurrentSession().load(Teacher.class, teacherToken);
            lesson.getTeachers().add(teacher);

            Lesson checkedLesson = (Lesson) sf.getCurrentSession()
                    .createSQLQuery("SELECT * FROM AG_Lesson WHERE lessonClassToken_fk = :classToken AND lessonSubjectToken_fk = :subjectToken")
                    .addEntity(Lesson.class)
                    .setParameter("classToken", paramClass)
                    .setParameter("subjectToken", paramSubject)
                    .uniqueResult();
            /*List<Object> joinedLessons = sf.getCurrentSession()
                    .createSQLQuery("SELECT * FROM JOIN_Teacher_Lesson WHERE lessonId_fk = :lessonId_fk")
                    .setParameter("lessonId_fk", checkedLesson.getId())
                    .list();*/

            sf.getCurrentSession()
                    .createSQLQuery("DELETE FROM JOIN_Teacher_Lesson WHERE lessonId_fk = :lessonId_fk AND teacherToken_fk = :teacherToken_fk")
                    .setParameter("lessonId_fk", checkedLesson.getId())
                    .setParameter("teacherToken_fk", teacherToken)
                    .executeUpdate();
            /*} else {
                sf.getCurrentSession()
                        .createSQLQuery("DELETE FROM JOIN_Teacher_Lesson WHERE lessonId_fk = :lessonId_fk AND teacherToken_fk = :teacherToken_fk")
                        .setParameter("lessonId_fk", checkedLesson.getId())
                        .setParameter("teacherToken_fk", teacherToken)
                        .executeUpdate();
                sf.getCurrentSession()
                        .createSQLQuery("DELETE FROM AG_Lesson WHERE lessonClassToken_fk = :classToken AND lessonSubjectToken_fk = :subjectToken")
                        .setParameter("classToken", paramClass)
                        .setParameter("subjectToken", paramSubject)
                        .executeUpdate();
            }*/

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
    public void createOrUpdateLessons(ArrayList<JsonLesson> oldLessons, ArrayList<JsonLesson> newLessons) throws WebApplicationException {
        for (JsonLesson lesson : newLessons) {
            if (!oldLessons.contains(lesson)) {
                this.insertNewLesson(lesson.getClassToken(), lesson.getSubjectToken(), lesson.getTeacherList());
            }
        }
        for (JsonLesson lesson : oldLessons) {
            if (!newLessons.contains(lesson)) {
                this.deleteLesson(lesson.getClassToken(), lesson.getSubjectToken(), lesson.getTeacherList().get(0));
            }
        }
    }
}
