package com.oskarmc.execute.common.util;

import com.oskarmc.execute.common.configuration.ExecuteSettings;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;

public class ExecuteTemplate {
    public Scheduler scheduler;

    public ExecuteTemplate(ExecuteSettings executeSettings, Class<? extends Job> cronJobClass) {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        try {
            this.scheduler = schedulerFactory.getScheduler();

            for (String s : executeSettings.getCommands().keySet()) {

                try {
                    JobDetail job = JobBuilder.newJob(cronJobClass)
                            .withIdentity("MyJob-" + s)
                            .build();

                    Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity("MyTrigger-" + s)
                            .withSchedule(CronScheduleBuilder.cronScheduleNonvalidatedExpression(s))
                            .build();
                    this.scheduler.scheduleJob(job, trigger);
                } catch (SchedulerException | ParseException e) {
                    e.printStackTrace();
                }
            }

            this.scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() throws SchedulerException {
        scheduler.shutdown();
    }
}
