package orbrpg.events;

import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import orbrpg.functions.*;
import orbrpg.systems.LevelingSystem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class PlayerJoinEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        for (int i = 0; i < 35; i++) {
            if (inv.getItem(i) == null)
                continue;
            e.getPlayer().getInventory().setItem(i, Item.refreshItem(inv.getItem(i)));
        }
        new PlayerData(e.getPlayer()).setLevel(0);
        new PlayerData(e.getPlayer()).setCurrentExp(0);
        new PlayerData(e.getPlayer()).setMaximumExp(new LevelingSystem(e.getPlayer()).getEXPRequired(0));
        new IncreaseStats(e.getPlayer()).max();
        new Scoreboard(e.getPlayer());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Bukkit.getPlayer(e.getPlayer().getName()) == null)
                    cancel();
                new IncreaseStats(e.getPlayer());
                new PlayerRefreshUI(e.getPlayer());
            }
        }.runTaskTimer(OrbRPG.getInstance(), 1, 20); // Start loop
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.join"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} joined the server!",
                    e.getPlayer().getName()
            );
    }
}
