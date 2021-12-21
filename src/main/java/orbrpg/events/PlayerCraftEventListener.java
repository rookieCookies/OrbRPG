package orbrpg.events;

import utils.Item;
import utils.Misc;
import orbrpg.OrbRPG;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.logging.Level;

public class PlayerCraftEventListener implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (!Item.isCustomItem(e.getCurrentItem()))
            return;
        e.getWhoClicked().sendMessage(Misc.getMessage("messages.vanilla_recipe"));
        e.setCancelled(true);
        e.getCurrentItem().setType(Material.AIR);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.craft_item"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} Events > " + getClass().getName(),
                    e.getWhoClicked().getName()
            );
    }
}
