package orbrpg.events;

import orbrpg.Item;
import orbrpg.Misc;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
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

import java.util.Random;
import java.util.logging.Level;

public class PlayerAttackEventListener implements Listener {
    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) {
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getDamager();
        if (e.getDamager() instanceof Arrow) {
            System.out.println(((Arrow) e.getDamager()).getShooter());
            p = (Player) ((Arrow) e.getDamager()).getShooter();
        }
        PlayerData data = new PlayerData(p);
        if (data.isAttackCooldownTrue()) {
            return;
        }
        assert p != null;
        ItemStack tool = p.getInventory().getItemInMainHand();
        String itemType = Item.getTypeOfItem(tool);
        float finalDamage = data.getDamage();
        EntityDamageEvent.DamageCause damageCause = e.getCause();
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
            finalDamage /= 4;
        }
        if (damageCause != EntityDamageEvent.DamageCause.PROJECTILE && "bow".equals(itemType)) {
            finalDamage /= 100;
        }

        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        if (config.getBoolean("damage_indicator.enabled")){
            Location victimLocation = e.getEntity().getLocation();
            Location loc = new Location(e.getEntity().getWorld(), 0, 0, 0);
            Random random = OrbRPG.getInstance().getRand();
            loc.setX(victimLocation.getX() + random.nextFloat());
            loc.setY(victimLocation.getY() + (random.nextFloat() / 1.2));
            loc.setZ(victimLocation.getZ() + random.nextFloat());
            ArmorStand armorstand = (ArmorStand) e.getEntity().getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
            armorstand.setGravity(false);
            armorstand.setCustomNameVisible(true);
            armorstand.setInvulnerable(true);
            armorstand.setMarker(true);
            armorstand.setVisible(false);
            String formattedDamage = String.valueOf(finalDamage);
            if (config.getBoolean("damage_indicator", true))
                formattedDamage = Misc.formatNumber(finalDamage);
            armorstand.setCustomName(Misc.getMessage("texts.damage_indicator").replace("%damage%", formattedDamage));
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), armorstand::remove, 20L);
        }
        e.setCancelled(false);
        e.setDamage(finalDamage);
        data.setAttackCooldownTrue();
        Bukkit.getScheduler().runTaskLater(
                OrbRPG.getInstance(),
                data::setAttackCooldownFalse,
                (OrbRPG.getInstance().getConfig().getInt("attack_cooldown")));
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.events.player.attack_entity"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: {0} Events > " + getClass().getName(),
                    p.getName()
            );
    }
    @EventHandler
    public void onDamageToPlayer(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        Player p = (Player) e.getEntity();
        PlayerData data = new PlayerData(p);
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
    }
}