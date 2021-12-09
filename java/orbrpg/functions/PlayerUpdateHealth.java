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
        float health = data.getFloat("current_health") / data.getFloat("maximum_health") * 20;
        if (health < 1) health = 1;
        else if (health > 20) health = 20;
        p.setHealth(health);
    }
}