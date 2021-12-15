package orbrpg.functions;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PlayerUpdateHealth {
    private final Player p;

    public PlayerUpdateHealth(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        PlayerData data = new PlayerData(p);
        double health = data.getCurrentHealth() / data.getMaximumHealth() * 20;
        if (health < 1)
            health = 1;
        else if (health > 20)
            health = 20;
        else health = Math.round(health); // We need to round it up since we need an integer to assign as health level
        p.setHealth((int) health);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.update_health_bar"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }
}