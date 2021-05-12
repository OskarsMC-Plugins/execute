package com.oskarsmc.execute;

import configuration.ExecuteSettings;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.quartz.SchedulerException;
import util.ExecuteTemplate;
import util.StatsUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class ExecuteSpigot extends JavaPlugin {
    public ExecuteSettings executeSettings;
    public ExecuteTemplate executeTemplate;

    // Metrics
    public Metrics metrics;
    public static AtomicInteger executed = new AtomicInteger(0);

    @Override
    public void onEnable() {
        // Plugin startup logic
        Logger logger = Bukkit.getLogger();
        this.executeSettings = new ExecuteSettings(getDataFolder(), logger);
        this.executeTemplate = new ExecuteTemplate(executeSettings, CronJob.class);

        this.metrics = new Metrics(this, StatsUtils.BSTATS_SPIGOT_ID);

        for (CustomChart chart : StatsUtils.getUniversalCharts(this.executeSettings)) {
            this.metrics.addCustomChart(chart);
        }

        this.metrics.addCustomChart(new SingleLineChart("executed_commands", new Callable<Integer>() {
            @Override
            public Integer call() {
                int ret = executed.get();
                executed.set(0);
                return ret;
            }
        }));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            this.executeTemplate.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
