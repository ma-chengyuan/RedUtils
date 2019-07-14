package me.alan20210202.redutils;

import me.alan20210202.redutils.utils.SerializationUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Switch;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;
import java.util.stream.Collectors;

public class InputConfig implements ConfigurationSerializable {
    private final List<Location> inputs;
    private List<Boolean> states;

    public int getInputCount() {
        return inputs.size();
    }

    public InputConfig(List<Location> inputs) {
        this.inputs = new ArrayList<>(inputs); // Avoid weird problems
        this.states = inputs.stream().map(InputConfig::getStateAt).collect(Collectors.toList());
        assert this.inputs.size() == this.states.size();
    }

    public void toggle() {
        assert inputs.size() == 1;
        setStateAt(inputs.get(0), !getStateAt(inputs.get(0)));
        states.set(0, getStateAt(inputs.get(0)));
    }

    public void setStates(List<Boolean> newStates) {
        assert this.states.size() == newStates.size();
        this.states = newStates;
        assert isStillValid();
        for (int i = 0; i < inputs.size(); i++)
            setStateAt(inputs.get(i), states.get(i));
    }

    public List<Boolean> getStates() {
        return states;
    }

    public void updateStates() {
        assert isStillValid();
        for (int i = 0; i < inputs.size(); i++)
            states.set(i, getStateAt(inputs.get(i)));
    }

    public boolean isStillValid() {
        for (Location loc : inputs)
            if (!isValidInput(loc))
                return false;
        return true;
    }

    public static boolean isValidInput(Location loc) {
        Block block = loc.getBlock();
        return block.getBlockData() instanceof Switch;
    }

    private static boolean getStateAt(Location loc) {
        assert isValidInput(loc);
        Block block = loc.getBlock();
        BlockData data = block.getBlockData();
        assert data instanceof Powerable;
        return ((Powerable) data).isPowered();
    }

    private static void setStateAt(Location loc, boolean powered) {
        assert isValidInput(loc);
        Block block = loc.getBlock();
        BlockData data = block.getBlockData();
        assert data instanceof Switch;
        // First power the lever
        Switch s = (Switch) data;
        s.setPowered(powered);
        block.setBlockData(s);
        block.getState().update(true);

        // Then update the block
        final Block relative;
        switch (s.getFace()) {
        case WALL: relative = block.getRelative(s.getFacing(), -1); break;
        case FLOOR: relative = block.getRelative(BlockFace.DOWN); break;
        default: relative = block.getRelative(BlockFace.UP); break;
        }

        // Nice fix, works like a charm
        // Credit: https://www.spigotmc.org/threads/powering-lever-and-updating-state.141261/
        BlockState initialState = relative.getState();
        BlockState state = relative.getState();
        state.setType(Material.AIR);
        state.update(true, false);
        initialState.update(true);
    }

    // Serialization

    @Override
    public Map<String, Object> serialize() {
        return Collections.singletonMap("inputs", SerializationUtils.serializeLocsAndStates(inputs, states));
    }

    @SuppressWarnings("unchecked")
    public InputConfig(Map<String, Object> map) {
        this.inputs = new ArrayList<>();
        this.states = new ArrayList<>();
        SerializationUtils.deserializeLocsAndStates((ArrayList<Map<String, Object>>) map.get("inputs"),
                this.inputs, this.states);
    }
}
