package me.alan20210202.redutils.commands.output;

import me.alan20210202.redutils.OutputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class OutputSeparateCommand extends SimpleCommand {
    public OutputSeparateCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getOutputConfigs().keySet());
        return emptyList();
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 2;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        String name = args[0], strInterval = args[1];
        OutputConfig outputConfig = config.getOutputConfigs().get(name);
        if (outputConfig == null) {
            sender.sendMessage(ChatColor.RED + "No output config named \"" + name + "\"");
            return;
        }
        try {
            int interval = Integer.parseInt(strInterval);
            outputConfig.setSeparateInterval(interval);
            sender.sendMessage(ChatColor.RED + "Separator Interval set for output config " + name + ": " + interval);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Number format error!");
        }
    }
}
