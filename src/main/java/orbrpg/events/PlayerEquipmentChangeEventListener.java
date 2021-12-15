package orbrpg.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.functions.PlayerRefreshUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.logging.Level;

public class PlayerEquipmentChangeEventListener implements Listener {
    @EventHandler
    public void onHeldItemChange(PlayerItemHeldEvent e) {
        e.getPlayer().getInventory().setItem(e.getNewSlot(),
                Item.refreshItem(e.getPlayer().getInventory().getItem(e.getNewSlot())));
        new PlayerRefreshUI(e.getPlayer());
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.change_held_item"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} changed their held item!",
                    e.getPlayer().getName()
            );
    }
    @EventHandler
    public void onEvent(PlayerArmorChangeEvent e) {
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> new PlayerRefreshUI(e.getPlayer()), 1);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.change_armor"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} changed their armor!",
                    e.getPlayer().getName()
            );
    }
}
