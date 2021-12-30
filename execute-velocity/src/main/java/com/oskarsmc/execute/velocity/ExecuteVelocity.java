package com.oskarsmc.execute.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.oskarsmc.execute.common.configuration.ExecuteSettings;
import org.bstats.charts.CustomChart;
import org.bstats.charts.SingleLineChart;
import org.bstats.velocity.Metrics;
import org.quartz.*;
import org.slf4j.Logger;
import com.oskarsmc.execute.common.util.ExecuteTemplate;
import com.oskarsmc.execute.common.util.StatsUtils;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecuteVelocity {

    @Inject
    private Logger logger;

    @Inject
    public ProxyServer proxyServer;

    @Inject
    private @DataDirectory
    Path dataDirectory;

    @Inject
    private Metrics.Factory metricsFactory;

    public ExecuteSettings executeSettings;
    public ExecuteTemplate executeTemplate;

    // Metrics
    public Metrics metrics;
    public AtomicInteger executed = new AtomicInteger(0);

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CronJob.plugin = this;
        this.executeSettings = new ExecuteSettings(dataDirectory.toFile(), logger);
        this.executeTemplate = new ExecuteTemplate(this.executeSettings, CronJob.class);

        this.metrics = metricsFactory.make(this, StatsUtils.BSTATS_VELOCITY_ID);

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

    @Subscribe
    public void onDisable(ProxyShutdownEvent event) {
        try {
            this.executeTemplate.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
