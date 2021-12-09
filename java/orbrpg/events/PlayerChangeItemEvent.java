package orbrpg.events;

import orbrpg.Item;
import orbrpg.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerChangeItemEvent implements Listener {
    @EventHandler
    public void onHeldItemChange(PlayerItemHeldEvent e) {
        e.getPlayer().getInventory().setItem(e.getNewSlot(), Item.refreshItem(e.getPlayer().getInventory().getItem(e.getNewSlot())));
    }
}
