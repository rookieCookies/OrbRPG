package orbrpg.commands.info;

import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class InfoTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (!sender.hasPermission("orbrpg.command.getitem")) {
            return list;
        }
        if (args.length == 2)
            for (int i = 0; i < 9; i++)
                list.add(String.valueOf(i + 1));
        if (args.length > 1)
            return list;
        ConfigurationSection itemDataBase = OrbRPG.getInstance().getItemDatabase();
        ConfigurationSection itemsFile = OrbRPG.getInstance().getItemsFile();
        Map<String, Object> sec = itemsFile.getValues(false);
        for (Object a : sec.values()) {
            String path = a.toString();
            path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
            if (!itemDataBase.contains(path) ||
                    (!"".equals(args[0]) && !path.startsWith(args[0])))
                continue;
            list.add(path);
        }
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.info.tab_complete"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} tab completed the command [/info] successfully! The returned value was " + list,
                    sender.getName()
            );
        return list;
    }
}
