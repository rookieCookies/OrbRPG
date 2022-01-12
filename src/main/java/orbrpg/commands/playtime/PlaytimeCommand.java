package orbrpg.commands.playtime;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import utils.FileM;

import java.util.logging.Level;

public class PlaytimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(FileM.getMessage("command_messages.errors.incorrect_sender"));
                return false;
            }
            sender.sendMessage(FileM.getMessage("command_messages.info.play_time.no_target")
                    .replace("%time%", ((Player) sender).getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 60 + " minutes"));
            return true;
        }
        var target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null) {
            sender.sendMessage(FileM.getMessage("command_messages.error.incorrect_argument"));
        }
        else
            sender.sendMessage(FileM.getMessage("command_messages.info.play_time.set_target")
                    .replace("%time%", ((Player) sender).getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 60 + " minutes")
                    .replace("%target%", args[0]));
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.playtime.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Command > [/playtime]",
                    sender.getName()
            );
        return true;
    }
}
