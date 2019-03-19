package me.islandscout.hawkalertsbungee;

import net.md_5.bungee.config.Configuration;

class ConfigHelper {

    static String getOrSetDefault(String defaultValue, Configuration config, String path) {
        String result = config.getString(path);
        if(result == null || result.equals("")) {
            config.set(path, defaultValue);
            result = defaultValue;
        }
        return result;
    }
}
