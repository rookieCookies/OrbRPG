package orbrpg.commands.warp;

import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import utils.FileM;
import utils.PlayerM;

import java.util.logging.Level;

public class WarpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        var doReturn = false;
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        if (!(sender instanceof Player)) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.incorrect_sender"));
            doReturn = true;
        } else if (args.length < 1) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.not_enough_arguments"));
            doReturn = true;
        } else if (!sender.hasPermission("orbrpg.warp." + args[0]) &&
                !sender.hasPermission("orbrpg.warp.*")) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.no_permission"));
            doReturn = true;
        } else if (config.get("warps." + args[0] + ".w") == null)
            doReturn = true;
        if (doReturn)
            return false;
        var player = (Player) sender;
        PlayerM.warpPlayer(player, args[0]);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.warp.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Command > [/warp] > " + args[0],
                    sender.getName()
            );
        return true;
    }
}
