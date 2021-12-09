package orbrpg.functions;

import orbrpg.PlayerData;
import org.bukkit.entity.Player;

public class PlayerUpdateTex {
    private final Player p;

    public PlayerUpdateTex(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        PlayerData data = new PlayerData(p);
        float tex = data.getFloat("current_tex") / data.getFloat("maximum_tex") * 20;
        if (tex < 1) tex = 1;
        else if (tex > 20) tex = 20;
        p.setFoodLevel((int) tex);
    }
}