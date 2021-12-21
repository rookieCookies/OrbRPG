package orbrpg.commands.playtime;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class PlayTimeTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length > 1)
            return list;
        for (Player i : Bukkit.getOnlinePlayers())
            list.add(i.getName());
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.warp.tab_complete"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) TabComplete > [/playtime] > " + list,
                    sender.getName()
            );
        return list;
    }
}
