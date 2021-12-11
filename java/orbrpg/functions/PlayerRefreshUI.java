package orbrpg.functions;

import org.bukkit.entity.Player;

public class PlayerRefreshUI {
    private final Player p;
    public PlayerRefreshUI(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        new PlayerRefreshStats(p).ReCalculate();
        new SendActionBar(p);
        new PlayerUpdateHealth(p);
        new PlayerUpdateTex(p);
    }
}
