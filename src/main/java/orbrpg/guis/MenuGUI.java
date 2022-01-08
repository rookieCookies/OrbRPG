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
import org.bukkit.inventory.meta.SkullMeta;
import utils.GUI;
import utils.Item;
import utils.Misc;

import java.util.ArrayList;
import java.util.List;

public class MenuGUI implements Listener {
    private final Inventory inv;
    private final ItemStack closeButton = GUI.getCloseItem();
    private final ItemStack enderChestButton = Item.createGuiItem(Material.ENDER_CHEST, "&aEnder Chest", "&eClick to view!");
    private final ItemStack craftingTableButton = Item.createGuiItem(Material.CRAFTING_TABLE, "&aCrafting Table", "&eClick to view!");
    private final ItemStack upgradesButton = Item.createGuiItem(Material.DEAD_BUSH, "&aUpgrades", "&eClick to view!");
    private final ItemStack statsButton = Item.createGuiItem(Material.PLAYER_HEAD, "&aPlayer Stats", "&eClick to view!");


    public MenuGUI() {
        inv = Bukkit.createInventory(null, 54, Component.text(Misc.coloured("&eMenu")));
        initializeItems();
    }

    public void initializeItems() {
        inv.setContents(GUI.fillBackround(inv, Item.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "")));
        inv.setContents(GUI.createBorder(inv, Item.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "")));
        inv.setItem(49, closeButton);
        inv.setItem(23, enderChestButton);
        inv.setItem(21, upgradesButton);
        inv.setItem(13, statsButton);
        inv.setItem(22, craftingTableButton);
    }

    public void openInventory(final Player ent) {
        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setOwningPlayer(ent);
        meta.displayName(Component.text(Misc.coloured("&a" + ent.getName() + "'s Stats")));
        List<String> lore = new ArrayList<>();
        lore.add(Misc.coloured("&eClick to view!"));
        meta.setLore(lore);
        statsButton.setItemMeta(meta);
        inv.setItem(13, statsButton);
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
            p.closeInventory();
        else if (statsButton.equals(clickedItem))
            new StatsGUI().openInventory(p);
        else if (upgradesButton.equals(clickedItem))
            new UpgradesGUI().openInventory(p);
        else if (enderChestButton.equals(clickedItem))
            p.openInventory(p.getEnderChest());
        else if (craftingTableButton.equals(clickedItem))
            p.openWorkbench(null, true);
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }
}