package me.alan20210202.redutils.commands.input;

import me.alan20210202.redutils.InputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InputOnesCommand extends SimpleCommand {
    public InputOnesCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getInputConfigs().keySet());
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
        List<Boolean> newStates = new ArrayList<>();
        for (int i = 0; i < inputConfig.getInputCount(); i++)
            newStates.add(true);
        inputConfig.setStates(newStates);
        sender.sendMessage(config.getInputPreLog(name) + "filled with ones");
    }
}
