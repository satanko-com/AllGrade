/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.satanko.weballgrade.controller.DatabaseAccessProvider;
import com.satanko.weballgrade.data.model.GradeLog;
import com.satanko.weballgrade.service.PreparedServerResponses;
import com.satanko.weballgrade.service.ServiceConstants;
import com.satanko.weballgrade.service.ServiceImplementationProvider;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This class handles all incomming HTTP request for the <b>/database/gradelogs</b> resource.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
@Path("/gradelogs")
public class GradeLogsService {

    //The values of the following fields must be the same as in Mobile AllGrade
    public static final String TIMESTAMP = "timestamp";
    public static final String CLASS   = "class";
    public static final String TEACHER = "teacher";
    public static final String STUDENT = "student";
    public static final String SUBJECT = "subject";
    public static final String TYPE    = "type";
    public static final String GRADING = "grading";
    public static final String COMMENT = "comment";
    
    /**
     * A field providing access to Google's Gson library.
     */
    private Gson gson = ServiceImplementationProvider.provideGson();
    /**
     * A field providing access to the database.
     */
    private DatabaseAccessProvider databaseAccessProvider = ServiceImplementationProvider.provideHibernateDatabaseAccess();

    /**
     * This method is invoked when a HTTP <b>POST</b> request to <i>/database/gradelogs</i> occurs.
     * <p>
     * The method expects a JSON String in the HTTP request body. 
     * This string will then be validated, processed and inserted into the the database.
     * 
     * @param body A JSON String holding the information for a single {@link GradeLog}
     * @return A HTTP {@link Response} object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postGradeLogs(String body) {
            JsonObject jsonGradeLog = new JsonParser().parse(body).getAsJsonObject();
            if( !(jsonGradeLog.has(TIMESTAMP)&&jsonGradeLog.has(CLASS)&&jsonGradeLog.has(TEACHER)&&jsonGradeLog.has(STUDENT)&&
                jsonGradeLog.has(SUBJECT)&&jsonGradeLog.has(TYPE)&&jsonGradeLog.has(GRADING)&&jsonGradeLog.has(COMMENT)) ){
                throw new WebApplicationException(PreparedServerResponses.INVALID_JSON);
            }
            databaseAccessProvider.createGradeLog(
                    jsonGradeLog.get(TIMESTAMP).getAsLong(),
                    jsonGradeLog.get(CLASS).getAsString(),
                    jsonGradeLog.get(TEACHER).getAsString(),
                    jsonGradeLog.get(STUDENT).getAsString(), 
                    jsonGradeLog.get(SUBJECT).getAsString(),
                    jsonGradeLog.get(TYPE).getAsString(),
                    jsonGradeLog.get(GRADING).getAsInt(), 
                    jsonGradeLog.get(COMMENT).getAsString() );
            return Response.ok().build();
    }
    
    /**
     * This method is invoked when a HTTP <b>GET</b> request to <i>/database/gradelogs</i> occurs.
     * 
     * The method validates all incoming query parameters and then selects a list of {@link GradeLog}s from the database.
     * This list gets serialized by Gson and is sent back via a HTTP response.
     * 
     * @param paramClass A URL parameter representing a class
     * @param paramTeacher A URL parameter representing a teacher
     * @param paramSubject A URL parameter representing a subject
     * @return A JSON String 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getGradelogListJSON(
            @QueryParam(ServiceConstants.PARAM_CLASS) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramClass,
            @QueryParam(ServiceConstants.PARAM_TEACHER) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramTeacher,
            @QueryParam(ServiceConstants.PARAM_SUBJECT) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramSubject) {
        if (paramClass.equals(ServiceConstants.PARAM_VALUE_NOINPUT) || 
            paramTeacher.equals(ServiceConstants.PARAM_VALUE_NOINPUT) || 
            paramSubject.equals(ServiceConstants.PARAM_VALUE_NOINPUT)) {
            throw new WebApplicationException(PreparedServerResponses.BADREQUEST_QUERY_PARAMETER);
        } else if (!databaseAccessProvider.checkTeacherInClass(paramTeacher, paramClass)) {
            throw new WebApplicationException(PreparedServerResponses.CONFLICT_NOT_TEACHING);
        } else {
            List<GradeLog> gradelogList = databaseAccessProvider.getGradeLogs(paramTeacher, paramClass, paramSubject);
            return gson.toJson(gradelogList);
        }
    }
}
