package me.alan20210202.redutils.commands;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class WandCommand extends SimpleCommand {
    public WandCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        return emptyList();
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 0;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        sender.getInventory().addItem(new ItemStack(Material.RED_DYE, 1));
        sender.sendMessage(ChatColor.RED + "Given wand, have fun!");
    }
}
