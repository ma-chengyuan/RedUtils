package me.alan20210202.redutils.commands;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.input.InputCommand;
import me.alan20210202.redutils.commands.output.OutputCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class RedUtilsCommand extends SimpleCommand implements TabExecutor {
    private InputCommand inputCommand;
    private OutputCommand outputCommand;
    private DoneMultiSelectionCommand doneMultiSelectionCommand;
    private WandCommand wandCommand;
    private ResetTimerCommand resetTimerCommand;
    private static final List<String> SUB_COMMANDS = Arrays.asList("in", "out", "doneMultiSel", "wand", "rst", "resetTimer");

    public RedUtilsCommand(RedUtils plugin) {
        super(plugin);
        inputCommand = new InputCommand(plugin);
        outputCommand = new OutputCommand(plugin);
        doneMultiSelectionCommand = new DoneMultiSelectionCommand(plugin);
        wandCommand = new WandCommand(plugin);
        resetTimerCommand = new ResetTimerCommand(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], SUB_COMMANDS);
        else {
            String[] fwd = takeFirst(args);
            switch (args[0]) {
            case "in": return inputCommand.complete(sender, fwd, config);
            case "out": return outputCommand.complete(sender, fwd, config);
            case "doneMultiSel": return doneMultiSelectionCommand.complete(sender, fwd, config);
            case "wand": return wandCommand.complete(sender, fwd, config);
            case "rst":
            case "resetTimer": resetTimerCommand.complete(sender, fwd, config);
            }
        }
        return null;
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count > 0;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        String[] fwd = takeFirst(args);
        switch (args[0]) {
        case "in":
        case "input": inputCommand.onCommand(sender, fwd, config); break;
        case "out":
        case "output": outputCommand.onCommand(sender, fwd, config); break;
        case "doneMultiSel": doneMultiSelectionCommand.onCommand(sender, fwd, config); break;
        case "wand": wandCommand.onCommand(sender, fwd, config); break;
        case "rst":
        case "resetTimer": resetTimerCommand.onCommand(sender, fwd, config); break;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "/redutils can only be executed by a player!");
            sender.sendMessage(ChatColor.RED + "To execute from a command block or console, use /redutilsas");
            return true;
        }
        Player player = (Player)sender;
        onCommand(player, args, plugin.getPlayerConfigManager().get(player));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return null;
        Player player = (Player)sender;
        return complete(player, args, plugin.getPlayerConfigManager().get(player));
    }
}
