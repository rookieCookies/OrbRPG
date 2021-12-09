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
    final Player p;
    public SendActionBar(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        PlayerData data = new PlayerData(p);
        float maximumHealth = data.getFloat("maximum_health");
        float maximumTex = data.getFloat("maximum_tex");
        float currentHealth = data.getFloat("current_health");
        float currentTex = data.getFloat("current_tex");
        float defense = data.getFloat("defense");
        String message = "";
        if (10 < currentHealth / maximumHealth * 100)
            message += "&4%health%&4/%maximum_health%&4❤ ";
        else {
            SplittableRandom random = new SplittableRandom();
            String sub = "";
            if (random.nextInt(1000) == 1)
                sub = "You're low! So here is a big fucking text to make your life harder! Get Fucked!";
            p.showTitle(Title.title(Component.text(Misc.coloured("&4%health%&4/%maximum_health%&4❤"
                    .replaceAll("%maximum_health%", String.valueOf(Math.round(maximumHealth)))
                    .replaceAll("%health%", String.valueOf(Math.round(currentHealth))))
            ), Component.text(sub)));
            PotionEffect potion = new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, false, false, false);
            p.addPotionEffect(potion);
        }
        if (defense > 1)
            message += "&2%defense%&2\uD83D\uDEE1 ";
        message += "&6%tex%&6/%maximum_tex%&6✎";
        message = Misc.coloured(message)
                .replaceAll("%maximum_health%", Misc.formatNumber(maximumHealth))
                .replaceAll("%health%", Misc.formatNumber(currentHealth))
                .replaceAll("%maximum_tex%", Misc.formatNumber(maximumTex))
                .replaceAll("%tex%", Misc.formatNumber(currentTex))
                .replaceAll("%defense%", Misc.formatNumber(defense));
        p.sendActionBar(Component.text(message));
    }
}
