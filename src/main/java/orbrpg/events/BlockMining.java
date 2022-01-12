package orbrpg.events;

import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import utils.FileM;
import utils.Item;

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
            e.getPlayer().sendMessage(FileM.getMessage("messages.block_level_requirement").replace("%level%", String.valueOf(levelRequirement)));
            return;
        }
        var dropItemID = blocksFile.getString(blockID + ".item_drop", "default");
        var dropItem = Item.getItem(dropItemID);
        if (dropItem == null)
            return;
        var goldDrop = blocksFile.getDouble(blockID + ".gold", 0);
        OrbRPG.getInstance().getEconomy().depositPlayer(e.getPlayer(), goldDrop);
        var expDrop = blocksFile.getDouble(blockID + ".exp", 0);
        data.addExp((float) expDrop);
        dropItem.setAmount(1);
        var dropItemChance = blocksFile.getDouble(blockID + ".item_drop_chance", 100);
        var dropLoc = e.getBlock().getLocation();
        dropLoc.setY(dropLoc.getY() + 0.5);
        var oldMaterial = e.getBlock().getType();
        var oldBlockData = e.getBlock().getBlockData();
        var changeMaterial = Material.valueOf(blocksFile.getString(blockID + ".change_block", "AIR").toUpperCase(Locale.ENGLISH));
        e.getBlock().setType(changeMaterial);
        if (Math.random() * 100 < dropItemChance)
            e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), dropItem);
        var respawnTime = blocksFile.getDouble(blockID + ".respawn_time", 1);
        if (respawnTime != -1) {
            Bukkit.getScheduler().runTaskLater(OrbRPG.getInstance(), () -> {
                e.getBlock().setType(oldMaterial);
                e.getBlock().setBlockData(oldBlockData);
            }, (long) (20L * respawnTime));
        }
    }
}
