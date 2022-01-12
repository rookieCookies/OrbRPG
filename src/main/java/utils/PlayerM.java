package utils;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayerM {
    PlayerM() { throw new IllegalStateException("Utility class"); }
    public static void teleportPlayer(Player p, Location loc, boolean lockPlayersRotation) {
        var teleportLocation = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
        teleportLocation = teleportLocation.getBlock().getLocation();
        teleportLocation.add(0.5F, 0F, 0.5F);
        if (lockPlayersRotation) {
            teleportLocation.setYaw(p.getLocation().getYaw());
            teleportLocation.setPitch(p.getLocation().getPitch());
        }
        p.teleport(teleportLocation);
    }
    public static void warpPlayer(Player p, String warpID) {
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        var w = config.getString("warps."+warpID+".w");
        if (w == null || Bukkit.getWorld(w) == null)
            return;
        var x = config.getDouble("warps."+warpID+".x");
        var y = config.getDouble("warps."+warpID+".y");
        var z = config.getDouble("warps."+warpID+".z");
        teleportPlayer(p, new Location(Bukkit.getWorld(w), x, y, z), true);
    }
}
