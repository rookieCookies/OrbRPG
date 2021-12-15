package orbrpg.functions;

import orbrpg.OrbRPG;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PlayerRefreshUI {
    private final Player p;
    public PlayerRefreshUI(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        new PlayerRefreshStats(p).reCalculate();
        new SendActionBar(p);
        new PlayerUpdateHealth(p);
        new PlayerUpdateTex(p);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.refresh_ui"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }
}
