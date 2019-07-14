package me.alan20210202.redutils.commands;

import me.alan20210202.redutils.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class DoneMultiSelectionCommand extends SimpleCommand {
    public DoneMultiSelectionCommand(RedUtils plugin) {
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
        if (config.getSelectedLocations().isEmpty())
            sender.sendMessage(ChatColor.RED + "You have just selected none inputs yet!");
        else {
            if (config.getCommandState() == CommandState.INPUT_SEL_MULTI) {
                if (config.getInputConfigs().containsKey(config.getNameOfSelection()))
                    sender.sendMessage(ChatColor.RED + "Overriding previous input config");
                config.getInputConfigs().put(config.getNameOfSelection(), new InputConfig(config.getSelectedLocations()));
                config.getSelectedLocations().clear();
                config.setCommandState(CommandState.NOTHING);
                config.setNameOfSelection(null);
            } else if (config.getCommandState() == CommandState.OUTPUT_SEL_MULTI) {
                if (config.getOutputConfigs().containsKey(config.getNameOfSelection()))
                    sender.sendMessage(ChatColor.RED + "Overriding previous output config");
                config.getOutputConfigs().put(config.getNameOfSelection(), new OutputConfig(config.getSelectedLocations()));
                config.getSelectedLocations().clear();
                config.setCommandState(CommandState.NOTHING);
                config.setNameOfSelection(null);
            } else
                sender.sendMessage("It seems that you are not selecting anything!");
        }
    }
}
