package orbrpg.functions;

import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class PlayerRefreshStats {
    private final Player p;
    private List<Float> toolStats = new ArrayList<>();
    private List<Float> offhandStats = new ArrayList<>();
    private List<Float> armorStats = new ArrayList<>();

    public PlayerRefreshStats(Player p) {
        this.p = p;
    }

    public void reCalculate() {
        refreshMainHand();
        refreshOffhand();
        refreshArmor();
        var data = new PlayerData(p);
        final float mainHandHealth = toolStats.get(0);
        final float mainHandDefense = toolStats.get(1);
        final float mainHandDamage = toolStats.get(2);
        final float mainHandTex = toolStats.get(3);
        final float mainHandSpeed = toolStats.get(4);
        final float mainHandLifeSteal = toolStats.get(5);

        final float offhandHealth = offhandStats.get(0);
        final float offhandDefense = offhandStats.get(1);
        final float offhandDamage = offhandStats.get(2);
        final float offhandTex = offhandStats.get(3);
        final float offhandSpeed = offhandStats.get(4);
        final float offhandLifeSteal = offhandStats.get(5);

        final float armorHealth = armorStats.get(0);
        final float armorDefense = armorStats.get(1);
        final float armorDamage = armorStats.get(2);
        final float armorTex = armorStats.get(3);
        final float armorSpeed = armorStats.get(4);
        final float armorLifeSteal = armorStats.get(5);

        final float maximumHealth = 10 + mainHandHealth + offhandHealth + armorHealth;
        final float totalDefense = mainHandDefense + offhandDefense + armorDefense;
        final float maximumTex = 5 + mainHandTex + offhandTex + armorTex;
        final float totalDamage = mainHandDamage + offhandDamage + armorDamage;
        float totalSpeed = mainHandSpeed + offhandSpeed + armorSpeed + 100;
        final float totalLifeSteal = mainHandLifeSteal + offhandLifeSteal + armorLifeSteal;
        data.setMaximumHealth(maximumHealth);
        data.setDefense(totalDefense);
        data.setMaximumTex(maximumTex);
        data.setDamage(totalDamage);
        if (totalSpeed > 500)
            totalSpeed = 500;
        p.setWalkSpeed(totalSpeed / 100 * 0.2F);
        data.setSpeed(totalSpeed);
        data.setLifeSteal(totalLifeSteal);
        if (OrbRPG.getInstance().getConfig().getBoolean("debug.functions.recalculate_stats"))
            OrbRPG.getInstance().getLogger().log(
                    Level.INFO,
                    "Debug: ({0}) Function > " + getClass().getName(),
                    p.getName()
            );
    }

    public void refreshMainHand() {
        var playerInventory = p.getInventory();
        ItemStack tool = playerInventory.getItemInMainHand();
        String type = Item.getTypeOfItem(tool);
        var mainHandHealth = 0F;
        var mainHandDefense = 0F;
        var mainHandDamage = 0F;
        var mainHandSpeed = 0F;
        var mainHandTex = 0F;
        var mainHandLifeSteal = 0F;
        var minWeaponDamage = 1F;
        var maxWeaponDamage = 1F;
        if (!"armor".equals(type)) {
            if ("weapon".equals(type)) {
                minWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_1");
                maxWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_2");
            }
            mainHandDamage = (OrbRPG.getInstance().getRand()
                    .nextFloat() * (maxWeaponDamage - minWeaponDamage) + minWeaponDamage);
            mainHandHealth = Item.getFloatFromItem(tool, "health");
            mainHandDefense = Item.getFloatFromItem(tool, "defense");
            mainHandTex = Item.getFloatFromItem(tool, "tex");
            mainHandSpeed = Item.getFloatFromItem(tool, "speed");
            mainHandLifeSteal = Item.getFloatFromItem(tool, "life_steal");
        }
        toolStats = new ArrayList<>();
        toolStats.add(0, mainHandHealth);
        toolStats.add(1, mainHandDefense);
        toolStats.add(2, mainHandDamage);
        toolStats.add(3, mainHandTex);
        toolStats.add(4, mainHandSpeed);
        toolStats.add(5, mainHandLifeSteal);
    }

    public void refreshOffhand() {
        var playerInventory = p.getInventory();
        ItemStack tool = playerInventory.getItemInOffHand();
        String type = Item.getTypeOfItem(tool);
        var offhandHealth = 0F;
        var offhandDefense = 0F;
        var offhandDamage = 0F;
        var offhandSpeed = 0F;
        var offhandTex = 0F;
        var offhandLifeSteal = 0F;
        if (type != null && Objects.equals(type, "offhand")) {
            offhandHealth = Item.getFloatFromItem(tool, "health");
            offhandDefense = Item.getFloatFromItem(tool, "defense");
            offhandDamage = Item.getFloatFromItem(tool, "damage");
            offhandTex = Item.getFloatFromItem(tool, "tex");
            offhandSpeed = Item.getFloatFromItem(tool, "speed");
            offhandLifeSteal = Item.getFloatFromItem(tool, "life_steal");
        }
        offhandStats = new ArrayList<>();
        offhandStats.add(0, offhandHealth);
        offhandStats.add(1, offhandDefense);
        offhandStats.add(2, offhandDamage);
        offhandStats.add(3, offhandTex);
        offhandStats.add(4, offhandSpeed);
        offhandStats.add(5, offhandLifeSteal);
    }

    public void refreshArmor() {
        var playerInventory = p.getInventory();
        var armorHealth = 0F;
        var armorDefense = 0F;
        var armorDamage = 0F;
        var armorTex = 0F;
        var armorSpeed = 0F;
        var armorLifeSteal = 0F;
        for (var i = 1; i < 4; i++) {
            ItemStack armorItem = switch (i) {
                case 1 -> playerInventory.getHelmet();
                case 2 -> playerInventory.getChestplate();
                case 3 -> playerInventory.getLeggings();
                default -> playerInventory.getBoots();
            };
            if (armorItem == null)
                continue;
            armorHealth += Item.getFloatFromItem(armorItem, "health");
            armorDefense += Item.getFloatFromItem(armorItem, "defense");
            armorDamage += Item.getFloatFromItem(armorItem, "damage");
            armorTex += Item.getFloatFromItem(armorItem, "tex");
            armorSpeed += Item.getFloatFromItem(armorItem, "speed");
            armorLifeSteal += Item.getFloatFromItem(armorItem, "life_steal");
        }
        armorStats = new ArrayList<>();
        armorStats.add(0, armorHealth);
        armorStats.add(1, armorDefense);
        armorStats.add(2, armorDamage);
        armorStats.add(3, armorTex);
        armorStats.add(4, armorSpeed);
        armorStats.add(5, armorLifeSteal);
    }
}
