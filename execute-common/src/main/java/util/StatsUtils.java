package util;

import com.cronutils.mapper.CronMapper;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import configuration.ExecuteSettings;
import org.bstats.MetricsBase;
import org.bstats.charts.AdvancedPie;
import org.bstats.charts.CustomChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class StatsUtils {
    public static final int BSTATS_SPIGOT_ID = 11307;
    public static final int BSTATS_VELOCITY_ID = 11308;
    public static final int BSTATS_BUNGEECORD_ID = 11309;

    public static List<CustomChart> getUniversalCharts(ExecuteSettings executeSettings) {
        List<CustomChart> charts = new ArrayList<CustomChart>();

        charts.add(new AdvancedPie("cron_expression", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() {
                Map<String, Integer> valueMap = new HashMap<>();

                for (Map.Entry<String, List<String>> entry : executeSettings.getCommands().entrySet()) {
                    valueMap.put(CronMapper.fromQuartzToUnix().map(new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ)).parse(entry.getKey())).asString(), entry.getValue().size());
                }

                return valueMap;
            }
        }));
        charts.add(new AdvancedPie("commands", new Callable<Map<String, Integer>>() {
            @Override
            public Map<String, Integer> call() {
                Map<String, Integer> valueMap = new HashMap<>();

                if (executeSettings.isIncludeCommandInStats()) {
                    for (Map.Entry<String, List<String>> entry : executeSettings.getCommands().entrySet()) {
                        for (String command : entry.getValue()) {
                            String commandFormatted = command.split(" ", 2)[0]; // TODO: maybe some checking and adding if its used multiple times?

                            valueMap.putIfAbsent(commandFormatted, 1);
                        }
                    }
                }

                valueMap.put("User opted out of this stat.", 1);

                return valueMap;
            }
        }));

        return charts;
    }
}
