/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service.database;

import com.google.gson.Gson;
import com.satanko.weballgrade.controller.DatabaseAccessProvider;
import com.satanko.weballgrade.data.model.Clazz;
import com.satanko.weballgrade.service.ServiceImplementationProvider;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This class handles all incomming HTTP request for the <b>/database/classes</b> resource.
 * 
 * @author Julian Tropper (julian.tropper@gmail.com)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
@Path("/classes")
public class ClassesService {
    
    /**
     * A field providing access to Google's Gson library.
     */
    private Gson gson = ServiceImplementationProvider.provideGson();
    
    /**
     * A field providing access to the database.
     */
    private DatabaseAccessProvider databaseAccessProvider = ServiceImplementationProvider.provideHibernateDatabaseAccess();

    /**
     * This method is invoked when a HTTP <b>GET</b> request to <i>/database/classes</i> occurs.
     * 
     * The method selects a list of {@link Class}es from the database.
     * This list gets serialized by Gson and is sent back via a HTTP response.
     * 
     * @return A JSON String 
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getClassListJSON() {
        List<Clazz> classList = databaseAccessProvider.getClassList();
        return gson.toJson(classList);
    }
}
