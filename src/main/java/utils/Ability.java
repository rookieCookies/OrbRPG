package utils;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Ability {
    Ability() { throw new IllegalStateException("Utility class"); }
    public static void pushForwards(Player p, float xMult, float yMult, float zMult) {
        @NotNull Vector direction = p.getLocation().getDirection();
        direction.setX(direction.getX() * xMult);
        direction.setY(direction.getY() * yMult);
        direction.setZ(direction.getZ() * zMult);
        p.setVelocity(p.getLocation().getDirection());
    }
}
