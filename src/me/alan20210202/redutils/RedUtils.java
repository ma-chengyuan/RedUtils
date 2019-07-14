package me.alan20210202.redutils;

import me.alan20210202.redutils.commands.RedUtilsAsCommand;
import me.alan20210202.redutils.commands.RedUtilsCommand;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class RedUtils extends JavaPlugin {
    private PlayerConfigManager playerConfigManager;

    public PlayerConfigManager getPlayerConfigManager() {
        return playerConfigManager;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Logger log = getLogger();
        log.info("Greetings from RedUtils!");

        ConfigurationSerialization.registerClass(InputConfig.class);
        ConfigurationSerialization.registerClass(OutputConfig.class);
        ConfigurationSerialization.registerClass(PlayerConfig.class);

        // Initialize our commands
        PluginCommand pluginRedUtilsCommand = getCommand("redutils");
        assert pluginRedUtilsCommand != null;
        RedUtilsCommand redUtilsCommand = new RedUtilsCommand(this);
        pluginRedUtilsCommand.setExecutor(redUtilsCommand);
        pluginRedUtilsCommand.setTabCompleter(redUtilsCommand);

        PluginCommand pluginRUCommand = getCommand("ru");
        assert pluginRUCommand != null;
        pluginRUCommand.setExecutor(redUtilsCommand);
        pluginRUCommand.setTabCompleter(redUtilsCommand);

        PluginCommand pluginRedUtilsAsCommand = getCommand("redutilsas");
        assert pluginRedUtilsAsCommand != null;
        RedUtilsAsCommand redUtilsAsCommand = new RedUtilsAsCommand(redUtilsCommand);
        pluginRedUtilsAsCommand.setExecutor(redUtilsAsCommand);
        pluginRedUtilsAsCommand.setTabCompleter(redUtilsAsCommand);

        PluginCommand pluginRUAsCommand = getCommand("ruas");
        assert pluginRUAsCommand != null;
        pluginRUAsCommand.setExecutor(redUtilsAsCommand);
        pluginRUAsCommand.setTabCompleter(redUtilsAsCommand);
        // Load configs from files
        playerConfigManager = new PlayerConfigManager(this);
        playerConfigManager.loadConfigsFromFiles();

        Server server = getServer();
        server.getPluginManager().registerEvents(new InteractEventListener(this), this);
        server.getScheduler().scheduleSyncRepeatingTask(this, playerConfigManager::checkAndLogEverything, 0, 1);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        playerConfigManager.saveConfigsToFiles();
        Logger log = getLogger();
        log.info("Saved states to files");
    }
}
