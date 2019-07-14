package me.alan20210202.redutils.commands.output;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class OutputCommand extends SimpleCommand {
    private final OutputCreateCommand outputCreateCommand;
    private final OutputDeleteCommand outputDeleteCommand;
    private final OutputListCommand outputListCommand;
    private final OutputSeeCommand outputSeeCommand;
    private final OutputFormatCommand outputFormatCommand;
    private final OutputLogCommand outputLogCommand;
    private final OutputSeparateCommand outputSeparateCommand;
    private static final List<String> SUB_COMMANDS = Arrays.asList("new", "del", "list", "see", "fmt", "log", "sep");


    public OutputCommand(RedUtils plugin) {
        super(plugin);
        outputCreateCommand = new OutputCreateCommand(plugin);
        outputDeleteCommand = new OutputDeleteCommand(plugin);
        outputListCommand = new OutputListCommand(plugin);
        outputSeeCommand = new OutputSeeCommand(plugin);
        outputFormatCommand = new OutputFormatCommand(plugin);
        outputLogCommand = new OutputLogCommand(plugin);
        outputSeparateCommand = new OutputSeparateCommand(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], SUB_COMMANDS);
        else {
            String[] fwd = takeFirst(args);
            switch (args[0]) {
            case "new": return outputCreateCommand.complete(sender, fwd, config);
            case "del": return outputDeleteCommand.complete(sender, fwd, config);
            case "list": return outputListCommand.complete(sender, fwd, config);
            case "see": return outputSeeCommand.complete(sender, fwd, config);
            case "fmt": return outputFormatCommand.complete(sender, fwd, config);
            case "log": return outputLogCommand.complete(sender, fwd, config);
            case "sep": return outputSeparateCommand.complete(sender, fwd, config);
            }
        }
        return emptyList();
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count > 0;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        String[] fwd = takeFirst(args);
        switch (args[0]) {
        case "new": outputCreateCommand.onCommand(sender, fwd, config); break;
        case "del": outputDeleteCommand.onCommand(sender, fwd, config); break;
        case "list": outputListCommand.onCommand(sender, fwd, config); break;
        case "see": outputSeeCommand.onCommand(sender, fwd, config); break;
        case "fmt": outputFormatCommand.onCommand(sender, fwd, config); break;
        case "log": outputLogCommand.onCommand(sender, fwd, config); break;
        case "sep": outputSeparateCommand.onCommand(sender, fwd, config); break;
        }
    }
}
