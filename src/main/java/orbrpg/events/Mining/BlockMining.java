package orbrpg.events.Mining;

import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;
import utils.Item;
import utils.Misc;
import utils.PlayerData;

import java.util.Locale;

public class BlockMining implements Listener {
    @EventHandler
    public void onMine(BlockBreakEvent e) {
        var blockID = e.getBlock().getType().toString().toLowerCase(Locale.ROOT);
        FileConfiguration blocksFile = OrbRPG.getInstance().getBlocksFile();
        if (!blocksFile.contains(blockID) ||
                !blocksFile.getBoolean(blockID + ".enabled"))
            return;
        var data = new PlayerData(e.getPlayer());
        var levelRequirement = blocksFile.getInt(blockID + ".level_requirement", 0);
        if (levelRequirement > data.getLevel()) {
            e.getPlayer().sendMessage(Misc.getMessage("messages.block_level_requirement").replace("%level%", String.valueOf(levelRequirement)));
            return;
        }
        var dropItemID = blocksFile.getString(blockID + ".item_drop", "default");
        var dropItemChance = blocksFile.getDouble(blockID + ".item_drop_chance", 100);
        var goldDrop = blocksFile.getDouble(blockID + ".gold", 0);
        var expDrop = blocksFile.getDouble(blockID + ".exp", 0);
        var oldMaterial = e.getBlock().getType();
        var changeMaterial = Material.valueOf(blocksFile.getString(blockID + ".change_block", "AIR").toUpperCase());
        var respawnTime = blocksFile.getDouble(blockID + ".respawn_time", 1);
        OrbRPG.getInstance().getEconomy().depositPlayer(e.getPlayer(), goldDrop);
        data.addExp((float) expDrop);
        e.getBlock().setType(changeMaterial);
        Location dropLoc = e.getBlock().getLocation();
        dropLoc.setY(dropLoc.getY() + 0.5);
        if (Math.random() * 100 < dropItemChance)
            e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), Item.getItem(dropItemID));
        if (respawnTime != -1)
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> e.getBlock().setType(oldMaterial), (long) (20L * respawnTime));
    }
}
