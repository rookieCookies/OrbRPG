package orbrpg.functions;

import utils.Misc;
import orbrpg.OrbRPG;
import utils.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerDeath {
    private final Player player;
    private boolean wasKilled;

    public PlayerDeath(Player p) {
        this.player = p;
        var data = new PlayerData(p);
        new PlayerRefreshUI(p);
        if (data.getCurrentHealth() > 0)
            return;
        wasKilled = force();
    }
    public boolean force() {
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        var w = config.getString("warps.spawn.world");
        if (w == null || Bukkit.getWorld(w) == null)
            return false;
        var x = config.getDouble("warps.spawn.x");
        var y = config.getDouble("warps.spawn.y");
        var z = config.getDouble("warps.spawn.z");
        var loc = new Location(Bukkit.getWorld(w), x, y, z);
        player.teleport(loc);
        new IncreaseStats(player).max();
        Misc.getMessage("messages.player_death");
        if (OrbRPG.getInstance().getConfig().getBoolean("features.on_death.totem_effect"))
            player.playEffect(EntityEffect.TOTEM_RESURRECT);
        if (OrbRPG.getInstance().getConfig().getBoolean("features.on_death.clear_potions"))
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        var potion = new PotionEffect(PotionEffectType.HUNGER, 200_000, 100, false, false, false);
        player.addPotionEffect(potion);
        return true;
    }
    public boolean wasKilled() {
        return wasKilled;
    }
}
