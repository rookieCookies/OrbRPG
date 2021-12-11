package orbrpg.commands.getitem;

import orbrpg.Misc;
import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
            return false;
        }
        if (!sender.hasPermission("orbrpg.command.getitem")) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.no_permission"));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.not_enough_arguments"));
            return false;
        }
        int loop = 1;
        if (args.length == 2 && !args[1].equals("0")) loop = Integer.parseInt(args[1]);
        if (args[0].equals("*")) {
            ConfigurationSection itemDataBase = OrbRPG.getInstance().getItemDatabase();
            ConfigurationSection itemsFile = OrbRPG.getInstance().getItemsFile();
            Map<String, Object> sec = itemsFile.getValues(false);
            Player player = (Player) sender;
            for (int i = 0; i < loop; i++) {
                for (Object a : sec.values()) {
                    String path = a.toString();
                    path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
                    ItemStack item = itemDataBase.getItemStack(path);
                    if (item == null) continue;
                    player.getInventory().addItem(item);
                    sender.sendMessage(Misc.getMessage("command_messages.success.item_received"));
                }
            }
            return true;
        }
        Player player = (Player) sender;
        for (int i = 0; i < loop; i++) {
            ItemStack item = OrbRPG.getInstance().getItemDatabase().getItemStack(args[0]);
            if (item == null) {
                sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_item"));
                return false;
            }
            player.getInventory().addItem(item);
            sender.sendMessage(Misc.getMessage("command_messages.success.item_received"));
        }
        return true;
    }
}
