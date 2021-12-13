package orbrpg.commands.warp;

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

public class WarpTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length > 1)
            return list;
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        Map<String, Object> sec = config.getConfigurationSection("warps").getValues(false);
        for (Object a : sec.values()) {
            String path = a.toString();
            path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
            path = path.substring(path.indexOf("warps." + 6));
            if ((
                    !sender.hasPermission("orbrpg.warp." + path) &&
                            !sender.hasPermission("orbrpg.warp.*")) ||
                    (!"".equals(args[0]) &&
                            !path.startsWith(args[0]))
            )
                continue;
            list.add(path);
        }
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.warp.tab_complete"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} tab completed the command [/warp] successfully! The returned value was " + list,
                    sender.getName()
            );
        return list;
    }
}
