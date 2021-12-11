package orbrpg.events;

import orbrpg.Item;
import orbrpg.Misc;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import orbrpg.functions.PlayerRefreshUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerAttackEventListener implements Listener {
    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getDamager();
        if (e.getDamager() instanceof Arrow) p = (Player) ((Arrow) e.getDamager()).getShooter();
        PlayerData data = new PlayerData(p);
        if (data.getBool("attack_cooldown")) return;
        assert p != null;
        ItemStack tool = p.getInventory().getItemInMainHand();
        String itemType = Item.getTypeOfItem(tool);
        float finalDamage = data.getFloat("damage");
        EntityDamageEvent.DamageCause damageCause = e.getCause();
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            finalDamage /= 4;
        }
        if (!(damageCause == EntityDamageEvent.DamageCause.PROJECTILE) && itemType != null && itemType.equals("bow")) {
            finalDamage /= 100;
        }
        Location victimLocation = e.getEntity().getLocation();
        Location loc = new Location(e.getEntity().getWorld(), 0, 0, 0);
        loc.setX(victimLocation.getX() + (Math.random() - Math.random()));
        loc.setY(victimLocation.getY() + (Math.random() - Math.random()) / 1.2);
        loc.setZ(victimLocation.getZ() + (Math.random() - Math.random()));
        ArmorStand armorstand = (ArmorStand) e.getEntity().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        armorstand.setGravity(false);
        armorstand.setCustomNameVisible(true);
        armorstand.setInvulnerable(true);
        armorstand.setMarker(true);
        armorstand.setVisible(false);
        String formattedDamage = Misc.formatNumber(finalDamage);
        armorstand.setCustomName(Misc.getMessage("texts.damage_indicator").replaceAll("%damage%", formattedDamage));
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), armorstand::remove, 20L);
        e.setCancelled(false);
        e.setDamage(finalDamage);
        data.setBool("attack_cooldown", true);
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> data.setBool("attack_cooldown", false), (OrbRPG.getInstance().getConfig().getInt("attack_cooldown")));
    }
    @EventHandler
    public void onDamageToPlayer(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        Player p = (Player) e.getEntity();
        PlayerData data = new PlayerData(p);
        float defense = data.getFloat("defense");
        float damage = (float) e.getDamage() - defense;
        if (damage < 1) damage = 1;
        float currentHealth = data.getFloat("current_health");
        currentHealth -= damage;
        data.setFloat("current_health", currentHealth);
        new PlayerRefreshUI(p);
        e.setDamage(0.0);
        e.setCancelled(false);
    }
}