package orbrpg.items.crystals;

import org.bukkit.Particle;
import org.bukkit.entity.Player;
import utils.Ability;

public class DashCrystal {
    DashCrystal(Player p) {
        var loc = p.getLocation();
        loc.setY(loc.getY() + 0.2);
        p.getLocation().getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 30, 0.5, 0.05, 0.15);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 30, 0.3, 0.1, 0.3, 0.1);
        Ability.pushForwards(p, 2F, 1.2F, 2F);
    }
}
