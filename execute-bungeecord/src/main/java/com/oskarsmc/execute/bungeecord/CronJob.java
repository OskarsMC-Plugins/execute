package com.oskarsmc.execute.bungeecord;

import net.md_5.bungee.api.ProxyServer;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Map;

public class CronJob implements Job {
    public static ExecuteBungeeCord plugin;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (jobExecutionContext.getTrigger() instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) jobExecutionContext.getTrigger();
            String cronExpression = cronTrigger.getCronExpression();

            for (Map.Entry<String, List<String>> entry : plugin.executeSettings.getCommands().entrySet()) {
                if ((entry.getKey()).equals(cronExpression)) {
                    for (String command : entry.getValue()) {
                        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), command);
                        plugin.executed.incrementAndGet();
                    }
                }
            }
        } else {
            throw new IllegalStateException("How did we get here?");
        }
    }
}
