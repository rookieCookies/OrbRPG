package orbrpg.commands.info;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import orbrpg.OrbRPG;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import utils.FileM;
import utils.Item;
import utils.Misc;

import java.util.logging.Level;

public class InfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        var doReturn = false;
        if (!(sender instanceof Player) && args.length < 1) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.incorrect_sender"));
            doReturn = true;
        } else if (!sender.hasPermission("orbrpg.command.info")) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.no_permission"));
            doReturn = true;
        }
        if (doReturn)
            return false;
        ItemStack item;
        if (args.length < 1)
            item =  OrbRPG.getInstance().getItemDatabase()
                    .getItemStack(
                            Item.getIDOfItem(((Player) sender)
                                    .getInventory()
                                    .getItemInMainHand()));
        else {
            if (!OrbRPG.getInstance().getItemDatabase().contains(args[0])) {
                sender.sendMessage(FileM.getMessage("command_messages.errors.incorrect_item"));
                return false;
            }
            item = OrbRPG.getInstance().getItemDatabase().getItemStack(args[0]);
        }

        var data = Item.getDataOfItem(item);
        var rarity = data.get(new NamespacedKey(OrbRPG.getInstance(), "rarity"), PersistentDataType.STRING);
        sender.sendMessage(Misc.coloured(FileM.getMessage("rarities." + rarity + ".color_id")+ "╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍"));
        sender.sendMessage("");
        sender.sendMessage(" " + Misc.coloured(item.getItemMeta().getDisplayName() + ":"));
        sender.sendMessage(Misc.coloured(" &l↳ " + "&a&lCreator" + "&7 (Hover)"));
        sender.sendMessage(Misc.coloured(" &l↳ " + "&a&lStats" + "&7 (Hover)"));
        var i = new TextComponent(Misc.coloured(" &l↳ " + "&b&lFor creators!" + "&7 (Hover)"));
        i.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Misc.coloured("&7 Custom Model Data: &a" + item.getItemMeta().getCustomModelData())).create()));
        sender.sendMessage(i);
        sender.sendMessage("");
        sender.sendMessage(Misc.coloured(FileM.getMessage("rarities." + rarity + ".color_id")+ "╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍╍"));
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.info.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Command > [/info] > " + Item.getIDOfItem(item),
                    sender.getName()
            );
        return true;
    }
}
