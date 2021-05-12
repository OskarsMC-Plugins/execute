package com.oskarsmc.execute;

import org.bukkit.Bukkit;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;

public class CronJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (jobExecutionContext.getTrigger() instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) jobExecutionContext.getTrigger();
            String cronExpression = cronTrigger.getCronExpression();
            ExecuteSpigot executeSpigot = ExecuteSpigot.getPlugin(ExecuteSpigot.class);

            Bukkit.getScheduler().runTask(executeSpigot, new Runnable() {
                @Override
                public void run() {
                    for (Map.Entry<String, List<String>> entry : executeSpigot.executeSettings.getCommands().entrySet()) {
                        if ((entry.getKey()).equals(cronExpression)) {
                            for (String command : entry.getValue()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                                ExecuteSpigot.executed.incrementAndGet();
                            }
                        }
                    }
                }
            });
        } else {
            throw new IllegalStateException("How did we get here?");
        }
    }
}
