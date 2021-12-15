package orbrpg.events;

import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.logging.Level;

public class PlayerBowEventListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getItem() == null || e.getItem().getType() != Material.LEAD) {
            return;
        }
        Player p = e.getPlayer();
        PlayerData data = new PlayerData(p);
        if (!e.getAction().isLeftClick() || data.isBowCooldownTrue()) {
            return;
        }
        e.setCancelled(true);
        data.setBowCooldownTrue();
        Vector playerDirection = p.getLocation().getDirection();
        Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        arrow.setVelocity(new Vector(
                arrow.getVelocity().getX() * 2,
                arrow.getVelocity().getY() * 2,
                arrow.getVelocity().getZ() * 2
        ));
        arrow.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);
        float cooldown = Item.getFloatFromItem(e.getItem(), "bow_cooldown");
        Bukkit.getScheduler().runTaskLater(
                OrbRPG.getInstance(),
                data::setBowCooldownFalse,
                (long) cooldown);
        data.addExp(50);
        p.setCooldown(e.getItem().getType(), (int) cooldown);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.shoot_bow"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Events > " + getClass().getName() + " > " + cooldown,
                    p.getName()
            );
    }
}
