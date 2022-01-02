package orbrpg.functions;

import orbrpg.OrbRPG;
import orbrpg.events.mining.BlockMining;
import orbrpg.events.mining.MiningWheat;

public class RegisterMining {
    public RegisterMining() {
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(new BlockMining(), OrbRPG.getInstance());
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(new MiningWheat(), OrbRPG.getInstance());
    }
}
