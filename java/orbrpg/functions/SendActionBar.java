package orbrpg.functions;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import orbrpg.Misc;
import orbrpg.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.SplittableRandom;

public class SendActionBar {
    private final Player p;
    SendActionBar(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        PlayerData data = new PlayerData(p);
        float maximumHealth = data.getMaximumHealth();
        float maximumTex = data.getMaximumTex();
        float currentHealth = data.getCurrentHealth();
        float currentTex = data.getCurrentTex();
        float defense = data.getDefense();
        String message = "";
        if (10 < currentHealth / maximumHealth * 100)
            message += "&4%health%&4/%maximum_health%&4❤ ";
        else {
            SplittableRandom random = new SplittableRandom();
            String sub = "";
            if (random.nextInt(1000) == 1)
                sub = "You're low! So here is a big fucking text to make your life harder! Get Fucked!";
            p.showTitle(Title.title(Component.text(Misc.coloured("&4%health%&4/%maximum_health%&4❤"
                    .replace("%maximum_health%", String.valueOf(Math.round(maximumHealth)))
                    .replace("%health%", String.valueOf(Math.round(currentHealth))))
            ), Component.text(sub)));
            PotionEffect potion = new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, false, false, false);
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
    }
}
