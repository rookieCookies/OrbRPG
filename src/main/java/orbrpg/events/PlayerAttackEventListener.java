package orbrpg.events;

import utils.Item;
import utils.Misc;
import orbrpg.OrbRPG;
import utils.PlayerData;
import orbrpg.functions.PlayerDeath;
import orbrpg.functions.PlayerRefreshUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public class PlayerAttackEventListener implements Listener {
    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) {
            return;
        }
        Player p;
        e.setCancelled(true);
        if (e.getDamager() instanceof Arrow) {
            p = (Player) ((Arrow) e.getDamager()).getShooter();
        } else p = (Player) e.getDamager();
        var data = new PlayerData(p);
        if (data.isAttackCooldownTrue()) {
            return;
        }
        assert p != null;
        ItemStack tool = p.getInventory().getItemInMainHand();
        String itemType = Item.getTypeOfItem(tool);
        float finalDamage = data.getDamage();
        var damageCause = e.getCause();
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            finalDamage /= 4;
        if (damageCause != EntityDamageEvent.DamageCause.PROJECTILE && "bow".equals(itemType))
            finalDamage /= 100;
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        if (config.getBoolean("damage_indicator.enabled")){
            var victimLocation = e.getEntity().getLocation();
            var random = OrbRPG.getInstance().getRand();
            float xPosition = (float) (victimLocation.getX() + random.nextFloat() - random.nextFloat());
            float yPosition = (float) (victimLocation.getY() + random.nextFloat() / 1.5 + 1);
            float zPosition = (float) (victimLocation.getZ() + random.nextFloat() - random.nextFloat());
            var loc = new Location(e.getEntity().getWorld(), xPosition, yPosition, zPosition);
            var armorStand = (ArmorStand) e.getEntity().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setCustomNameVisible(true);
            armorStand.setInvulnerable(true);
            armorStand.setMarker(true);
            armorStand.setVisible(false);
            var formattedDamage = String.valueOf(finalDamage);
            if (config.getBoolean("damage_indicator", true))
                formattedDamage = Misc.formatNumber(finalDamage);
            armorStand.setCustomName(Misc.getMessage("texts.damage_indicator").replace("%damage%", formattedDamage));
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), armorStand::remove, 20L);
        }
        e.setCancelled(false);
        e.setDamage(finalDamage);
        data.setAttackCooldownTrue();
        Bukkit.getScheduler().runTaskLater(
                OrbRPG.getInstance(),
                data::setAttackCooldownFalse,
                (OrbRPG.getInstance().getConfig().getInt("attack_cooldown")));
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> {
            if (e.getEntity().isDead())
                data.addHealth(data.getLifeSteal());
        }, 1L);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.attack_entity"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} Events > " + getClass().getName(),
                    p.getName()
            );
    }
    @EventHandler
    public void onDamageToPlayer(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p))
            return;
        var data = new PlayerData(p);
        float defense = data.getDefense();
        float damage = (float) e.getDamage() - defense;
        if (damage < 1)
            damage = 1;
        float currentHealth = data.getCurrentHealth();
        currentHealth -= damage;
        data.setCurrentHealth(currentHealth);
        new PlayerRefreshUI(p);
        e.setDamage(0.0);
        e.setCancelled(false);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.attacked_by_entity"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} Events > " + getClass().getName(),
                    p.getName()
            );
        new PlayerDeath(p);
    }
}