package orbrpg.events;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import orbrpg.functions.PlayerDeath;
import orbrpg.functions.PlayerRefreshUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import utils.FileM;
import utils.Item;
import utils.Misc;
import utils.i.MathM;
import utils.i.StringM;

import java.util.Locale;
import java.util.logging.Level;

public class PlayerAttackEventListener implements Listener {
    @EventHandler
    public void onDamageByPlayer(EntityDamageByEntityEvent e) {
        if ((!(e.getDamager() instanceof Player) && !(e.getDamager() instanceof Arrow)) || e.getEntity() instanceof HumanEntity) {
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
            finalDamage /= 95;
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        if (config.getBoolean("combat.damage_indicator.enabled")){
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
            if (config.getBoolean("combat.damage_indicator", true))
                formattedDamage = StringM.getFormattedNumber(finalDamage);
            armorStand.setCustomName(FileM.getMessage("texts.damage_indicator").replace("%damage%", formattedDamage));
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), armorStand::remove, 20L);
        }
        e.setCancelled(false);
        e.setDamage(finalDamage);
        data.setAttackCooldownTrue();
        Bukkit.getScheduler().runTaskLater(
                OrbRPG.getInstance(),
                data::setAttackCooldownFalse,
                (OrbRPG.getInstance().getConfig().getInt("combat.attack_cooldown")));
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
    public void onEntityDeath(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null)
            return;
        String name = e.getEntity().getName();
        System.out.println(name);
        name = name.substring(name.indexOf(']') + 2, name.indexOf('(') - 3);
        System.out.println("[" + name + "]");
        name = name.toLowerCase(Locale.ROOT).replace(' ', '_');
        name = ChatColor.stripColor(name);
        var mobsFile = OrbRPG.getInstance().getMobsFile();
        System.out.println(mobsFile.getValues(false));
        System.out.println(mobsFile.contains(name));
        OrbRPG.getInstance().saveMobsFile();
        if (!mobsFile.contains(name))
            return;
        System.out.println("Mobs file contains ID");
        if (!mobsFile.getBoolean(name + ".enabled"))
            return;
        System.out.println("The mob is enabled");
        var data = new PlayerData(e.getEntity().getKiller());
        var exp = mobsFile.getDouble(name + ".drop.exp", 0);
        var gold = mobsFile.getDouble(name + ".drop.gold.amount", 0);
        var goldChance = mobsFile.getDouble(name + ".drop.gold.chance", 0);
        var darkQuartz = mobsFile.getDouble(name + ".drop.dark_quartz.amount", 0);
        var darkQuartzChance = mobsFile.getDouble(name + ".drop.dark_quartz.chance", 0);
        var itemDropID = mobsFile.getString(name + ".drop.item.id");
        var itemDrop = Item.getItem(itemDropID);
        itemDrop.setAmount(mobsFile.getInt(name + ".drop.item.amount"));
        var itemDropChance = mobsFile.getDouble(name + ".drop.item.id");
        System.out.println("Created all the variables, registering stats");
        data.addExp((float) exp);
        if (MathM.chanceOf(goldChance))
            OrbRPG.getInstance().getEconomy().depositPlayer(e.getEntity().getKiller(), gold);
        if (Misc.chanceOf(darkQuartzChance))
            data.addDarkQuartz((float) darkQuartz);
        if (Misc.chanceOf(itemDropChance))
            e.getEntity().getWorld().dropItem(e.getEntity().getLocation().add(new Vector(0, 0.5, 0)), itemDrop);
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