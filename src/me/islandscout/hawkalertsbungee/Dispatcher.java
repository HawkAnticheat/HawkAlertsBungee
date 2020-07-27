package me.islandscout.hawkalertsbungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class Dispatcher implements Listener {

    private final Map<InetSocketAddress, String> servers;
    private static final String defaultPrefix = "&cHAWK: &7";
    private static final String defaultFormat = "[%server%] %flag%";

    private final String prefix;
    private final String format;

    Dispatcher(HawkAlertsBungee plugin) {
        this.servers = new HashMap<>();
        prefix = ChatColor.translateAlternateColorCodes('&', ConfigHelper.getOrSetDefault(defaultPrefix, plugin.getConfig(), "prefix"));
        format = ChatColor.translateAlternateColorCodes('&', ConfigHelper.getOrSetDefault(defaultFormat, plugin.getConfig(), "format"));
        BungeeCord.getInstance().getPluginManager().registerListener(plugin, this);
        Map<String, ServerInfo> serverInfoMap = BungeeCord.getInstance().getServers();
        for(String name : serverInfoMap.keySet()) {
            servers.put(serverInfoMap.get(name).getAddress(), name);
        }
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if(!e.getTag().equals("BungeeCord") || e.getData().length < 1)
            return;
        ByteArrayDataInput in = ByteStreams.newDataInput(e.getData());
        String msg = in.readUTF();
        if(!msg.equals("HawkACAlert"))
            return;
        msg = in.readUTF();
        String serverName = servers.get(e.getSender().getAddress());
        if(serverName == null) {
            return;
        }
        BaseComponent[] bComponents = TextComponent.fromLegacyText(prefix + format.replace("%server%", serverName).replace("%flag%", msg));
        for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers()) {
            if(!pp.hasPermission("hawkalertsbungee.alert") || pp.getServer().getInfo().getName().equals(serverName))
                continue;
            pp.sendMessage(bComponents);
        }
        BungeeCord.getInstance().getConsole().sendMessage(bComponents);
    }

}
