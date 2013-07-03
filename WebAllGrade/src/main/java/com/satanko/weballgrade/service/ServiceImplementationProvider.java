/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.satanko.weballgrade.controller.DatabaseAccessProvider;
import com.satanko.weballgrade.controller.impl.HibernateDatabaseAccessProvider;
import com.satanko.weballgrade.data.dao.impl.hibernate.AllGradeDBUtil;
import com.satanko.weballgrade.data.model.*;
import com.satanko.weballgrade.data.serializer.*;

/**
 * This class provides the implementations of the interfaces used by the service classes. (factory design pattern)
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class ServiceImplementationProvider {

    /**
     * This method provides a new GsonBuilder with all available TypeAdapter classes.
     * 
     * @return A {@link Gson} object
     * @see TypeAdapter
     */
    public static Gson provideGson() {
        return new GsonBuilder()
//                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .registerTypeAdapter(Student.class, new StudentAdapter())
                .registerTypeAdapter(GradeLog.class, new GradeLogAdapter())
                .registerTypeAdapter(Lesson.class, new LessonAdapter())
                .registerTypeAdapter(Teacher.class, new TeacherAdapter())
                .registerTypeAdapter(Subject.class, new SubjectAdapter())
                .registerTypeAdapter(Clazz.class, new ClazzAdapter())
                .create();
    }

    /**
     * This method provides an implementation of the {@link DatabaseAccessProvider} interface with Hibernate.
     * 
     * @return A {@link HibernateDatabaseAccessProvider} object.
     * @see AllGradeDBUtil
     */
    public static DatabaseAccessProvider provideHibernateDatabaseAccess() {
        return (DatabaseAccessProvider) new HibernateDatabaseAccessProvider();
    }
}
