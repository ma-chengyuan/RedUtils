package me.alan20210202.redutils.commands.output;

import me.alan20210202.redutils.OutputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class OutputLogCommand extends SimpleCommand {
    public OutputLogCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getOutputConfigs().keySet());
        return null;
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 1;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        String name = args[0];
        OutputConfig outputConfig = config.getOutputConfigs().get(name);
        if (outputConfig == null) {
            sender.sendMessage(ChatColor.RED + "No output config named \"" + name + "\"");
            return;
        }
        outputConfig.setLogging(!outputConfig.isLogging());
        sender.sendMessage(ChatColor.RED + "Active logging for output config " + name + ": " + outputConfig.isLogging()
        );
    }
}
