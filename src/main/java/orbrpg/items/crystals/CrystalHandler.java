package orbrpg.items.crystals;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataType;
import utils.Item;

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
        crystal(e.getPlayer(), data.getCrystalOneID(), 1);
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
        Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), data::setCrystalTwoCooldownFalse,
                Item.getDataOfItem(data.getCrystalOne()).get(new NamespacedKey(OrbRPG.getInstance(), "crystal_cooldown"), PersistentDataType.LONG));
        crystal(e.getPlayer(), data.getCrystalTwoID(), 2);
    }
    public void crystal(Player p, String id, int type) {
        var crystal = new CrystalAbility(p);
        var success = false;
        switch (id) {
            case "dash" -> success = crystal.dash();
            case "super_dash" -> success = crystal.superDash();
            case "small_heal" -> success = crystal.smallHeal();
            case "medium_heal" -> success = crystal.mediumHeal();
            case "large_heal" -> success = crystal.largeHeal();
            case "teleport" -> success = crystal.teleport();
            default -> throw new IllegalStateException("Unexpected Crystal Value");
        }
        var data = new PlayerData(p);
        if (type == 1 && !success)
            data.setCrystalOneCooldownFalse();
        else if (type == 2 && !success)
            data.setCrystalTwoCooldownFalse();
    }
}
