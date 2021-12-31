package com.oskarsmc.execute.common.util;

import com.oskarsmc.execute.common.configuration.ExecuteSettings;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.ParseException;
import java.util.Properties;

public class ExecuteTemplate {
    public Scheduler scheduler;

    public ExecuteTemplate(ExecuteSettings executeSettings, Class<? extends Job> cronJobClass) {
        Properties schedulerProperties = new Properties();
        schedulerProperties.setProperty("org.quartz.threadPool.threadCount", "10");

        try {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory(schedulerProperties);
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
