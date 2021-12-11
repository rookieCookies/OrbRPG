package orbrpg.events;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.functions.PlayerRefreshUI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerEquipmentChangeEventListener implements Listener {
    @EventHandler
    public void onHeldItemChange(PlayerItemHeldEvent e) {
        e.getPlayer().getInventory().setItem(e.getNewSlot(), Item.refreshItem(e.getPlayer().getInventory().getItem(e.getNewSlot())));
    }
    @EventHandler
    public void onArmorChange(PlayerArmorChangeEvent e) {
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> new PlayerRefreshUI(e.getPlayer()), 1);
    }
}
