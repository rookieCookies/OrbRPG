package orbrpg.commands.main;

import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import utils.Misc;

import java.util.logging.Level;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        boolean doReturn = false;
        if (!(sender instanceof Player) && args.length < 1) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
            doReturn = true;
        } else if (!sender.hasPermission("orbrpg.command.main")) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.no_permission"));
            doReturn = true;
        }
        if (doReturn)
            return false;
        switch (args[0]) {
            case "reload":
                OrbRPG.getInstance().reloadConfig();
                sender.sendMessage(Misc.coloured("&aReload complete!"));
        }
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.rpg.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Command > [/rpg]",
                    sender.getName()
            );
        return true;
    }
}
