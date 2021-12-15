package orbrpg;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Item {
    Item() { throw new IllegalStateException("Utility class"); }
    public static ItemStack refreshItem(ItemStack item) {
        if (item == null)
            return null;
        String itemID = getIDOfItem(item);
        if (itemID == null)
            return item;
        return OrbRPG.getInstance().getItemDatabase().getItemStack(itemID);
    }
    public static float getFloatFromItem(ItemStack item, String nameSpace) {
        if (item == null)
            return 0F;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return 0F;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        Float returnValue = container.get(new NamespacedKey(OrbRPG.getInstance(), nameSpace), PersistentDataType.FLOAT);
        if (returnValue == null)
            returnValue = 0F;
        return returnValue;
    }
    public static String getIDOfItem(ItemStack item) {
        if (item == null)
            return null;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(OrbRPG.getInstance(), "item_id"), PersistentDataType.STRING);
    }
    public static String getTypeOfItem(ItemStack item) {
        if (item == null)
            return null;
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return container.get(new NamespacedKey(OrbRPG.getInstance(), "item_type"), PersistentDataType.STRING);
    }
    public static List<String> getInfoFromItem(ItemStack item) {
        List<String> list = new ArrayList<>();
        list.add("Server");
        list.add("Server");
        if (item == null) {
            return list;
        }
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return list;
        }
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        list.set(0, container.get(new NamespacedKey(OrbRPG.getInstance(), "creator"), PersistentDataType.STRING));
        list.set(1, container.get(new NamespacedKey(OrbRPG.getInstance(), "creator_discord"), PersistentDataType.STRING));
        return list;
    }
    public static boolean isCustomItem(ItemStack item) {
        if (item == null)
            return false;
        String itemID = getIDOfItem(item);
        if (itemID == null)
            return false;
        return OrbRPG.getInstance().getItemDatabase().contains(itemID);
    }
}