package me.alan20210202.redutils.commands;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SimpleCommand {
    protected String alt(Iterable<String> alternatives) { return " [" + String.join("/", alternatives) + "] "; }

    protected static String[] takeFirst(String[] args) {
        String[] ret = new String[args.length - 1];
        System.arraycopy(args, 1, ret, 0, ret.length);
        return ret;
    }

    protected static List<String> tryCompleteWith(String arg, Iterable<String> cand) {
        List<String> ret = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, cand, ret);
        Collections.sort(ret);
        return ret;
    }

    protected RedUtils plugin;

    public SimpleCommand(RedUtils plugin) {
        this.plugin = plugin;
    }

    public void onCommand(Player sender, String[] args, PlayerConfig config) {
        if (!argsCountCheck(args.length))
            sender.sendMessage(ChatColor.RED + "Wrong number of arguments to the command!");
        else
            execute(sender, args, config);
    }

    public abstract List<String> complete(Player sender, String[] args, PlayerConfig config);

    protected abstract boolean argsCountCheck(int count);

    protected abstract void execute(Player sender, String[] args, PlayerConfig config);
}
