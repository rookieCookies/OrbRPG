package orbrpg.events;

import orbrpg.Item;
import orbrpg.Misc;
import orbrpg.functions.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerLoginEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        for (int i = 0; i < 35; i++) {
            if (inv.getItem(i) == null) continue;
            e.getPlayer().getInventory().setItem(i, Item.refreshItem(inv.getItem(i)));
        }
        new Scoreboard(e.getPlayer());
    }
}
