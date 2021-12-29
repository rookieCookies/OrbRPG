package orbrpg.functions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import orbrpg.OrbRPG;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import utils.Misc;
import utils.PlayerData;

import java.util.SplittableRandom;
import java.util.logging.Level;

public class SendActionBar {
    private final Player p;
    SendActionBar(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        var data = new PlayerData(p);
        float maximumHealth = data.getMaximumHealth();
        float maximumTex = data.getMaximumTex();
        float currentHealth = data.getCurrentHealth();
        float currentTex = data.getCurrentTex();
        float defense = data.getDefense();
        var message = "";
        if (10 < currentHealth / maximumHealth * 100 && currentHealth > 0)
            message += "&4%health%&4/%maximum_health%&4❤ ";
        else {
            var random = new SplittableRandom();
            var sub = "";
            if (random.nextInt(1000) == 1)
                sub = "You're low! So here is a big fucking text to make your life harder! Get Fucked!";
            if (currentHealth > 0)
                p.showTitle(Title.title(Component.text(Misc.coloured("&4%health%&4/%maximum_health%&4❤"
                        .replace("%maximum_health%", String.valueOf(Math.round(maximumHealth)))
                        .replace("%health%", String.valueOf(Math.round(currentHealth))))
                ), Component.text(sub)));
            var potion = new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, false, false, false);
            p.addPotionEffect(potion);
        }
        if (defense > 1)
            message += "&2%defense%&2\uD83D\uDEE1 ";
        message += "&6%tex%&6/%maximum_tex%&6✎";
        message = Misc.coloured(message)
                .replace("%maximum_health%", Misc.formatNumber(maximumHealth))
                .replace("%health%", Misc.formatNumber(currentHealth))
                .replace("%maximum_tex%", Misc.formatNumber(maximumTex))
                .replace("%tex%", Misc.formatNumber(currentTex))
                .replace("%defense%", Misc.formatNumber(defense));
        p.sendActionBar(Component.text(message));
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.send_actionbar"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }
}
