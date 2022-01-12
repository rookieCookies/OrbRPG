package orbrpg.events;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import orbrpg.functions.IncreaseStats;
import orbrpg.functions.PlayerRefreshUI;
import orbrpg.functions.Scoreboard;
import orbrpg.systems.LevelingSystem;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import utils.Item;

import java.util.logging.Level;

public class PlayerJoinEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        var data = new PlayerData(e.getPlayer());
        data.setLevel(0);
        data.setCurrentExp(0);
        data.setMaximumExp(new LevelingSystem(e.getPlayer()).getEXPRequired(0));
        data.resetCooldown();
        data.setRespawnableFalse();
        e.getPlayer().getInventory().setHeldItemSlot(8);
        e.getPlayer().getInventory().setItemInMainHand(Item.getItem("menu"));
        e.getPlayer().getInventory().setHeldItemSlot(0);
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
