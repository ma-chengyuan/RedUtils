package me.alan20210202.redutils.commands.input;

import me.alan20210202.redutils.InputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputToggleCommand extends SimpleCommand {
    public InputToggleCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getInputConfigs().entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().getInputCount() == 1)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList()));
        return emptyList();
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 1;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        String name = args[0];
        InputConfig inputConfig = config.getInputConfigs().get(name);
        if (inputConfig == null) {
            sender.sendMessage(ChatColor.RED + "No input config named \"" + name + "\"");
            return;
        }
        if (inputConfig.getInputCount() > 1)
            sender.sendMessage(ChatColor.RED + "Toggle command only applied to single-bit input");
        else {
            inputConfig.toggle();
            sender.sendMessage(config.getInputPreLog(name) + "Toggled to " + inputConfig.getStates().get(0));
        }
    }
}
