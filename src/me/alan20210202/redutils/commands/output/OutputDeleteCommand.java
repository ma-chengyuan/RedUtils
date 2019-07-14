package me.alan20210202.redutils.commands.output;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class OutputDeleteCommand extends SimpleCommand {
    public OutputDeleteCommand(RedUtils plugin) {
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
        if (config.getOutputConfigs().containsKey(args[0])) {
            config.getOutputConfigs().remove(args[0]);
            sender.sendMessage(ChatColor.RED + "Config successfully deleted");
        } else
            sender.sendMessage(ChatColor.RED + "The config doesn't exist");
    }
}
