package orbrpg.events.Mining;

import orbrpg.OrbRPG;
import orbrpg.events.Mining.Farming.MiningWheat;

public class RegisterMining {
    RegisterMining() {
        OrbRPG.getInstance().getServer().getPluginManager().registerEvents(new MiningWheat(), OrbRPG.getInstance());
    }
}
