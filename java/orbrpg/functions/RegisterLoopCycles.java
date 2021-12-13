package orbrpg.functions;

import net.kyori.adventure.text.Component;
import orbrpg.Misc;
import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class RegisterLoopCycles {
    public RegisterLoopCycles() {
        run();
    }

    public void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                float x = Math.round(Math.random());
                String message;
                if (x == 0)
                    message = Misc.coloured(
                            "&9Do /discord to join our discord, it's much appreciated!"
                    );
                else if (x == 1)
                    message = Misc.coloured(
                            "&5Tip: &eWhile looking at a recipe of an item, you can " +
                                    "click on the required items to see how to craft them!"
                    );
                else message = "";
                Bukkit.broadcast(Component.text(message));
            }
        }.runTaskTimer(OrbRPG.getInstance(), 1, 1200); // Tips loop
    }
}
