package orbrpg.functions;

import orbrpg.OrbRPG;
import orbrpg.events.BlockMining;
import orbrpg.events.MiningWheat;

import java.util.logging.Level;

public class RegisterMining {
    public RegisterMining() {
        final long start = System.currentTimeMillis();
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(new BlockMining(), OrbRPG.getInstance());
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(new MiningWheat(), OrbRPG.getInstance());

        final long fin = System.currentTimeMillis() - start;
        String logMessage = "Mining has been generated successfully! (" + fin + "ms)";
        OrbRPG.getInstance().getLogger().log(Level.INFO, logMessage);
    }
}
