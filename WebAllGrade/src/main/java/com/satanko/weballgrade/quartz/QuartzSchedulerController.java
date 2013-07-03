/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.weballgrade.quartz;

import com.satanko.weballgrade.service.PreparedServerResponses;
import com.satanko.weballgrade.service.ServiceConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This class controlls the scheduler for this application.
 * 
 * <p>Additional Information:<br>
 * <a href=http://quartz-scheduler.org>http://quartz-scheduler.org</a>
 * 
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * 
 * @version 1.0
 */
public class QuartzSchedulerController {
    
    /**
     * A {@link JobKey} field holding the identifier to a regular database update.
     */
    private final JobKey JOBKEY_DATABASE_UPDATE_REGULAR = new JobKey("RegularDatabaseUpdate");
    /**
     * A {@link JobKey} field holding the identifier to a instant database update.
     */
    private final JobKey JOBKEY_DATABASE_UPDATE_INSTANT = new JobKey("InstantDatabaseUpdate");
    
    private static int atHour = 12;
    private static int dayOfWeek = 1;
    private static int dayOfMonth = 1;
    /**
     * A field holding a String value that stands for the period in which the service should run.
     * 
     * <p>Expected values are {@value ServiceConstants#PARAM_VALUE_DAILY}, 
     * {@value ServiceConstants#PARAM_VALUE_WEEKLY} or {@value ServiceConstants#PARAM_VALUE_MONTHLY}.
     */
    private static String period = ServiceConstants.PARAM_VALUE_NOINPUT;
    
    private Scheduler sched;
    private JobDetail instantDatabaseUpdateJob;
    private JobDetail regularDatabaseUpdateJob;
    
    
    public QuartzSchedulerController() 
    {
        try {
            sched = new StdSchedulerFactory().getScheduler();
            sched.start();
            
            regularDatabaseUpdateJob = JobBuilder.newJob(QuartzUpdateDatabaseJob.class)
                    .withIdentity(JOBKEY_DATABASE_UPDATE_REGULAR)
                    .storeDurably(true)
                    .build();
            instantDatabaseUpdateJob = JobBuilder.newJob(QuartzUpdateDatabaseJob.class)
                    .withIdentity(JOBKEY_DATABASE_UPDATE_INSTANT)
                    .storeDurably(true)
                    .build();
            
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzSchedulerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(PreparedServerResponses.ERROR_SCHEDULER);
        }
    }
    
    /**
     * This method adds the {@link #JOBKEY_DATABASE_UPDATE_INSTANT} job to the scheduler.
     */
    public void startInstantDatabaseUpdate()
    {
        try {
            if(!sched.checkExists(JOBKEY_DATABASE_UPDATE_INSTANT))
            {
                sched.addJob(instantDatabaseUpdateJob, true);
                
            }
            if(!QuartzUpdateDatabaseJob.IS_RUNNING)
            {
                sched.triggerJob(JOBKEY_DATABASE_UPDATE_INSTANT);
            }
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzSchedulerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(PreparedServerResponses.ERROR_SCHEDULER);
        }
    }
    
    /**
     * This method interrupts the currently running {@link #JOBKEY_DATABASE_UPDATE_INSTANT} job.
     * @return <b>true</b> if the job was interrupted, otherwise <b>false</b>
     */
    public boolean stopInstantDatabaseUpdate()
    {
        try {
            return sched.interrupt(JOBKEY_DATABASE_UPDATE_INSTANT);
        } catch (UnableToInterruptJobException ex) {
            Logger.getLogger(QuartzSchedulerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(PreparedServerResponses.ERROR_SCHEDULER);
        }
    }
    
    /**
     * This method checks if there is a {@link #JOBKEY_DATABASE_UPDATE_REGULAR} job existing in the scheduler.
     * @return <b>true</b> if a regular job exists in the scheduler, otherwise <b>false</b>
     */
    public boolean isOnRegularSchedule()
    {
        try {
            return sched.checkExists(JOBKEY_DATABASE_UPDATE_REGULAR);
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzSchedulerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(PreparedServerResponses.ERROR_SCHEDULER);
        }
    }
    
    /**
     * This method adds the {@link #JOBKEY_DATABASE_UPDATE_REGULAR} job to the scheduler.
     */
    public void startRegularDatabaseUpdates()
    {
        try {
            Trigger trigger = null;
            switch(period)
            {
                case ServiceConstants.PARAM_VALUE_DAILY:
                    trigger = TriggerBuilder.newTrigger()
                            .forJob(regularDatabaseUpdateJob)
                            .startNow()
                            .withSchedule(
                                CronScheduleBuilder.dailyAtHourAndMinute(atHour, 0)
                                .withMisfireHandlingInstructionFireAndProceed())
                            .build();
                    break;
                case ServiceConstants.PARAM_VALUE_WEEKLY:
                    trigger = TriggerBuilder.newTrigger()
                            .forJob(regularDatabaseUpdateJob)
                            .startNow()
                            .withSchedule(
                                CronScheduleBuilder.weeklyOnDayAndHourAndMinute(dayOfWeek,atHour, 0)
                                .withMisfireHandlingInstructionFireAndProceed())
                            .build();
                    break;
                case ServiceConstants.PARAM_VALUE_MONTHLY:
                    trigger = TriggerBuilder.newTrigger()
                            .forJob(regularDatabaseUpdateJob)
                            .startNow()
                            .withSchedule(
                                CronScheduleBuilder.monthlyOnDayAndHourAndMinute(dayOfMonth,atHour, 0)
                                .withMisfireHandlingInstructionFireAndProceed())
                            .build();
                    break;
                    default: throw new WebApplicationException(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Could not initialize trigger for regular database update. Period was not set")
                            .build());
            }
            sched.scheduleJob(trigger);
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzSchedulerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(PreparedServerResponses.ERROR_SCHEDULER);
        }
    }
    
    /**
     * This method removes the {@link #JOBKEY_DATABASE_UPDATE_REGULAR} job from the scheduler.
     */
    public boolean stopRegularDatabaseUpdates()
    {
        try {
            return sched.deleteJob(JOBKEY_DATABASE_UPDATE_REGULAR);
        } catch (SchedulerException ex) {
            Logger.getLogger(QuartzSchedulerController.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(PreparedServerResponses.ERROR_SCHEDULER);
        }
    }

    public static void setAtHour(int atHour) {
        QuartzSchedulerController.atHour = atHour;
    }

    public static void setDayOfMonth(int dayOfMonth) {
        QuartzSchedulerController.dayOfMonth = dayOfMonth;
    }

    public static void setDayOfWeek(int dayOfWeek) {
        QuartzSchedulerController.dayOfWeek = dayOfWeek;
    }

    public static void setPeriod(String period) {
        QuartzSchedulerController.period = period;
    }
    
}