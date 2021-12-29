package orbrpg.commands.setwarp;

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

public class SetWarpTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                @NotNull String alias, @NotNull String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length > 1)
            return list;
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        Map<String, Object> sec = config.getConfigurationSection("warps").getValues(false);
        for (Object a : sec.values()) {
            var path = a.toString();
            path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
            path = path.substring(path.indexOf("warps.") + 6);
            list.add(path);
        }
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.set_warp.tab_complete"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) TabComplete > [/setwarp] > " + list,
                    sender.getName()
            );
        return list;
    }
}
