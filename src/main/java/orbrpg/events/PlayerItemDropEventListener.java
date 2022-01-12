package orbrpg.events;

import orbrpg.OrbRPG;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import utils.FileM;
import utils.Item;

import java.util.logging.Level;

public class PlayerItemDropEventListener implements Listener {
    @EventHandler
    public void onPlayerDropEvent(PlayerDropItemEvent e) {
        if (!"quest_item".equals(Item.getTypeOfItem(e.getItemDrop().getItemStack())))
            return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(FileM.getMessage("messages.quest_item"));
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.drop_item"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} Events > " + getClass().getName(),
                    e.getPlayer().getName()
            );
    }
}
