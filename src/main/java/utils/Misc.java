package utils;

import orbrpg.OrbRPG;
import org.bukkit.ChatColor;

public class Misc {
    Misc() { throw new IllegalStateException("Utility class"); }
    public static String coloured(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }
    public static boolean chanceOf(double chance) {
        float i = (OrbRPG.getInstance().getRand().nextFloat() * 100);
        System.out.println(i);
        return i <= chance;
    }
}
