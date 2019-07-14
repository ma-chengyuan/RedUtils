package me.alan20210202.redutils.commands.input;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class InputCommand extends SimpleCommand {
    private InputCreateCommand inputCreateCommand;
    private InputDeleteCommand inputDeleteCommand;
    private InputListCommand inputListCommand;
    private InputToggleCommand inputToggleCommand;
    private InputSetCommand inputSetCommand;
    private InputZerosCommand inputZerosCommand;
    private InputOnesCommand inputOnesCommand;
    private static final List<String> SUB_COMMANDS = Arrays.asList(
            "new", "del", "list", "toggle", "set", "0s", "1s"
    );

    public InputCommand(RedUtils plugin) {
        super(plugin);
        inputCreateCommand = new InputCreateCommand(plugin);
        inputDeleteCommand = new InputDeleteCommand(plugin);
        inputListCommand = new InputListCommand(plugin);
        inputToggleCommand = new InputToggleCommand(plugin);
        inputSetCommand = new InputSetCommand(plugin);
        inputZerosCommand = new InputZerosCommand(plugin);
        inputOnesCommand = new InputOnesCommand(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], SUB_COMMANDS);
        else {
            String[] fwd = takeFirst(args);
            switch (args[0]) {
            case "new": return inputCreateCommand.complete(sender, fwd, config);
            case "del": return inputDeleteCommand.complete(sender, fwd, config);
            case "list": return inputListCommand.complete(sender, fwd, config);
            case "toggle": return inputToggleCommand.complete(sender, fwd, config);
            case "set": return inputSetCommand.complete(sender, fwd, config);
            case "0s": return inputZerosCommand.complete(sender, fwd, config);
            case "1s": return inputOnesCommand.complete(sender, fwd, config);
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
        case "new": inputCreateCommand.onCommand(sender, fwd, config); break;
        case "del": inputDeleteCommand.onCommand(sender, fwd, config); break;
        case "list": inputListCommand.onCommand(sender, fwd, config); break;
        case "toggle": inputToggleCommand.onCommand(sender, fwd, config); break;
        case "set": inputSetCommand.onCommand(sender, fwd, config); break;
        case "0s": inputZerosCommand.onCommand(sender, fwd, config); break;
        case "1s": inputOnesCommand.onCommand(sender, fwd, config); break;
        }
    }
}
