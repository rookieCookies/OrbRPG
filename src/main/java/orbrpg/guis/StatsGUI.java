package orbrpg.guis;

import net.kyori.adventure.text.Component;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
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
import utils.FileM;
import utils.GUI;
import utils.Item;
import utils.Misc;
import utils.i.StringM;

import java.util.ArrayList;
import java.util.List;

public class StatsGUI implements Listener {
    private final Inventory inv;
    private final ItemStack closeButton = Item.createGuiItem(Material.ARROW, "&cGo Back", "&eClick to go back!");
    private final ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
    private final ItemStack statHealth = new ItemStack(Material.RED_DYE, 1);
    private final ItemStack statDefense = new ItemStack(Material.GREEN_DYE, 1);
    private final ItemStack statTex = new ItemStack(Material.ORANGE_DYE, 1);
    private final ItemStack statSpeed = new ItemStack(Material.WHITE_DYE, 1);

    public StatsGUI() {
        inv = Bukkit.createInventory(null, 54, Component.text(Misc.coloured("&ePlayer Stats")));
        initializeItems();
    }

    public void initializeItems() {
        inv.setContents(GUI.fillBackground(inv, Item.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, "")));
        inv.setContents(GUI.createBorder(inv, Item.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "")));
        inv.setItem(49, closeButton);
    }

    public void openInventory(final Player p) {
        prepareStatHealth(p);
        prepareStatDefense(p);
        prepareStatTex(p);
        prepareStatSpeed(p);
        preparePlayerHead(p);
        inv.setItem(13, playerHead);
        inv.setItem(21, statHealth);
        inv.setItem(22, statDefense);
        inv.setItem(23, statTex);
        inv.setItem(31, statSpeed);
        p.openInventory(inv);
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
    void preparePlayerHead(Player p) {
        SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.PLAYER_HEAD);
        meta.setOwningPlayer(p);
        meta.displayName(Component.text(Misc.coloured("&a" + p.getName() + "'s Stats")));
        List<String> lore = new ArrayList<>();
        lore.add(Misc.coloured(""));
        lore.add(FileM.getMessage("scoreboard.balance") + Misc.coloured("&6" + StringM.getFormattedNumber((float) OrbRPG.getInstance().getEconomy().getBalance(p))));
        lore.add(Misc.coloured(""));
        var data = new PlayerData(p);
        lore.add(Misc.coloured(FileM.getMessage("scoreboard.level") + "&a" + data.getLevel()));
        lore.add(Misc.coloured("&7Exp: &a" + StringM.getFormattedNumber(data.getCurrentExp())));
        lore.add(Misc.coloured("&7Exp Required: &a" + StringM.getFormattedNumber(data.getMaximumExp())));
        lore.add(Misc.coloured("&7Level Progress: &a" + StringM.getFormattedNumber(data.getCurrentExp() / data.getMaximumExp() * 100) + "%"));
        lore.add(Misc.coloured(""));
        meta.setLore(lore);
        playerHead.setItemMeta(meta);
    }
    void prepareStatHealth(Player p) {
        List<String> lore = new ArrayList<>();
        var meta = statHealth.getItemMeta();
        var data = new PlayerData(p);
        meta.displayName(Component.text(Misc.coloured("&cHealth (" + StringM.getFormattedNumber(data.getCurrentHealth()) +"/"+ StringM.getFormattedNumber(data.getMaximumHealth()) + ")")));

        lore.add(Misc.coloured(""));
        lore.add(Misc.coloured("&7Health &c= &4&l0&c =&7 bad."));
        lore.add(Misc.coloured(""));

        meta.setLore(lore);
        statHealth.setItemMeta(meta);
    }
    void prepareStatDefense(Player p) {
        List<String> lore = new ArrayList<>();
        var meta = statDefense.getItemMeta();
        var data = new PlayerData(p);
        meta.displayName(Component.text(Misc.coloured("&aDefense (" + StringM.getFormattedNumber(data.getDefense()) + ")")));

        lore.add(Misc.coloured(""));
        lore.add(Misc.coloured("&7Every defense value you"));
        lore.add(Misc.coloured("&7have decreases the amount"));
        lore.add(Misc.coloured("&7of damage you take per hit by 1"));
        lore.add(Misc.coloured(""));
        meta.setLore(lore);
        statDefense.setItemMeta(meta);
    }
    void prepareStatTex(Player p) {
        List<String> lore = new ArrayList<>();
        var meta = statTex.getItemMeta();
        var data = new PlayerData(p);
        meta.displayName(Component.text(Misc.coloured("&6Tex (" + StringM.getFormattedNumber(data.getCurrentTex()) +"/"+ StringM.getFormattedNumber(data.getMaximumTex()) + ")")));

        lore.add(Misc.coloured(""));
        lore.add(Misc.coloured("&7Tex is a custom stat that is almost"));
        lore.add(Misc.coloured("&7essential for gameplay. It works like"));
        lore.add(Misc.coloured("&7an energy system."));
        lore.add(Misc.coloured(""));
        meta.setLore(lore);
        statTex.setItemMeta(meta);
    }
    void prepareStatSpeed(Player p) {
        List<String> lore = new ArrayList<>();
        var meta = statSpeed.getItemMeta();
        var data = new PlayerData(p);
        meta.displayName(Component.text(Misc.coloured("&fSpeed (" + Math.round(data.getSpeed()) +"/500" + ")")));

        lore.add(Misc.coloured(""));
        lore.add(Misc.coloured("&7Z&fo&bo&fo&bo&fo&bo&fo&bo&fo&7m!"));
        lore.add(Misc.coloured(""));
        meta.setLore(lore);
        statSpeed.setItemMeta(meta);
    }
}