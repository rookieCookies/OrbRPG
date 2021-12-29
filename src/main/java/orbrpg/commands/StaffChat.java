package orbrpg.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import utils.Misc;

import java.util.Arrays;

public class StaffChat implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1)
            return false;
        for (Player i : Bukkit.getOnlinePlayers())
            if (i.hasPermission("orbrpg.admin.staffchat"))
                i.sendMessage(Misc.coloured(String.format("&9&l Staff Chat: &r%s&f: " + Arrays.toString(args), i.getName())));
        return true;
    }
}
