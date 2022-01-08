package orbrpg.functions;

import orbrpg.OrbRPG;
import orbrpg.events.BlockMining;
import orbrpg.events.MiningWheat;

public class RegisterMining {
    public RegisterMining() {
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(new BlockMining(), OrbRPG.getInstance());
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(new MiningWheat(), OrbRPG.getInstance());
    }
}
