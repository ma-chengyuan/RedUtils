package me.alan20210202.redutils.commands.output;

import me.alan20210202.redutils.OutputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class OutputFormatCommand extends SimpleCommand {
    public OutputFormatCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getOutputConfigs().keySet());
        if (args.length == 2)
            return tryCompleteWith(args[1], OutputConfig.DISPLAY_FORMATS.keySet());
        return emptyList();
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 2;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        String name = args[0], format = args[1];
        OutputConfig outputConfig = config.getOutputConfigs().get(name);
        if (outputConfig == null) {
            sender.sendMessage(ChatColor.RED + "No output config named \"" + name + "\"");
            return;
        }
        if (!OutputConfig.DISPLAY_FORMATS.containsKey(format)) {
            sender.sendMessage(ChatColor.RED + "Invalid format name");
            return;
        }
        outputConfig.setDisplayFormat(format);
        sender.sendMessage(ChatColor.RED + "Display format set for output config " + name + ": " + format);
    }
}
