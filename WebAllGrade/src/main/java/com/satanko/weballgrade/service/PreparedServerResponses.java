/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service;

import com.satanko.weballgrade.quartz.QuartzSchedulerController;
import com.satanko.weballgrade.quartz.QuartzUpdateDatabaseJob;
import java.sql.SQLException;
import javax.ws.rs.core.Response;

/**
 * A class holding already prepared server responses in form of static fields for easy usage.
 * <p>
 * For more information about usable HTTP codes and when they should be applied, please visit the following link:<br> 
 * <a href=http://en.wikipedia.org/wiki/List_of_HTTP_status_codes>http://en.wikipedia.org/wiki/List_of_HTTP_status_codes</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class PreparedServerResponses {
    
    /**
     * <b>The field represents the HTTP error 400 (BAD_REQUEST).</b><p>
     * This response should be used if the server could not process the query parameters.
     */
    public static final Response BADREQUEST_QUERY_PARAMETER = 
            Response.status(Response.Status.BAD_REQUEST)
                    .entity("The required query parameters were wrong")
            .build();
    
    /**
     * <b>The field represents the HTTP error 500 (INTERNAL_SERVER_ERROR).</b><p>
     * This response should be used if the server encounteres an error with the database.<br>
     * Example: A {@link SQLException} occurs.
     */
    public static final Response ERROR_DATABASE =
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Database error")
            .build();
    
    /**
     * <b>The field represents the HTTP error 400 (BAD_REQUEST).</b><p>
     * This response should be used if the server could not process the provided JSON String.
     * @see PreparedServerResponses#BADREQUEST_QUERY_PARAMETER
     */
    public static final Response INVALID_JSON =
            Response.status(Response.Status.BAD_REQUEST)
                    .entity("The provided JSON String was invalid")
            .build();
    
    /**
     * <b>The field represents the HTTP error 409 (CONFLICT).</b><p>
     * This response should be used if the database does not contain lessons for a teacher in a specific class.
     */
    public static final Response CONFLICT_NOT_TEACHING = 
            Response.status(Response.Status.CONFLICT)
                .entity("Teacher is not teaching given class")
            .build();
    /**
     * <b>The field represents the HTTP error 500 (INTERNAL_SERVER_ERROR).</b><p>
     * This response should be used if the server encounters an error with the Quartz Scheduler.
     * @see QuartzSchedulerController
     * @see QuartzUpdateDatabaseJob
     */
    public static final Response ERROR_SCHEDULER =
            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Quartz scheduler error")
            .build();
    
    /**
     * A method that builds a HTTP 400 (BAD_REQUEST) response with a custom parameter name
     * when the value of a scheduler setting is wrong.
     * 
     * @param nameOfParameter A String holding the name of a url parameter
     * @return A HTTP-{@link Response} built by this method
     */
    public static Response buildSettingsParseErrorResponse(String nameOfParameter)
    {
        return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Wrong value for parameter \""+nameOfParameter+"\"")
               .build();
    }
}
