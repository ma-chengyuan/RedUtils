package me.alan20210202.redutils.commands.input;

import me.alan20210202.redutils.InputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class InputListCommand extends SimpleCommand {
    public InputListCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        return null;
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 0;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        sender.sendMessage(ChatColor.RED + "Current input configs:");
        for (Map.Entry<String, InputConfig> entry : config.getInputConfigs().entrySet())
            sender.sendMessage(ChatColor.RED + " " + entry.getKey() + " (" + entry.getValue().getInputCount() + " bit(s))");
    }
}
