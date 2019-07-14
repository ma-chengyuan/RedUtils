package me.alan20210202.redutils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.*;

public class PlayerConfigManager {
    private final Map<UUID, PlayerConfig> configs;
    private final RedUtils plugin;

    public PlayerConfigManager(RedUtils plugin) {
        configs = new HashMap<>();
        this.plugin = plugin;
    }

    public void loadConfigsFromFiles() {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) return;
        assert folder.listFiles() != null; // Make IDEA happy
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            UUID uuid = UUID.fromString(file.getName().replace(".yml", ""));
            YamlConfiguration config = new YamlConfiguration();
            try {
                config.load(file);
                configs.put(uuid, config.getObject("playerConfig", PlayerConfig.class));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void saveConfigsToFiles() {
        File folder = plugin.getDataFolder();
        if (!folder.exists()) folder.mkdirs();
        for (Map.Entry<UUID, PlayerConfig> entry: configs.entrySet()) {
            YamlConfiguration config = new YamlConfiguration();
            config.set("playerConfig", entry.getValue());
            try {
                config.save(new File(folder, entry.getKey().toString() + ".yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public PlayerConfig get(Player player) {
        UUID uuid = player.getUniqueId();
        if (!configs.containsKey(uuid))
            configs.put(uuid, new PlayerConfig());
        return configs.get(uuid);
    }

    public void checkAndLogEverything() {
        List<String> toBeRemoved = new ArrayList<>();
        for (Map.Entry<UUID, PlayerConfig> entry : configs.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());
            if (player == null) continue;
            PlayerConfig config = entry.getValue();
            config.increaseTimer();

            for (Map.Entry<String, InputConfig> entryInput : config.getInputConfigs().entrySet()) {
                String name = entryInput.getKey();
                InputConfig inputConfig = entryInput.getValue();
                if (!inputConfig.isStillValid()) {
                    player.sendMessage(ChatColor.RED + "Input config \"" + name + "\" is no longer valid and thus deleted automatically");
                    toBeRemoved.add(name);
                    continue;
                }
                inputConfig.updateStates();
            }
            for (String name : toBeRemoved)
                config.getInputConfigs().remove(name);
            toBeRemoved.clear();

            for (Map.Entry<String, OutputConfig> entryOutput : config.getOutputConfigs().entrySet()) {
                String name = entryOutput.getKey();
                OutputConfig outputConfig = entryOutput.getValue();
                if (!outputConfig.isStillValid()) {
                    player.sendMessage(ChatColor.RED + "Output config \"" + name + "\" is no longer valid and thus deleted automatically");
                    toBeRemoved.add(name);
                    continue;
                }
                if (outputConfig.updateStates() && outputConfig.isLogging()) {
                    player.sendMessage(config.getOutputPreLog(name) + outputConfig.getDisplay());
                }
            }
            for (String name : toBeRemoved)
                config.getOutputConfigs().remove(name);
            toBeRemoved.clear();
        }
    }
}
