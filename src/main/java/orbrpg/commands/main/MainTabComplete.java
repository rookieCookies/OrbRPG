package orbrpg.commands.main;

import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class MainTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!sender.hasPermission("orbrpg.command.main")) {
            return list;
        }
        if (args.length == 2)
            for (var i = 0; i < 9; i++)
                list.add(String.valueOf(i + 1));
        if (args.length > 1)
            return list;
        list.add("reload");
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.rpg.tab_complete"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) TabComplete > [/main] > " + list,
                    sender.getName()
            );
        return list;
    }
}
