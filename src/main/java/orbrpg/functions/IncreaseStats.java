package orbrpg.functions;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class IncreaseStats {
    private final Player p;

    public IncreaseStats(Player player) {
        this.p = player;
        run();
    }
    public void run() {
        var data = new PlayerData(p);
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        float maximumHealth = data.getMaximumHealth();
        float maximumTex = data.getMaximumTex();
        float currentHealth = data.getCurrentHealth();
        float currentTex = data.getCurrentTex();
        currentHealth += maximumHealth * config.getDouble("stats.regeneration.health") / 100;
        currentTex += maximumTex * config.getDouble("stats.regeneration.tex") / 100;
        if (currentHealth > maximumHealth)
            currentHealth = maximumHealth;
        if (currentTex > maximumTex)
            currentTex = maximumTex;
        data.setCurrentHealth(currentHealth);
        data.setCurrentTex(currentTex);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.increase_stats"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }
    public void max() {
        var data = new PlayerData(p);
        float maximumHealth = data.getMaximumHealth();
        float maximumTex = data.getMaximumTex();
        data.setCurrentHealth(maximumHealth);
        data.setCurrentTex(maximumTex);
    }
}
