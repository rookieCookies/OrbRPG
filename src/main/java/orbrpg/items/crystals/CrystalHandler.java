package orbrpg.items.crystals;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataType;
import utils.Item;
import utils.PlayerData;

public class CrystalHandler implements Listener {
    @EventHandler
    public void onShift(PlayerToggleSneakEvent e) {
        if (!e.getPlayer().isSneaking())
            return;
        var data = new PlayerData(e.getPlayer());
        if (data.isCrystalOneCooldownTrue())
            return;
        if ("".equals(data.getCrystalOneID()))
            return;
        data.setCrystalOneCooldownTrue();
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), data::setCrystalOneCooldownFalse,
                Item.getDataOfItem(data.getCrystalOne()).get(new NamespacedKey(OrbRPG.getInstance(), "crystal_cooldown"), PersistentDataType.LONG));
        crystal(e.getPlayer(), data.getCrystalOneID());
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        if (e.getPlayer().isSneaking())
            return;
        e.setCancelled(true);
        var data = new PlayerData(e.getPlayer());
        if (data.isCrystalTwoCooldownTrue())
            return;
        if ("".equals(data.getCrystalTwoID()))
            return;
        data.setCrystalTwoCooldownTrue();
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), data::setCrystalOneCooldownFalse,
                Item.getDataOfItem(data.getCrystalOne()).get(new NamespacedKey(OrbRPG.getInstance(), "crystal_cooldown"), PersistentDataType.LONG));
        crystal(e.getPlayer(), data.getCrystalTwoID());
    }
    public void crystal(Player p, String id) {
        switch (id) {
            case "dash": new DashCrystal(p);
                break;
            default:
                throw new IllegalStateException("Unexpected Crystal Value");
        }
    }
}
