package orbrpg.functions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import utils.FileM;
import utils.Misc;
import utils.PlayerM;

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
        new IncreaseStats(player).max();
        if (config.getBoolean("features.on_death.totem_effect"))
            player.playEffect(EntityEffect.TOTEM_RESURRECT);
        if (config.getBoolean("features.on_death.clear_potions"))
            for (PotionEffect effect : player.getActivePotionEffects()) {
                player.removePotionEffect(effect.getType());
            }
        var potion = new PotionEffect(PotionEffectType.HUNGER, 200_000, 100, false, false, false);
        player.addPotionEffect(potion);
        var data = new PlayerData(player);
        data.setDeathLocation(player.getLocation());
        data.setRespawnableTrue();
        if (data.getDarkQuartz() >= 5) {
            var message = new TextComponent(Misc.coloured(FileM.getMessage("messages.player_death") + " &5Pay &d5&5 Dark Quartz to respawn back at where you died &8(Click here!)"));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/respawn"));
            player.sendMessage(message);
        } else player.sendMessage(FileM.getMessage("messages.player_death"));
        player.showTitle(Title.title(Component.text(Misc.coloured("&4&lYou died!")), Component.text("")));
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> PlayerM.warpPlayer(player, "spawn"), 5);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.player_death"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    player.getName()
            );
    }
}
