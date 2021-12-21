package orbrpg.events;

import utils.Item;
import orbrpg.OrbRPG;
import utils.PlayerData;
import orbrpg.functions.*;
import orbrpg.systems.LevelingSystem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class PlayerJoinEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        new PlayerData(e.getPlayer()).setLevel(0);
        new PlayerData(e.getPlayer()).setCurrentExp(0);
        new PlayerData(e.getPlayer()).setMaximumExp(new LevelingSystem(e.getPlayer()).getEXPRequired(0));
        Item.refreshInventory(e.getPlayer());
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
                    "Debug: {0} Events > " + getClass().getName(),
                    e.getPlayer().getName()
            );
    }
}
