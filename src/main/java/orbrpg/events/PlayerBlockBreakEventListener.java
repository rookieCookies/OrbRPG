package orbrpg.events;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockBreakEventListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE)
            return;
        var item = p.getItemInUse();
        if (item == null)
            return;
        var itemMeta = item.getItemMeta();
        if (itemMeta == null)
            return;
        var itemData = itemMeta.getPersistentDataContainer();

    }
}
