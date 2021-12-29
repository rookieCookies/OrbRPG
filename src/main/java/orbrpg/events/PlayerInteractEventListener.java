package orbrpg.events;

import orbrpg.OrbRPG;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Level;

public class PlayerInteractEventListener implements Listener {
    @EventHandler
    public void onBlockInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null)
            return;
        if (e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.FARMLAND)
            e.setCancelled(true);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.interact"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} Events > " + getClass().getName(),
                    e.getPlayer().getName()
            );
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        e.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() == GameMode.CREATIVE)
            return;
        e.setCancelled(true);
    }
}
