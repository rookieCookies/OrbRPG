package orbrpg.functions;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class IncreaseStats {
    private final Player p;

    public IncreaseStats(Player player) {
        this.p = player;
        run();
    }
    public void run() {
        PlayerData data = new PlayerData(p);
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        float maximumHealth = data.getFloat("maximum_health");
        float maximumTex = data.getFloat("maximum_tex");
        float currentHealth = data.getFloat("current_health");
        float currentTex = data.getFloat("current_tex");
        currentHealth += maximumHealth * config.getDouble("regeneration.health") / 100;
        currentTex += maximumTex * config.getDouble("regeneration.tex") / 100;
        if (currentHealth > maximumHealth) currentHealth = maximumHealth;
        if (currentTex > maximumTex) currentTex = maximumTex;
        data.setFloat("current_health", currentHealth);
        data.setFloat("current_tex", currentTex);
        OrbRPG.getInstance().getItemDatabase().set(String.valueOf(p.displayName()), p);
        OrbRPG.getInstance().saveItemDatabase();
    }
    public void max() {
        PlayerData data = new PlayerData(p);
        ConfigurationSection config = OrbRPG.getInstance().getConfig();
        float maximumHealth = data.getFloat("maximum_health");
        float maximumTex = data.getFloat("maximum_tex");
        data.setFloat("current_health", maximumHealth);
        data.setFloat("current_tex", maximumTex);
        OrbRPG.getInstance().getItemDatabase().set(p.getName(), p.toString());
        OrbRPG.getInstance().saveItemDatabase();
    }
}
