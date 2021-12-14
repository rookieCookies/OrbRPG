package orbrpg.commands.info;

import orbrpg.Item;
import orbrpg.Misc;
import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class InfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        boolean doReturn = false;
        if (!(sender instanceof Player) && args.length < 1) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
            doReturn = true;
        } else if (!sender.hasPermission("orbrpg.command.info")) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.no_permission"));
            doReturn = true;
        }
        if (doReturn)
            return false;
        ItemStack item;
        if (args.length < 1) item =  OrbRPG.getInstance().getItemDatabase()
                .getItemStack(Item.getIDOfItem(((Player) sender).getInventory().getItemInMainHand()));
        else {
            if (!OrbRPG.getInstance().getItemDatabase().contains(args[0])) {
                sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_item"));
                return false;
            }
            item = OrbRPG.getInstance().getItemDatabase().getItemStack(args[0]);
        }
        List<String> data = Item.getInfoFromItem(item);
        sender.sendMessage(Misc.getMessage("command_messages.info.item_info")
                .replace("%creator%", data.get(0))
                .replace("%creator_discord%", data.get(1))
        );
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.commands.info.command"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} executed the command [/info] successfully! ",
                    sender.getName()
            );
        return true;
    }
}
