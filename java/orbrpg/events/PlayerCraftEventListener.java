package orbrpg.events;

import orbrpg.Item;
import orbrpg.Misc;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class PlayerCraftEventListener implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (!Item.isCustomItem(e.getCurrentItem())) return;
        e.getWhoClicked().sendMessage(Misc.getMessage("messages.vanilla_recipe"));
        e.setCancelled(true);
        e.getCurrentItem().setType(Material.AIR);
    }
}
