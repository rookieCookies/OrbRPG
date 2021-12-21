package orbrpg.commands.setwarp;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import utils.Misc;

import java.util.logging.Level;

public class SetWarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        var doReturn = false;
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        if (!(sender instanceof Player)) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
            doReturn = true;
        } else if (args.length < 1) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.not_enough_arguments"));
            doReturn = true;
        } else if (!sender.hasPermission("orbrpg.commands.setwarp")) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.no_permission"));
            doReturn = true;
        }
        if (doReturn)
            return false;
        var loc = ((Player) sender).getLocation();
        config.set("warps." + args[0] + ".x", loc.getX());
        config.set("warps." + args[0] + ".y", loc.getY());
        config.set("warps." + args[0] + ".z", loc.getZ());
        config.set("warps." + args[0] + ".w", loc.getWorld().getName());
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.warp.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Command > [/setwarp] > " + args[0] + loc,
                    sender.getName()
            );
        return true;
    }
}