package me.alan20210202.redutils.commands;

import me.alan20210202.redutils.PlayerConfig;
import me.alan20210202.redutils.RedUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class ResetTimerCommand extends SimpleCommand {
    public ResetTimerCommand(RedUtils plugin) {
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
        config.resetTimer();
        sender.sendMessage(ChatColor.RED + "Timer reset");
    }
}
