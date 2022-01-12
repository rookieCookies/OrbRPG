package orbrpg.commands;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import utils.FileM;
import utils.PlayerM;

public class RespawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.incorrect_sender"));
            return false;
        } else if (!sender.hasPermission("orbrpg.command.respawn")) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.no_permission"));
            return false;
        }
        var data = new PlayerData((Player) sender);
        if (!data.canRespawn()) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.cant_respawn"));
            return false;
        }
        if (data.getDarkQuartz() < FileM.getInt("combat.respawn_price")) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.not_enough_dark_quartz").replace("%respawn_message%",
                    String.valueOf(OrbRPG.getInstance().getConfig().getInt("respawn_price", 5))));
            return false;
        }
        data.addDarkQuartz(-FileM.getInt("combat.respawn_price"));
        PlayerM.teleportPlayer((Player) sender, data.getDeathLocation(), false);
        return true;
    }
}
