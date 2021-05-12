package configuration;

import adapters.LoggerAdapter;
import com.cronutils.builder.CronBuilder;
import com.cronutils.mapper.CronMapper;
import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import com.moandjiezana.toml.Toml;
import util.VersionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteSettings {
    private final File dataFolder;
    private final File file;

    private HashMap<String, List<String>> commands = new HashMap<String, List<String>>();;

    private final Double configVersion;
    private boolean enabled;
    private boolean includeCommandInStats;

    public ExecuteSettings(File dataFolder, Object logger) {
        this.dataFolder = dataFolder;
        this.file = new File(this.dataFolder, "config.toml");

        saveDefaultConfig();
        Toml toml = loadConfig();

        this.enabled = toml.getBoolean("plugin.enabled");

        // Version
        this.configVersion = toml.getDouble("developer-info.config-version");

        if (!VersionUtils.isLatestConfigVersion(this)) {
            LoggerAdapter.warn(logger, "Your Config is out of date (Latest: " + VersionUtils.CONFIG_VERSION + ", Config Version: " + this.getConfigVersion() + ")!");
            LoggerAdapter.warn(logger, "Please backup your current config.toml, and delete the current one. A new config will then be created on the next proxy launch.");
            LoggerAdapter.warn(logger, "The plugin's functionality will not be enabled until the config is updated.");
            this.setEnabled(false);
            return;
        }

        // Stats
        this.includeCommandInStats = toml.getBoolean("plugin.include-command-in-stats");

        // Commands
        Toml commandsToml = toml.getTable("commands");

        for (Map.Entry<String, Object> entry : commandsToml.toMap().entrySet()) {
            String key = entry.getKey().replace("\"", "");
            List<String> commands = commandsToml.getList(entry.getKey());

            LoggerAdapter.info(logger, "Loading cron job: " + key);

            key = CronMapper.fromUnixToQuartz().map(new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX)).parse(key)).asString();

            this.commands.put(key, commands);
        }
    }

    private void saveDefaultConfig() {
        if (!dataFolder.exists()) dataFolder.mkdir();
        if (file.exists()) return;

        try (InputStream in = ExecuteSettings.class.getResourceAsStream("/config.toml")) {
            Files.copy(in, file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File getConfigFile() {
        return new File(dataFolder, "config.toml");
    }

    private Toml loadConfig() {
        return new Toml().read(getConfigFile());
    }

    public Double getConfigVersion() {
        return configVersion;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public HashMap<String, List<String>> getCommands() {
        return commands;
    }

    public boolean isIncludeCommandInStats() {
        return includeCommandInStats;
    }
}
