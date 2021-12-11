package orbrpg.events;

import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.functions.*;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        for (int i = 0; i < 35; i++) {
            if (inv.getItem(i) == null) continue;
            e.getPlayer().getInventory().setItem(i, Item.refreshItem(inv.getItem(i)));
        }
        new IncreaseStats(e.getPlayer()).max();
        new Scoreboard(e.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getPlayer(e.getPlayer().getName()) == null) cancel();
                new IncreaseStats(e.getPlayer());
                new PlayerRefreshUI(e.getPlayer());
            }
        }.runTaskTimer(OrbRPG.getInstance(), 1, 20); // Start loop
    }
}
