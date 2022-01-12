package orbrpg.items.crystals;

import orbrpg.PlayerData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import utils.Ability;
import utils.PlayerM;

public class CrystalAbility {
    private final Player p;
    CrystalAbility(Player p) {
        this.p = p;
    }
    public boolean dash() {
        var loc = p.getLocation();
        loc.setY(loc.getY() + 0.2);
        p.getLocation().getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 30, 0.5, 0.05, 0.15);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 30, 0.3, 0.1, 0.3, 0.1);
        Ability.pushForwards(p, 2F, 1.2F, 2F);
        return true;
    }
    public boolean superDash() {
        var loc = p.getLocation();
        loc.setY(loc.getY() + 0.2);
        p.getLocation().getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 120, 0.5, 0.05, 0.3);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 30, 0.4, 0.2, 0.4, 0.25);
        Ability.pushForwards(p, 4F, 1.2F, 4F);
        Ability.pushForwards(p, 4F, 1.4F, 4F);
        Ability.pushForwards(p, 4F, 1.4F, 4F);
        Ability.pushForwards(p, 4F, 1.4F, 4F);
        return true;
    }
    public boolean smallHeal() {
        var playerData = new PlayerData(p);
        playerData.addHealth(5);
        var loc = p.getLocation();
        loc.setY(loc.getY() + 1);
        loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 10, 0.2, 0.5, 0.2, 0.1);
        return true;
    }
    public boolean mediumHeal() {
        var playerData = new PlayerData(p);
        playerData.addHealth(30);
        var loc = p.getLocation();
        loc.setY(loc.getY() + 1);
        loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 25, 0.3, 0.5, 0.3, 0.15);
        return true;
    }
    public boolean largeHeal() {
        var playerData = new PlayerData(p);
        playerData.addHealth(150);
        var loc = p.getLocation();
        loc.setY(loc.getY() + 1);
        loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc, 75, 0.4, 0.5, 0.4, 0.2);
        return true;
    }
    public boolean teleport() {
        var targetBlock = p.getTargetBlock(35);
        if (targetBlock == null)
            return false;
        var targetBlockLocation = targetBlock.getLocation();
        while (targetBlockLocation.getBlock().getType() != Material.AIR)
            targetBlockLocation.setY(targetBlockLocation.getY() + 1);
        var loc = p.getLocation().getBlock().getLocation();
        loc.add(new Vector(0, -0.5, 0));
        var x = loc.getX() - targetBlockLocation.getX();
        var y = loc.getY() - targetBlockLocation.getY();
        var z = loc.getZ() - targetBlockLocation.getZ();
        var targetVector = new Vector(x, y + 1, z);
        float difference = (float) (Math.abs(x) + Math.abs(y) + Math.abs(z));
        var playerLocation = p.getLocation();
        PlayerM.teleportPlayer(p, targetBlockLocation, true);
        loc.getWorld().spawnParticle(Particle.REVERSE_PORTAL, playerLocation.add(new Vector(0, 1, 0)), 200, 0, 0, 0, 2);
        double dividedDistance = difference / 1.2;
        for (var i = 0; i < dividedDistance; i++) {
            double division = dividedDistance * i;
            if (division == 0)
                division = 1;
            var spawnLoc = new Location(
                    playerLocation.getWorld(),
                    playerLocation.getX() - (targetVector.getX() / division),
                    playerLocation.getY() + 1 - (targetVector.getY() / division),
                    playerLocation.getZ() - (targetVector.getZ() / division)
            );
            loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, spawnLoc, 30, 0.05, 0.05, 0.05, 0.3);
            loc.getWorld().spawnParticle(Particle.DRAGON_BREATH, spawnLoc, 5, 0.05, 0.05, 0.05, 0.05);
        }
        loc.getWorld().spawnParticle(Particle.REVERSE_PORTAL, p.getLocation(), 200, 0, 0, 0, 2);
        return true;
    }
    public void moveToward(Entity entity, Location to, double speed){
        var loc = entity.getLocation();
        var x = loc.getX() - to.getX();
        var y = loc.getY() - to.getY();
        var z = loc.getZ() - to.getZ();
        var velocity = new Vector(x, y, z).normalize().multiply(-speed);
        entity.setVelocity(velocity);
    }
}
