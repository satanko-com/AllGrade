/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.satanko.weballgrade.controller.DatabaseAccessProvider;
import com.satanko.weballgrade.data.model.JsonLesson;
import com.satanko.weballgrade.data.model.Lesson;
import com.satanko.weballgrade.service.PreparedServerResponses;
import com.satanko.weballgrade.service.ServiceConstants;
import com.satanko.weballgrade.service.ServiceImplementationProvider;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class handles all incoming HTTP request for the <b>/database/lessons</b> resource.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
@Path("/lessons")
public class LessonsService {

    //Those are the same as in the ExcelAllGrade Application
    public static final String OLD_LESSONS = "OldLessons";
    public static final String NEW_LESSONS = "NewLessons";
    public static final String SUBJECT_TOKEN = "SubjectToken";
    public static final String CLASS_TOKEN = "ClassToken";
    public static final String TEACHERS = "Teachers";
    public static final String TEACHER_TOKEN = "TeacherToken";
    
    /**
     * A field providing access to Google's Gson library.
     */
    private Gson gson = ServiceImplementationProvider.provideGson();
    /**
     * A field providing access to the database.
     */
    private DatabaseAccessProvider databaseAccessProvider = ServiceImplementationProvider.provideHibernateDatabaseAccess();

    /**
     * This method is invoked when a HTTP <b>GET</b> request to <i>/database/gradelogs</i> occurs.
     * 
     * The method validates all incomming query parameters and then selects a list of {@link Lesson}s from the database.
     * This list gets serialized by Gson and is sent back via a HTTP response.
     * 
     * @param paramClass A URL parameter representing a class
     * @param paramTeacher A URL parameter representing a teacher
     * @return A JSON String 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getLessonListJSON(
            @QueryParam(ServiceConstants.PARAM_CLASS) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramClass,
            @QueryParam(ServiceConstants.PARAM_TEACHER) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramTeacher) {
        if (paramClass.equals(ServiceConstants.PARAM_VALUE_NOINPUT) || paramTeacher.equals(ServiceConstants.PARAM_VALUE_NOINPUT)) {
            throw new WebApplicationException(PreparedServerResponses.BADREQUEST_QUERY_PARAMETER);
        } else if (!databaseAccessProvider.checkTeacherInClass(paramTeacher, paramClass)) {
            throw new WebApplicationException(PreparedServerResponses.CONFLICT_NOT_TEACHING);
        } else {
            List<Lesson> lessonList = databaseAccessProvider.getLessonList(paramTeacher, paramClass);
            return gson.toJson(lessonList);
        }
    }

    /**
     * <b>WRITTEN BY: Julian Tropper (julian.tropper@gmail.com)</b><p>
     * 
     * This method is invoked when a HTTP <b>POST</b> request to <i>/database/lessons</i> occurs.
     * <p>
     * The method expects a JSON String in the HTTP request body. 
     * This string will then be validated, processed and inserted into the the database.
     * 
     * @param body A JSON String holding the information for two lists of the type {@link Lesson}
     * @return A HTTP {@link Response} object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postLessons(String body) {        
        try {
            JsonObject jsonLesson = new JsonParser().parse(body).getAsJsonObject();
            System.out.println(jsonLesson.toString());

            if (!(jsonLesson.has(OLD_LESSONS)
                    && jsonLesson.has(NEW_LESSONS))) {
                throw new WebApplicationException(PreparedServerResponses.INVALID_JSON);
            }

            ArrayList<JsonLesson> oldLessons = new ArrayList<>();
            ArrayList<JsonLesson> newLessons = new ArrayList<>();

            JsonArray oldClasses = jsonLesson.get(OLD_LESSONS).getAsJsonArray();
            JsonArray newClasses = jsonLesson.get(NEW_LESSONS).getAsJsonArray();

            for (int i = 0; i < oldClasses.size(); i++) {
                JsonObject lessonObject = oldClasses.get(i).getAsJsonObject();

                String subjectToken = lessonObject.get(SUBJECT_TOKEN).getAsString();
                String classToken = lessonObject.get(CLASS_TOKEN).getAsString();

                JsonLesson lesson = new JsonLesson(classToken, subjectToken);

                JsonArray teacherList = lessonObject.get(TEACHERS).getAsJsonArray();

                for (int j = 0; j < teacherList.size(); j++) {
                    String teacherToken = teacherList.get(j).getAsJsonObject().get(TEACHER_TOKEN).getAsString();

                    lesson.addTeacher(teacherToken);
                }

                oldLessons.add(lesson);
            }

            for (int i = 0; i < newClasses.size(); i++) {
                JsonObject lessonObject = newClasses.get(i).getAsJsonObject();

                String subjectToken = lessonObject.get(SUBJECT_TOKEN).getAsString();
                String classToken = lessonObject.get(CLASS_TOKEN).getAsString();

                JsonLesson lesson = new JsonLesson(classToken, subjectToken);

                JsonArray teacherList = lessonObject.get(TEACHERS).getAsJsonArray();

                for (int j = 0; j < teacherList.size(); j++) {
                    String teacherToken = teacherList.get(j).getAsJsonObject().get(TEACHER_TOKEN).getAsString();

                    lesson.addTeacher(teacherToken);
                }

                newLessons.add(lesson);
            }

            databaseAccessProvider.createOrUpdateLessons(oldLessons, newLessons);

            return Response.ok().build();
        } catch (WebApplicationException ex) {
            return ex.getResponse();
        }
        
    }
}
