package orbrpg.functions;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class PlayerUpdateTex {
    private final Player p;

    public PlayerUpdateTex(Player p) {
        this.p = p;
        run();
    }
    public void run() {
        PlayerData data = new PlayerData(p);
        double tex = data.getCurrentTex() / data.getMaximumTex() * 20;
        if (tex < 1)
            tex = 1;
        else if (tex > 20)
            tex = 20;
        else tex = Math.round(tex); // We need to round it up since we need an integer to assign as food level
        p.setFoodLevel((int) tex);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.update_tex_bar"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }
}