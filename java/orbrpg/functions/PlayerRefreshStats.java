package orbrpg.functions;

import orbrpg.Item;
import orbrpg.OrbRPG;
import orbrpg.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class PlayerRefreshStats {
    private final Player p;

    public PlayerRefreshStats(Player p) {
        this.p = p;
    }

    public void reCalculate() {
        refreshMainHand();
        refreshOffhand();
        refreshArmor();
        PlayerData data = new PlayerData(p);
        final float mainHandHealth = data.getFloat("tool_health");
        final float mainHandDefense = data.getFloat("tool_defense");
        final float mainHandTex = data.getFloat("tool_tex");
        final float mainHandDamage = data.getFloat("tool_damage");
        final float mainHandSpeed = data.getFloat("tool_speed");
        final float mainHandLifeSteal = data.getFloat("tool_life_steal");

        final float offhandHealth = data.getFloat("offhand_health");
        final float offhandDefense = data.getFloat("offhand_defense");
        final float offhandTex = data.getFloat("offhand_tex");
        final float offhandDamage = data.getFloat("offhand_damage");
        final float offhandSpeed = data.getFloat("offhand_speed");
        final float offhandLifeSteal = data.getFloat("offhand_life_steal");

        final float armorHealth = data.getFloat("armor_health");
        final float armorDefense = data.getFloat("armor_defense");
        final float armorTex = data.getFloat("armor_tex");
        final float armorDamage = data.getFloat("armor_damage");
        final float armorSpeed = data.getFloat("armor_speed");
        final float armorLifeSteal = data.getFloat("armor_life_steal");

        final float maximumHealth = 10 + mainHandHealth + offhandHealth + armorHealth;
        final float totalDefense = mainHandDefense + offhandDefense + armorDefense;
        final float maximumTex = 5 + mainHandTex + offhandTex + armorTex;
        final float totalDamage = mainHandDamage + offhandDamage + armorDamage;
        float totalSpeed = mainHandSpeed + offhandSpeed + armorSpeed;
        final float totalLifeSteal = mainHandLifeSteal + offhandLifeSteal + armorLifeSteal;

        data.setMaximumHealth(maximumHealth);
        data.setDefense(totalDefense);
        data.setMaximumTex(maximumTex);
        data.setDamage(totalDamage);
        if (totalSpeed > 100)
            totalSpeed = 500;
        p.setWalkSpeed(totalSpeed / 100 * 0.2F);
        data.setSpeed(totalSpeed);
        data.setLifeSteal(totalLifeSteal);
    }

    public void refreshMainHand() {
        PlayerInventory playerInventory = p.getInventory();
        PlayerData playerData = new PlayerData(p);
        ItemStack tool = playerInventory.getItemInMainHand();
        String type = Item.getTypeOfItem(tool);
        float mainHandHealth = 0F;
        float mainHandDefense = 0F;
        float mainHandDamage = 0F;
        float mainHandSpeed = 0F;
        float mainHandTex = 0F;
        float mainHandLifeSteal = 0F;
        double minWeaponDamage = 1F;
        double maxWeaponDamage = 1F;
        if (!"armor".equals(type)) {
            if ("weapon".equals(type)) {
                minWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_1");
                maxWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_2");
            }
            mainHandDamage = (float) (OrbRPG.getInstance().getRand()
                    .nextFloat() * (maxWeaponDamage - minWeaponDamage) + minWeaponDamage);
            mainHandHealth = Item.getFloatFromItem(tool, "health");
            mainHandDefense = Item.getFloatFromItem(tool, "defense");
            mainHandTex = Item.getFloatFromItem(tool, "tex");
            mainHandSpeed = Item.getFloatFromItem(tool, "speed");
            mainHandLifeSteal = Item.getFloatFromItem(tool, "life_steal");
        }
        playerData.setFloat("tool_health", mainHandHealth);
        playerData.setFloat("tool_defense", mainHandDefense);
        playerData.setFloat("tool_damage", mainHandDamage);
        playerData.setFloat("tool_tex", mainHandTex);
        playerData.setFloat("tool_speed", mainHandSpeed);
        playerData.setFloat("tool_life_steal", mainHandLifeSteal);
    }

    public void refreshOffhand() {
        PlayerInventory playerInventory = p.getInventory();
        PlayerData playerData = new PlayerData(p);
        ItemStack tool = playerInventory.getItemInOffHand();
        String type = Item.getTypeOfItem(tool);
        float offhandHealth = 0F;
        float offhandDefense = 0F;
        float offhandDamage = 0F;
        float offhandSpeed = 0F;
        float offhandTex = 0F;
        float offhandLifeSteal = 0F;
        if (type != null && Objects.equals(type, "offhand")) {
            offhandHealth = Item.getFloatFromItem(tool, "health");
            offhandDefense = Item.getFloatFromItem(tool, "defense");
            offhandDamage = Item.getFloatFromItem(tool, "damage");
            offhandTex = Item.getFloatFromItem(tool, "tex");
            offhandSpeed = Item.getFloatFromItem(tool, "speed");
            offhandLifeSteal = Item.getFloatFromItem(tool, "life_steal");
        }
        playerData.setFloat("offhand_health", offhandHealth);
        playerData.setFloat("offhand_defense", offhandDefense);
        playerData.setFloat("offhand_damage", offhandDamage);
        playerData.setFloat("offhand_tex", offhandTex);
        playerData.setFloat("offhand_speed", offhandSpeed);
        playerData.setFloat("offhand_life_steal", offhandLifeSteal);
    }

    public void refreshArmor() {
        PlayerInventory playerInventory = p.getInventory();
        PlayerData playerData = new PlayerData(p);
        float armorHealth = 0F;
        float armorDefense = 0F;
        float armorDamage = 0F;
        float armorTex = 0F;
        float armorSpeed = 0F;
        float armorLifeSteal = 0F;
        for (int i = 1; i < 4; i++) {
            ItemStack armorItem;
            switch (i) {
                case 1:
                    armorItem = playerInventory.getHelmet();
                    break;
                case 2:
                    armorItem = playerInventory.getChestplate();
                    break;
                case 3:
                    armorItem = playerInventory.getLeggings();
                    break;
                default:
                    armorItem = playerInventory.getBoots();
            }
            if (armorItem == null)
                continue;
            armorHealth += Item.getFloatFromItem(armorItem, "health");
            armorDefense += Item.getFloatFromItem(armorItem, "defense");
            armorDamage += Item.getFloatFromItem(armorItem, "damage");
            armorTex += Item.getFloatFromItem(armorItem, "tex");
            armorSpeed += Item.getFloatFromItem(armorItem, "speed");
            armorLifeSteal += Item.getFloatFromItem(armorItem, "life_steal");
        }
        playerData.setFloat("armor_health", armorHealth);
        playerData.setFloat("armor_defense", armorDefense);
        playerData.setFloat("armor_damage", armorDamage);
        playerData.setFloat("armor_tex", armorTex);
        playerData.setFloat("armor_speed", armorSpeed);
        playerData.setFloat("armor_life_steal", armorLifeSteal);
    }
}
