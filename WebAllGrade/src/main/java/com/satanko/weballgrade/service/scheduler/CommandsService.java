/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.service.scheduler;

import com.satanko.weballgrade.quartz.QuartzSchedulerController;
import com.satanko.weballgrade.service.PreparedServerResponses;
import com.satanko.weballgrade.service.ServiceConstants;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * This class handles all incomming HTTP request for the <b>/scheduler/commands</b> resource.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
@Path("/commands")
public class CommandsService {
    
    private final String ACTION_START = "start";
    private final String ACTION_STOP = "stop";
    private final String ACTION_INFO = "info";
    private final String ACTION_REGULAR_START = "startregular";
    private final String ACTION_REGULAR_STOP = "stopregular";
    
    /**
     * A field providing access to the scheduler controller of the application.
     */
    private QuartzSchedulerController schedulerController = new QuartzSchedulerController();
    
    /**
     * This method is invoked when a HTTP <b>HEAD</b> request to <i>/scheduler/commands/update/</i> occurs.
     * <p>
     * The method validates all incoming query parameters and then calls methods of the {@link QuartzSchedulerController}
     * according to the requested action.
     * 
     * @param paramAction A path parameter containing the action String
     * @param paramPeriod A query parameter holding the wanted period of the action
     * @return A HTTP {@link Response} object
     */
    @HEAD
    @Path("/update/{action}")
    public Response executeAction(
            @PathParam(ServiceConstants.PARAM_ACTION) @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramAction,
            @QueryParam(ServiceConstants.PARAM_PERIOD)@DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT) String paramPeriod
            ) {
        switch(paramAction)
        {
            case ACTION_START:
                schedulerController.startInstantDatabaseUpdate();
                return Response.ok().build();
            case ACTION_STOP:
                schedulerController.stopInstantDatabaseUpdate();
                return Response.ok().build();
            case ACTION_REGULAR_START:
                if(schedulerController.isOnRegularSchedule())
                {
                    schedulerController.stopRegularDatabaseUpdates();
                }
                if(!paramPeriod.equals(ServiceConstants.PARAM_VALUE_NOINPUT)&&
                   (paramPeriod.equals(ServiceConstants.PARAM_VALUE_DAILY)||
                    paramPeriod.equals(ServiceConstants.PARAM_VALUE_WEEKLY)||
                    paramPeriod.equals(ServiceConstants.PARAM_VALUE_MONTHLY)) ){
                    QuartzSchedulerController.setPeriod(paramPeriod);
                }
                schedulerController.startRegularDatabaseUpdates();
                return Response.ok().build();
            case ACTION_REGULAR_STOP:
                schedulerController.stopRegularDatabaseUpdates();
                return Response.ok().build();
            case ACTION_INFO:
                if(schedulerController.isOnRegularSchedule()){
                    return Response.ok().entity("Regular schedule is set").build();
                }else
                {
                    return Response.ok().entity("Regular schedule is not set").build();
                }
                
            default: return PreparedServerResponses.BADREQUEST_QUERY_PARAMETER;
        }
    }
    
}
