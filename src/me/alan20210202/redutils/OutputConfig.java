package me.alan20210202.redutils;

import me.alan20210202.redutils.utils.ConvertUtils;
import me.alan20210202.redutils.utils.SerializationUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OutputConfig implements ConfigurationSerializable {
    public static final Map<String, Function<List<Boolean>, String>> DISPLAY_FORMATS;

    static {
        DISPLAY_FORMATS = new HashMap<>();
        DISPLAY_FORMATS.put("bin", ConvertUtils::toRawBinStr);
        DISPLAY_FORMATS.put("dec", ConvertUtils::toDecimal);
        DISPLAY_FORMATS.put("decFrac", ConvertUtils::toDecimalFrac);
        DISPLAY_FORMATS.put("hex", ConvertUtils::toHexPadLeft);
        DISPLAY_FORMATS.put("hexPadRight", ConvertUtils::toHexPadRight);
    }

    private List<Location> outputs;
    private List<Boolean> states;
    private boolean logging;
    private String displayFormat;
    private int separateInterval;

    public int getOutputCount() {
        return outputs.size();
    }

    public OutputConfig(List<Location> outputs) {
        this.outputs = new ArrayList<>(outputs);
        this.states = outputs.stream().map(OutputConfig::getStateAt).collect(Collectors.toList());
        this.logging = true;
        this.displayFormat = "bin";
        this.separateInterval = 0;
        assert this.outputs.size() == this.states.size();
    }

    public static boolean isValidOutput(Location loc) {
        Block block = loc.getBlock();
        BlockData data = block.getBlockData();
        return data instanceof Powerable || data instanceof AnaloguePowerable;
    }

    public boolean updateStates() {
        assert isStillValid();
        boolean changed = false;
        for (int i = 0; i < outputs.size(); i++) {
            changed |= getStateAt(outputs.get(i)) != states.get(i);
            states.set(i, getStateAt(outputs.get(i)));
        }
        return changed;
    }

    public boolean isStillValid() {
        for (Location loc : outputs)
            if (!isValidOutput(loc))
                return false;
        return true;
    }

    private static boolean getStateAt(Location loc) {
        assert isValidOutput(loc);
        Block block = loc.getBlock();
        BlockData data = block.getBlockData();
        if (data instanceof Powerable)
            return ((Powerable) data).isPowered();
        else
            return ((AnaloguePowerable) data).getPower() > 0;
    }

    public String getDisplay() {
        assert DISPLAY_FORMATS.containsKey(displayFormat);
        String display = DISPLAY_FORMATS.get(displayFormat).apply(states);
        if (separateInterval == 0) return display;
        if (display.contains(".")) {
            String[] parts = display.split("\\.");
            return ConvertUtils.separatePadLeft(parts[0], separateInterval)
                    + "." + ConvertUtils.separatePadRight(parts[1], separateInterval);
        } else
            return ConvertUtils.separatePadLeft(display, separateInterval);
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public int getSeparateInterval() {
        return separateInterval;
    }

    public void setSeparateInterval(int separateInterval) {
        this.separateInterval = separateInterval;
    }

    // Serialization

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("logging", logging);
        map.put("displayFormat", displayFormat);
        map.put("separateInterval", separateInterval);
        map.put("outputs", SerializationUtils.serializeLocsAndStates(outputs, states));
        return map;
    }

    @SuppressWarnings("unchecked")
    public OutputConfig(Map<String, Object> map) {
        this.outputs = new ArrayList<>();
        this.states = new ArrayList<>();
        this.logging = (Boolean) map.get("logging");
        this.displayFormat = (String) map.get("displayFormat");
        this.separateInterval = (Integer) map.get("separateInterval");
        SerializationUtils.deserializeLocsAndStates(
                (ArrayList<Map<String, Object>>) map.get("outputs"), this.outputs, this.states);
    }
}
