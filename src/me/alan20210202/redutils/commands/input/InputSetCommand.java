package me.alan20210202.redutils.commands.input;

import me.alan20210202.redutils.utils.ConvertUtils;
import me.alan20210202.redutils.InputConfig;
import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import me.alan20210202.redutils.commands.SimpleCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class InputSetCommand extends SimpleCommand {
    private static final Map<String, BiFunction<String, Integer, List<Boolean>>> INPUT_MODES;

    static {
        INPUT_MODES = new HashMap<>();
        INPUT_MODES.put("bin", ConvertUtils::fromBinPadLeft);
        INPUT_MODES.put("binPadRight", ConvertUtils::fromBinPadRight);
        INPUT_MODES.put("hex", ConvertUtils::fromHexPadLeft);
        INPUT_MODES.put("hexPadRight", ConvertUtils::fromHexPadRight);
        INPUT_MODES.put("dec", ConvertUtils::fromDecimal);
        INPUT_MODES.put("decFrac", ConvertUtils::fromDecimalFrac);
    }

    public InputSetCommand(RedUtils plugin) {
        super(plugin);
    }

    @Override
    public List<String> complete(Player sender, String[] args, PlayerConfig config) {
        if (args.length == 1)
            return tryCompleteWith(args[0], config.getInputConfigs().keySet());
        if (args.length == 2)
            return tryCompleteWith(args[1], INPUT_MODES.keySet());
        return emptyList();
    }

    @Override
    protected boolean argsCountCheck(int count) {
        return count == 3;
    }

    @Override
    protected void execute(Player sender, String[] args, PlayerConfig config) {
        String name = args[0];
        InputConfig inputConfig = config.getInputConfigs().get(name);
        if (inputConfig == null) {
            sender.sendMessage(ChatColor.RED + "No input config named \"" + name + "\"");
            return;
        }
        String mode = args[1], input = args[2].replace("_", "");
        if (!INPUT_MODES.containsKey(mode)) {
            sender.sendMessage(ChatColor.RED + "Unknown input mode");
            return;
        }
        try {
            List<Boolean> newStates = INPUT_MODES.get(mode).apply(input, inputConfig.getInputCount());
            inputConfig.setStates(newStates);
            sender.sendMessage(config.getInputPreLog(name) + "Set to " + input + " in mode " + mode);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Error: " + e.getMessage());
        }
    }
}
