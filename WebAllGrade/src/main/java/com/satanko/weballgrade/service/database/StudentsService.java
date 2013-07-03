/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service.database;

import com.google.gson.Gson;
import com.satanko.weballgrade.controller.DatabaseAccessProvider;
import com.satanko.weballgrade.data.model.Student;
import com.satanko.weballgrade.service.PreparedServerResponses;
import com.satanko.weballgrade.service.ServiceConstants;
import com.satanko.weballgrade.service.ServiceImplementationProvider;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * This class handles all incoming HTTP request for the <b>/database/students</b> resource.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
@Path("/students")
public class StudentsService {
    
    /**
     * A field providing access to Google's Gson library.
     */
    private Gson gson = ServiceImplementationProvider.provideGson();
    /**
     * A field providing access to the database.
     */
    private DatabaseAccessProvider databaseAccessProvider = ServiceImplementationProvider.provideHibernateDatabaseAccess();
    
    /**
     * This method is invoked when a HTTP <b>GET</b> request to <i>/database/students</i> occurs.
     * 
     * The method validates all incomming query parameters and then selects a list of {@link Student}s from the database.
     * This list gets serialized by Gson and is sent back via a HTTP response.
     * 
     * @param paramClass A URL parameter representing a class
     * @param paramTeacher A URL parameter representing a teacher
     * @return A JSON String 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStudentListJSON(
            @QueryParam(ServiceConstants.PARAM_CLASS) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramClass,
            @QueryParam(ServiceConstants.PARAM_TEACHER) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramTeacher) {
        if (paramClass.equals(ServiceConstants.PARAM_VALUE_NOINPUT) || paramTeacher.equals(ServiceConstants.PARAM_VALUE_NOINPUT)) {
            throw new WebApplicationException(PreparedServerResponses.BADREQUEST_QUERY_PARAMETER);
        } else if (!databaseAccessProvider.checkTeacherInClass(paramTeacher, paramClass)) {
            throw new WebApplicationException(PreparedServerResponses.CONFLICT_NOT_TEACHING);
        } else {
            List<Student> studentList = databaseAccessProvider.getStudentList(paramClass);
            return gson.toJson(studentList);
        }
    }
}
