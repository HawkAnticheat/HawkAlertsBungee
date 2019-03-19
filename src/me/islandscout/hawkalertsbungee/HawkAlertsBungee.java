package me.islandscout.hawkalertsbungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class HawkAlertsBungee extends Plugin {

    private File file;
    private Configuration config;

    private Dispatcher dispatcher;

    @Override
    public void onEnable() {
        file = new File(ProxyServer.getInstance().getPluginsFolder() + File.separator + "config.yml");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dispatcher = new Dispatcher(this);

        saveConfig();
    }

    @Override
    public void onDisable() {
        dispatcher = null;
    }

    public Dispatcher getDispatcher() {
        return dispatcher;
    }

    public Configuration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
