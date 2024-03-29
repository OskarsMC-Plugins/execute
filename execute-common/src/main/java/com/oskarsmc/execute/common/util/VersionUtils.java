package com.oskarsmc.execute.common.util;

import com.oskarsmc.execute.common.configuration.ExecuteSettings;

public class VersionUtils {

    public static final double CONFIG_VERSION = 0.1;

    public static boolean isLatestConfigVersion(ExecuteSettings executeSettings) {
        if (executeSettings.getConfigVersion() == null) {
            return false;
        }
        return executeSettings.getConfigVersion() == CONFIG_VERSION;
    }
}