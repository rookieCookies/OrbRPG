package orbrpg.functions;

import orbrpg.PlayerData;
import org.bukkit.entity.Player;

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

    }
}