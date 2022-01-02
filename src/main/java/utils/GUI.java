package utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI {
    GUI() { throw new IllegalStateException("Utility class"); }
    public static ItemStack[] fillBackround(Inventory inv, ItemStack backroundItem) {
        backroundItem.getItemMeta().setCustomModelData(1000);
        for (var i = 0; i < 54; i++)
            inv.setItem(i, backroundItem);
        return inv.getContents();
    }
    public static ItemStack[] createBorder(Inventory inv, ItemStack borderItem) {
        inv.setContents(createUpBorder(inv, borderItem));
        inv.setContents(createDownBorder(inv, borderItem));
        inv.setContents(createLeftBorder(inv, borderItem));
        inv.setContents(createRightBorder(inv, borderItem));
        return inv.getContents();
    }
    public static ItemStack[] createUpBorder(Inventory inv, ItemStack borderItem) {
        borderItem.getItemMeta().setCustomModelData(1000);
        for (var i = 0; i < 8; i++)
            inv.setItem(i, borderItem);
        return inv.getContents();
    }
    public static ItemStack[] createDownBorder(Inventory inv, ItemStack borderItem) {
        borderItem.getItemMeta().setCustomModelData(1000);
        for (var i = 44; i < 54; i++)
            inv.setItem(i, borderItem);
        return inv.getContents();
    }
    public static ItemStack[] createLeftBorder(Inventory inv, ItemStack borderItem) {
        borderItem.getItemMeta().setCustomModelData(1000);
        for (var i = 0; i < 54; i += 9)
            inv.setItem(i, borderItem);
        return inv.getContents();
    }
    public static ItemStack[] createRightBorder(Inventory inv, ItemStack borderItem) {
        borderItem.getItemMeta().setCustomModelData(1000);
        for (var i = 8; i < 54; i += 9)
            inv.setItem(i, borderItem);
        return inv.getContents();
    }
    public static ItemStack getCloseItem() {
        ItemStack item = Item.createGuiItem(Material.BARRIER, "&cClose", "&eClick to close!");
        item.getItemMeta().setCustomModelData(1000);
        return item;
    }
}
