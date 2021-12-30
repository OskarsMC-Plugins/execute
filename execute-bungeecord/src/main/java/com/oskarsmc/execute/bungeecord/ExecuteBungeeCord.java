package com.oskarsmc.execute.bungeecord;

import com.oskarmc.execute.common.configuration.ExecuteSettings;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.CustomChart;
import org.bstats.charts.SingleLineChart;
import org.quartz.SchedulerException;
import com.oskarmc.execute.common.util.ExecuteTemplate;
import com.oskarmc.execute.common.util.StatsUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public final class ExecuteBungeeCord extends Plugin {
    public ExecuteSettings executeSettings;
    public ExecuteTemplate executeTemplate;

    // Metrics
    public Metrics metrics;
    public AtomicInteger executed = new AtomicInteger(0);

    @Override
    public void onEnable() {
        // Plugin startup logic
        Logger logger = ProxyServer.getInstance().getLogger();
        CronJob.plugin = this;
        this.executeSettings = new ExecuteSettings(getDataFolder(), logger);
        this.executeTemplate = new ExecuteTemplate(this.executeSettings, CronJob.class);

        this.metrics = new Metrics(this, StatsUtils.BSTATS_BUNGEECORD_ID);

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
