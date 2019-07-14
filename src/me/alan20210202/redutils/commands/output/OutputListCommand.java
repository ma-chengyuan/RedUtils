package me.alan20210202.redutils.commands.output;

import me.alan20210202.redutils.InputConfig;
import me.alan20210202.redutils.OutputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class OutputListCommand extends SimpleCommand {
    public OutputListCommand(RedUtils plugin) {
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
        sender.sendMessage(ChatColor.RED + "Current output configs:");
        for (Map.Entry<String, OutputConfig> entry : config.getOutputConfigs().entrySet())
            sender.sendMessage(ChatColor.RED + " " + entry.getKey() + " (" + entry.getValue().getOutputCount() + " bit(s))");
    }
}
