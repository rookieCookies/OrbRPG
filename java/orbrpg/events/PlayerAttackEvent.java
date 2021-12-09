package orbrpg.events;

import net.kyori.adventure.text.Component;
import orbrpg.Item;
import orbrpg.Misc;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlayerAttackEvent {
    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow))
            return;
        e.setCancelled(true);
        Player p = (Player) e.getDamager();
        if (e.getDamager() instanceof Arrow) p = (Player) ((Arrow) e.getDamager()).getShooter();
        PlayerData data = new PlayerData(p);
        if (!data.getBool("attack_cooldown")) return;
        assert p != null;
        ItemStack tool = p.getInventory().getItemInMainHand();
        String itemType = Item.getTypeOfItem(tool);
        float minWeaponDamage = 1f;
        float maxWeaponDamage = 1f;
        if (itemType.equals("weapon")) {
            minWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_1");
            maxWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_2");
        }
        float finalDamage = (float) Math.random() * (maxWeaponDamage - minWeaponDamage) + minWeaponDamage;;
        EntityDamageEvent.DamageCause damageCause = e.getCause();
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            finalDamage /= 4;
        else if (!(damageCause == EntityDamageEvent.DamageCause.PROJECTILE) && itemType.equals("bow"))
            finalDamage /= 100;
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
}
