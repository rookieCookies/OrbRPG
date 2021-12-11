package orbrpg.events;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class EntityProjectileHitEventListener implements Listener {
    @EventHandler
    public void onProjectileHit(org.bukkit.event.entity.ProjectileHitEvent e) {
        if (e.getEntity().getType() == EntityType.ARROW)
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> e.getEntity().remove(), 1L);
    }
}
