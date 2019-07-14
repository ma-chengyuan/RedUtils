package me.alan20210202.redutils;

import me.alan20210202.redutils.utils.InvalidSliceException;
import me.alan20210202.redutils.utils.SliceUtils;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class InteractEventListener implements Listener {
    private final RedUtils plugin;

    InteractEventListener(RedUtils plugin) {
        this.plugin = plugin;
    }

    private static void fancyParticles(Location loc) {
        assert loc.getWorld() != null;
        loc.getWorld().playEffect(loc, Effect.VILLAGER_PLANT_GROW, 10);
    }

    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack holding = event.getItem();
            if (holding != null && holding.getType().equals(Material.RED_DYE)) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                Block block = event.getClickedBlock();
                assert block != null;
                Location loc = block.getLocation();
                PlayerConfig config = plugin.getPlayerConfigManager().get(player);

                switch (config.getCommandState()) {
                case INPUT_SEL_SLICE:
                    if (config.getSelectedLocations().size() == 0) {
                        config.getSelectedLocations().add(loc);
                        player.sendMessage(ChatColor.RED + "Then right click the least significant bit");
                    } else {
                        Location high = config.getSelectedLocations().get(0);
                        try {
                            List<Location> inputs = SliceUtils.getLocationsFromSlice(high, loc, InputConfig::isValidInput);
                            if (inputs.isEmpty())
                                player.sendMessage(ChatColor.RED + "No valid inputs found in the slice!");
                            else {
                                for (Location tmp : inputs) fancyParticles(tmp);
                                if (config.getInputConfigs().containsKey(config.getNameOfSelection()))
                                    player.sendMessage(ChatColor.RED + "Overriding previous input config");
                                config.getInputConfigs().put(config.getNameOfSelection(), new InputConfig(inputs));
                            }
                        } catch (InvalidSliceException e) {
                            player.sendMessage(ChatColor.RED + e.getMessage());
                        } finally {
                            config.setNameOfSelection(null);
                            config.getSelectedLocations().clear();
                            config.setCommandState(CommandState.NOTHING);
                        }
                    }
                    break;
                case INPUT_SEL_MULTI:
                    if (!InputConfig.isValidInput(loc))
                        player.sendMessage(ChatColor.RED + "The selected input block is invalid!");
                    else {
                        fancyParticles(loc);
                        config.getSelectedLocations().add(loc);
                        player.sendMessage(ChatColor.RED + "Selected input block at " + block.getLocation().toVector());
                    }
                    break;
                case INPUT_SEL_SINGLE:
                    if (!InputConfig.isValidInput(loc))
                        player.sendMessage(ChatColor.RED + "The selected input block is invalid!");
                    else {
                        fancyParticles(loc);
                        player.sendMessage(ChatColor.RED + "Selected input block at " + block.getLocation().toVector());
                        if (config.getInputConfigs().containsKey(config.getNameOfSelection()))
                            player.sendMessage(ChatColor.RED + "Overriding previous input config");
                        config.getInputConfigs().put(config.getNameOfSelection(), new InputConfig(Collections.singletonList(loc)));
                        config.setNameOfSelection(null);
                        config.getSelectedLocations().clear();
                        config.setCommandState(CommandState.NOTHING);
                    }
                    break;
                case OUTPUT_SEL_SLICE:
                    if (config.getSelectedLocations().size() == 0) {
                        config.getSelectedLocations().add(loc);
                        player.sendMessage(ChatColor.RED + "Then right click the least significant bit");
                    } else {
                        Location high = config.getSelectedLocations().get(0);
                        try {
                            List<Location> output = SliceUtils.getLocationsFromSlice(high, loc, OutputConfig::isValidOutput);
                            if (output.isEmpty())
                                player.sendMessage(ChatColor.RED + "No valid outputs found in the slice!");
                            else {
                                for (Location tmp : output) fancyParticles(tmp);
                                if (config.getOutputConfigs().containsKey(config.getNameOfSelection()))
                                    player.sendMessage(ChatColor.RED + "Overriding previous output config");
                                config.getOutputConfigs().put(config.getNameOfSelection(), new OutputConfig(output));
                            }
                        } catch (InvalidSliceException e) {
                            player.sendMessage(ChatColor.RED + e.getMessage());
                        } finally {
                            config.setNameOfSelection(null);
                            config.getSelectedLocations().clear();
                            config.setCommandState(CommandState.NOTHING);
                        }
                    }
                    break;
                case OUTPUT_SEL_MULTI:
                    if (!OutputConfig.isValidOutput(loc))
                        player.sendMessage(ChatColor.RED + "The selected output block is invalid!");
                    else {
                        fancyParticles(loc);
                        config.getSelectedLocations().add(loc);
                        player.sendMessage(ChatColor.RED + "Selected output block at " + block.getLocation().toVector());
                    }
                    break;
                case OUTPUT_SEL_SINGLE:
                    if (!OutputConfig.isValidOutput(loc))
                        player.sendMessage(ChatColor.RED + "The selected output block is invalid!");
                    else {
                        fancyParticles(loc);
                        player.sendMessage(ChatColor.RED + "Selected output block at " + block.getLocation().toVector());
                        if (config.getOutputConfigs().containsKey(config.getNameOfSelection()))
                            player.sendMessage(ChatColor.RED + "Overriding previous output config");
                        config.getOutputConfigs().put(config.getNameOfSelection(), new OutputConfig(Collections.singletonList(loc)));
                        config.setNameOfSelection(null);
                        config.getSelectedLocations().clear();
                        config.setCommandState(CommandState.NOTHING);
                    }
                }
            }
        }
    }
}
