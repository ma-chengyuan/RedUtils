package me.alan20210202.redutils.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedUtilsAsCommand implements TabExecutor {
    private final RedUtilsCommand redUtilsCommand;

    public RedUtilsAsCommand(RedUtilsCommand redUtilsCommand) {
        this.redUtilsCommand = redUtilsCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Too few arguments!");
            return true;
        }
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Can't find player with given name!");
        } else {
            String[] fwd = SimpleCommand.takeFirst(args);
            redUtilsCommand.onCommand(player, fwd, redUtilsCommand.plugin.getPlayerConfigManager().get(player));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return SimpleCommand.tryCompleteWith(args[0], Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()));
        else {
            Player player = Bukkit.getPlayer(args[0]);
            if (player == null) return new ArrayList<>();
            String[] fwd = SimpleCommand.takeFirst(args);
            return redUtilsCommand.complete(player, fwd, redUtilsCommand.plugin.getPlayerConfigManager().get(player));
        }
    }
}
