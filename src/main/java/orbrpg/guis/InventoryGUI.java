package orbrpg.guis;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import utils.GUI;
import utils.Item;

import java.util.Objects;

public class InventoryGUI implements Listener {
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!(e.getClickedInventory() instanceof PlayerInventory))
            return;
        if (e.getRawSlot() != 9 && e.getRawSlot() != 10)
            return;
        if (!e.getAction().equals(InventoryAction.PICKUP_ALL) &&
                !e.getAction().equals(InventoryAction.PLACE_ALL) &&
                !e.getAction().equals(InventoryAction.PLACE_ONE) &&
                !e.getAction().equals(InventoryAction.PLACE_SOME) &&
                !e.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)) {
            e.getWhoClicked().sendMessage("Incorrect click type " + e.getAction());
            e.setCancelled(true);
            return;
        }
        if (Objects.requireNonNull(e.getCursor()).getType() == Material.AIR) {
            e.getWhoClicked().sendMessage("Air");
            if (!((GUI.getEmptyItem()).equals(e.getWhoClicked().getInventory().getItem(e.getRawSlot()))))
                Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> e.getWhoClicked().getInventory().setItem(e.getRawSlot(), GUI.getEmptyItem()), 1L);
            else e.setCancelled(true);
        } else if ("crystal".equals(Item.getTypeOfItem(e.getCursor()))) {
            e.getWhoClicked().sendMessage("Crystal");
            e.getWhoClicked().getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
            Player p = (Player) e.getWhoClicked();
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), p::updateInventory, 1L);
        } else {
            e.getWhoClicked().sendMessage("other");
            e.setCancelled(true);
        }
    }
}
