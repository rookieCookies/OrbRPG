package orbrpg.commands;

import orbrpg.Misc;
import orbrpg.OrbRPG;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GetItem implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_sender"));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.not_enough_arguments"));
            return false;
        }
        ItemStack item = OrbRPG.getInstance().getItemDatabase().getItemStack(args[0]);
        if (item == null) {
            sender.sendMessage(Misc.getMessage("command_messages.errors.incorrect_item"));
            return false;
        }
        Player player = (Player) sender;
        player.getInventory().addItem(item);
        sender.sendMessage(Misc.getMessage("command_messages.success.item_received"));
        return false;
    }
}
