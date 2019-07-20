package me.alan20210202.redutils.commands.input;

import me.alan20210202.redutils.CommandState;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class InputCreateCommand extends SimpleCommand {
    private static final List<String> SELECTION_MODE = Arrays.asList("single", "multi", "slice");

    public InputCreateCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getInputConfigs().keySet());
        if (args.length == 2)
            return tryCompleteWith(args[1], SELECTION_MODE);
        return emptyList();
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 2;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        config.setNameOfSelection(args[0]);
        switch (args[1]) { // Selection mode
        case "slice":
            sender.sendMessage(ChatColor.RED + "Slice mode: first right click the most significant bit");
            config.setCommandState(CommandState.INPUT_SEL_SLICE);
            break;
        case "single":
            sender.sendMessage(ChatColor.RED + "Single mode: right click the single input");
            config.setCommandState(CommandState.INPUT_SEL_SINGLE);
            break;
        case "multi":
            sender.sendMessage(ChatColor.RED + "Multi mode: right click all the bits from highest to least significance");
            sender.sendMessage(ChatColor.RED + "Then run /redutils doneMultiSel to complete");
            config.setCommandState(CommandState.INPUT_SEL_MULTI);
            break;
        default:
            sender.sendMessage(ChatColor.RED + "Unknown selection mode: " + args[1]);
            break;
        }
    }
}
