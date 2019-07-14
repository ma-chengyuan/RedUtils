package me.alan20210202.redutils.commands.input;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class InputDeleteCommand extends SimpleCommand {
    public InputDeleteCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getInputConfigs().keySet());
        return null;
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 1;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        if (config.getInputConfigs().containsKey(args[0])) {
            config.getInputConfigs().remove(args[0]);
            sender.sendMessage(ChatColor.RED + "Config successfully deleted");
        } else
            sender.sendMessage(ChatColor.RED + "The config doesn't exist");
    }
}
