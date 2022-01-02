package orbrpg.items;

import orbrpg.guis.MenuGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import utils.Item;

public class MenuItem implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (!e.getAction().isLeftClick() && !e.getAction().isRightClick())
            return;
        if (!"menu".equals(Item.getIDOfItem(e.getItem())))
            return;
        e.setCancelled(true);
        new MenuGUI().openInventory(e.getPlayer());
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (!"menu".equals(Item.getIDOfItem(e.getItemDrop().getItemStack())))
            return;
        e.setCancelled(true);
        new MenuGUI().openInventory(e.getPlayer());
    }
    @EventHandler
    public void onHandSwitch(PlayerSwapHandItemsEvent e) {
        if (!"menu".equals(Item.getIDOfItem(e.getOffHandItem())))
            return;
        e.setCancelled(true);
        new MenuGUI().openInventory(e.getPlayer());
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!"menu".equals(Item.getIDOfItem(e.getCurrentItem())))
            return;
        e.setCancelled(true);
    }
}
