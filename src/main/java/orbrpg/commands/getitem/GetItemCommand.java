package orbrpg.commands.getitem;

import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import utils.FileM;
import utils.Item;

import javax.annotation.CheckForNull;
import java.util.Map;
import java.util.logging.Level;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        var doReturn = false;
        if (!(sender instanceof Player)) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.incorrect_sender"));
            doReturn = true;
        } else if (!sender.hasPermission("orbrpg.command.getitem")) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.no_permission"));
            doReturn = true;
        } else if (args.length < 1) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.not_enough_arguments"));
            doReturn = true;
        } else if (!OrbRPG.getInstance().getItemDatabase().contains(args[0]) &&
                !"*".equals(args[0])) {
            sender.sendMessage(FileM.getMessage("command_messages.errors.incorrect_item"));
            doReturn = true;
        }
        if (doReturn)
            return false;
        var loop = 1;
        if (args.length == 2 && !"0".equals(args[1]))
            loop = Integer.parseInt(args[1]);
        var player = (Player) sender;
        if ("*".equals(args[0])) {
            ConfigurationSection itemsFile = OrbRPG.getInstance().getItemsFile();
            Map<String, Object> sec = itemsFile.getValues(false);
            for (Object a : sec.values()) {
                var path = a.toString();
                path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
                @CheckForNull
                ItemStack item = Item.getItem(path);
                assert item != null;
                item.setAmount(loop);
                player.getInventory().addItem(item);
                sender.sendMessage(FileM.getMessage("command_messages.success.item_received"));
            }
            return true;
        }
        @CheckForNull
        var item = OrbRPG.getInstance().getItemDatabase().getItemStack(args[0]);
        assert item != null;
        item.setAmount(loop);
        player.getInventory().addItem(item);
        sender.sendMessage(FileM.getMessage("command_messages.success.item_received"));
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.getitem.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Command > [/getitem] > " + item.displayName(),
                    sender.getName()
            );
        return true;
    }
}
