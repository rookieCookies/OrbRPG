package orbrpg.commands.playtime;

import utils.Misc;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaytimeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
                return false;
            }
            sender.sendMessage(Misc.getMessage("command_messages.info.play_time.no_target")
                    .replace("%time%", ((Player) sender).getStatistic(Statistic.PLAY_ONE_MINUTE) + ""));
            return true;
        }
        var target = Bukkit.getOfflinePlayerIfCached(args[0]);
        if (target == null) {
            sender.sendMessage(Misc.getMessage("command_messages.error.incorrect_argument"));
            return false;
        }
        sender.sendMessage(Misc.getMessage("command_messages.info.play_time.set_target")
                .replace("%time%", target.getStatistic(Statistic.PLAY_ONE_MINUTE) + "")
                .replace("%target%", args[0]));
        return true;
    }
}
