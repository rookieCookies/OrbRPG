package orbrpg.functions;

import orbrpg.Item;
import orbrpg.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;

public class PlayerRefreshStats {
    Player p;

    public PlayerRefreshStats(Player p) {
        this.p = p;
    }

    public void ReCalculate() {
        RefreshMainHand();
        RefreshOffhand();
        RefreshArmor();
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

        data.setFloat("maximum_health", maximumHealth);
        data.setFloat("defense", totalDefense);
        data.setFloat("maximum_tex", maximumTex);
        data.setFloat("damage", totalDamage);
        if (totalSpeed > 100) totalSpeed = 500;
        p.setWalkSpeed(totalSpeed / 100 * 0.2f);
        data.setFloat("speed", totalSpeed);
        data.setFloat("life_steal", totalLifeSteal);
    }

    public void RefreshMainHand() {
        PlayerInventory playerInventory = p.getInventory();
        PlayerData playerData = new PlayerData(p);
        ItemStack tool = playerInventory.getItemInMainHand();
        String type = Item.getTypeOfItem(tool);
        float mainHandHealth = 0f;
        float mainHandDefense = 0f;
        float mainHandDamage = 0f;
        float mainHandSpeed = 0f;
        float mainHandTex = 0f;
        float mainHandLifeSteal = 0f;
        float minWeaponDamage = 1f;
        float maxWeaponDamage = 1f;
        if (type != null && !type.equals("armor")) {
            if (type.equals("weapon")) {
                minWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_1");
                maxWeaponDamage = Item.getFloatFromItem(tool, "weapon_damage_2");
            }
            mainHandDamage = (float) Math.random() * (maxWeaponDamage - minWeaponDamage) + minWeaponDamage;
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

    public void RefreshOffhand() {
        PlayerInventory playerInventory = p.getInventory();
        PlayerData playerData = new PlayerData(p);
        ItemStack tool = playerInventory.getItemInOffHand();
        String type = Item.getTypeOfItem(tool);
        float offhandHealth = 0f;
        float offhandDefense = 0f;
        float offhandDamage = 0f;
        float offhandSpeed = 0f;
        float offhandTex = 0f;
        float offhandLifeSteal = 0f;
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

    public void RefreshArmor() {
        PlayerInventory playerInventory = p.getInventory();
        PlayerData playerData = new PlayerData(p);
        float armorHealth = 0;
        float armorDefense = 0;
        float armorDamage = 0;
        float armorTex = 0;
        float armorSpeed = 0;
        float armorLifeSteal = 0;
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
            if (armorItem == null) continue;
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
