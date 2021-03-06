package orbrpg.events;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.logging.Level;

public class EntityProjectileHitEventListener implements Listener {
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().getType() == EntityType.ARROW)
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> e.getEntity().remove(), 1L);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.projectile_hit"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: Events > " + getClass().getName()
            );
    }
}
