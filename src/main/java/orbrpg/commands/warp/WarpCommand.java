package orbrpg.commands.warp;

import orbrpg.Misc;
import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        boolean doReturn = false;
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        if (!(sender instanceof Player)) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
            doReturn = true;
        } else if (args.length < 1) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.not_enough_arguments"));
            doReturn = true;
        } else if (!sender.hasPermission("orbrpg.command.warp." + args[0])) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.no_permission"));
            doReturn = true;
        } else if (config.get("warps." + args[0] + ".w") == null)
            doReturn = true;
        else System.out.println(0);
        if (doReturn)
            return false;
        Player player = (Player) sender;
        Location l = new Location(
                Bukkit.getWorld(config.getString("warps." + args[0] + ".w", "world")),
                config.getDouble("warps." + args[0] + ".x"),
                config.getDouble("warps." + args[0] + ".y"),
                config.getDouble("warps." + args[0] + ".z")
        );
        player.teleportAsync(l);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.warp.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} executed the command [/warp] successfully! And teleported to following warp: "
                            + args[0],
                    sender.getName()
            );
        return true;
    }
}
