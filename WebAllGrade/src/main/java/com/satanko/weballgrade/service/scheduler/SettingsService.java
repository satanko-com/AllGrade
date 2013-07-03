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
 * This class handles all incoming HTTP request for the <b>/scheduler/settings</b> resource.
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
@Path("/settings")
public class SettingsService {
    
    @QueryParam(ServiceConstants.PARAM_PERIOD)
    @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT)
    private String paramPeriod;
    
    @QueryParam(ServiceConstants.PARAM_ATHOUR)
    @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT)
    private String paramAtHour;
    
    @QueryParam(ServiceConstants.PARAM_DAYWEEK)
    @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT)
    private String paramDayOfWeek;
    
    @QueryParam(ServiceConstants.PARAM_DAYMONTH)
    @DefaultValue(ServiceConstants.PARAM_VALUE_NOINPUT)
    private String paramDayOfMonth;
    
    /**
     * This method is invoked when a HTTP <b>HEAD</b> request to <i>/scheduler/settings</i> occurs.
     * 
     * The method is responsible for assigning the parameter's values to setting variables in the
     * {@link QuartzSchedulerController}.
     * 
     * @return A HTTP {@link Response} object
     */
    @HEAD
    public Response initGivenSettings() {
        if(!paramPeriod.equals(ServiceConstants.PARAM_VALUE_NOINPUT))
        {
            QuartzSchedulerController.setPeriod(paramPeriod);
        }
        if(!paramAtHour.equals(ServiceConstants.PARAM_VALUE_NOINPUT))
        {
            try{
                int atHour = Integer.parseInt(paramAtHour);
                QuartzSchedulerController.setAtHour(atHour);
            }catch(NumberFormatException nfe)
            {
                throw new WebApplicationException(
                        PreparedServerResponses.buildSettingsParseErrorResponse(ServiceConstants.PARAM_ATHOUR));
            }
        }
        if(!paramDayOfWeek.equals(ServiceConstants.PARAM_VALUE_NOINPUT))
        {
            try{
                int dayOfWeek = Integer.parseInt(paramDayOfWeek);
                QuartzSchedulerController.setAtHour(dayOfWeek);
            }catch(NumberFormatException nfe)
            {
                throw new WebApplicationException(
                        PreparedServerResponses.buildSettingsParseErrorResponse(ServiceConstants.PARAM_DAYWEEK));
            }
        }
        if(!paramDayOfMonth.equals(ServiceConstants.PARAM_VALUE_NOINPUT))
        {
            try{
                int dayOfMonth = Integer.parseInt(paramDayOfMonth);
                QuartzSchedulerController.setAtHour(dayOfMonth);
            }catch(NumberFormatException nfe)
            {
                throw new WebApplicationException(
                        PreparedServerResponses.buildSettingsParseErrorResponse(ServiceConstants.PARAM_DAYMONTH));
            }
        }
        return Response.ok().build();
    }
}