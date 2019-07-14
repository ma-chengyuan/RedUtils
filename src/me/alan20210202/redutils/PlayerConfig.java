package me.alan20210202.redutils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerConfig implements ConfigurationSerializable {
    private CommandState commandState = CommandState.NOTHING;
    private List<Location> selectedLocations = new ArrayList<>();
    private String nameOfSelection = null;
    private long timer = 0;

    private Map<String, InputConfig> inputConfigs = new HashMap<>();
    private Map<String, OutputConfig> outputConfigs = new HashMap<>();

    public PlayerConfig() {}

    public long getTimer() {
        return timer;
    }

    public void resetTimer() {
        timer = 0;
    }

    public void increaseTimer() {
        timer++;
    }

    public String getInputPreLog(String name) {
        // Plus 1 because usually it takes 1 extra tick for an input to take effect
        // (The redstones around will be updated in the next game tick!
        return ChatColor.RED + "[" + (timer + 1) + " ticks][IN][" + name + "] ";
    }

    public String getOutputPreLog(String name) { return ChatColor.RED + "[" + timer + " ticks][OUT][" + name + "] "; }

    public CommandState getCommandState() {
        return commandState;
    }

    public void setCommandState(CommandState commandState) {
        this.commandState = commandState;
    }

    public String getNameOfSelection() {
        return nameOfSelection;
    }

    public void setNameOfSelection(String nameOfSelection) {
        this.nameOfSelection = nameOfSelection;
    }

    public List<Location> getSelectedLocations() {
        return selectedLocations;
    }

    public Map<String, InputConfig> getInputConfigs() {
        return inputConfigs;
    }

    public Map<String, OutputConfig> getOutputConfigs() {
        return outputConfigs;
    }

    // Serialization

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Map<String, Object>> inputs = new HashMap<>();
        for (Map.Entry<String, InputConfig> entry : inputConfigs.entrySet())
            inputs.put(entry.getKey(), entry.getValue().serialize());
        ret.put("inputConfigs", inputs);
        Map<String, Map<String, Object>> outputs = new HashMap<>();
        for (Map.Entry<String, OutputConfig> entry : outputConfigs.entrySet())
            outputs.put(entry.getKey(), entry.getValue().serialize());
        ret.put("outputConfigs", outputs);
        return ret;
    }

    @SuppressWarnings("unchecked")
    public PlayerConfig(Map<String, Object> map) {
        Map<String, Map<String, Object>> inputs = (Map<String, Map<String, Object>>)map.get("inputConfigs");
        for (Map.Entry<String, Map<String, Object>> entry: inputs.entrySet())
            this.inputConfigs.put(entry.getKey(), new InputConfig(entry.getValue()));
        Map<String, Map<String, Object>> outputs = (Map<String, Map<String, Object>>)map.get("outputConfigs");
        for (Map.Entry<String, Map<String, Object>> entry: outputs.entrySet())
            this.outputConfigs.put(entry.getKey(), new OutputConfig(entry.getValue()));
    }
}
