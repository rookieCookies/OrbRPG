package orbrpg.functions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import utils.Misc;
import utils.PlayerData;

import java.util.logging.Level;

public class PlayerDeath {
    private final Player player;

    public PlayerDeath(Player p) {
        this.player = p;
        if (!Bukkit.getOnlinePlayers().contains(p))
            return;
        var data = new PlayerData(p);
        new PlayerRefreshUI(p);
        if (data.getCurrentHealth() > 0)
            return;
        force();
    }
    public void force() {
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        Misc.warpPlayer(player, "spawn");
        new IncreaseStats(player).max();
        player.sendMessage(Misc.getMessage("messages.player_death"));
        if (config.getBoolean("features.on_death.totem_effect"))
            player.playEffect(EntityEffect.TOTEM_RESURRECT);
        if (config.getBoolean("features.on_death.clear_potions"))
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        var potion = new PotionEffect(PotionEffectType.HUNGER, 200_000, 100, false, false, false);
        player.addPotionEffect(potion);
        player.showTitle(Title.title(Component.text(Misc.coloured("&4&lYou died!")), Component.text("")));
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.player_death"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    player.getName()
            );
    }
}
