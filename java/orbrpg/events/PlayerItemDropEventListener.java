package orbrpg.events;

import orbrpg.Item;
import orbrpg.Misc;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerItemDropEventListener implements Listener {
    @EventHandler
    public void Event(PlayerDropItemEvent e) {
        if (!Item.getTypeOfItem(e.getItemDrop().getItemStack()).equals("quest_item")) return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(Misc.getMessage("messages.quest_item"));
    }
}
