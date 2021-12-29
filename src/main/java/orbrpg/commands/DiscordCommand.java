package orbrpg.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import orbrpg.OrbRPG;
import orbrpg.guis.MenuGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import utils.Misc;

import java.util.logging.Level;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
            return false;
        } else if (!sender.hasPermission("orbrpg.command.discord")) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.no_permission"));
            return false;
        }
        var bars = parse("&9------------------------------");
        var empty = parse("&9                                               ");
        var discordMessage = parse("&3 Our discord server                    ");
        sender.sendMessage(bars);
        sender.sendMessage(empty);
        sender.sendMessage(discordMessage);
        sender.sendMessage(empty);
        sender.sendMessage(bars);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.discord"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Command > [/discord]",
                    sender.getName()
            );
        return true;
    }
    TextComponent parse(String msg) {
        var i = new TextComponent(Misc.coloured(msg));
        i.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, OrbRPG.getInstance().getConfig().getString("discord_link")));
        i.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Misc.getMessage("command_messages.other.discord_hover")).create()));
        return i;
    }
}
