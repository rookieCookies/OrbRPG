package orbrpg.events;

import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerBowEventListener implements Listener {
    @EventHandler
    public void Events(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        PlayerData data = new PlayerData(p);
        if (e.getItem() == null || e.getItem().getType() != Material.LEAD) return;
        if (!e.getAction().isLeftClick() || data.getBool("bow_cooldown")) return;
        e.setCancelled(true);
        data.setBool("bow_cooldown", true);
        Vector playerDirection = p.getLocation().getDirection();
        Arrow arrow = p.launchProjectile(Arrow.class, playerDirection);
        arrow.setVelocity(new Vector(arrow.getVelocity().getX() * 2, arrow.getVelocity().getY() * 2, arrow.getVelocity().getZ() * 2));
        arrow.setPickupStatus(Arrow.PickupStatus.DISALLOWED);
        float cooldown = Item.getFloatFromItem(e.getItem(), "bow_cooldown");
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> data.setBool("bow_cooldown", false), (long) cooldown);
        data.addExp(50);
        p.setCooldown(e.getItem().getType(), (int) cooldown);
    }
}
