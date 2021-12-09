package orbrpg;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Item {
    public static ItemStack refreshItem(ItemStack item) {
        if (item == null) return item;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return item;
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();
        String itemID = data.get(new NamespacedKey(OrbRPG.getInstance(), "item_id"), PersistentDataType.STRING);
        if (itemID == null) return item;
        item = OrbRPG.getInstance().getItemDatabase().getItemStack(itemID);
        return item;
    }
    public static float getFloatFromItem(ItemStack item, String nameSpace) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        Float returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.FLOAT);
        if (returnValue == null) returnValue = 0f;
        return returnValue;
    }
    public static String getTypeOfItem(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        String returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), "item_type"), PersistentDataType.STRING);
        if (returnValue == null) returnValue = "tool";
        return returnValue;
    }
}
