package orbrpg.guis;

import net.kyori.adventure.text.Component;
import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import utils.GUI;
import utils.Item;
import utils.Misc;

public class UpgradesGUI implements Listener {
    private final Inventory inv;
    private final ItemStack closeButton = Item.createGuiItem(Material.ARROW, "&cGo Back", "&eClick to go back!");
    private final ItemStack placeholderButton = Item.createGuiItem(Material.RED_STAINED_GLASS_PANE, "&aPlaceholder", "&4&lCOMING SOON!");


    public UpgradesGUI() {
        inv = Bukkit.createInventory(null, 54, Component.text(Misc.coloured("&eUpgrades")));
        initializeItems();
    }

    public void initializeItems() {
        inv.setContents(GUI.fillBackround(inv, Item.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "")));
        inv.setContents(GUI.createBorder(inv, Item.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "")));
        inv.setItem(49, closeButton);
        inv.setItem(22, placeholderButton);
    }

    public void openInventory(final Player ent) {
        ent.openInventory(inv);
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(this, OrbRPG.getInstance());
    }
    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != inv)
            return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null ||
                clickedItem.getType().isAir())
            return;
        final Player p = (Player) e.getWhoClicked();
        if (closeButton.equals(clickedItem))
            new MenuGUI().openInventory(p);
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}