package orbrpg.functions;

import net.kyori.adventure.text.Component;
import orbrpg.OrbRPG;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import utils.Item;
import utils.Misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterItems {
    public RegisterItems() {
        run();
    }

    public void run() {
        var instance = OrbRPG.getInstance();
        ConfigurationSection itemDataBase = instance.getItemDatabase();
        ConfigurationSection itemsFile = instance.getItemsFile();
        Map<String, Object> sec = instance.getItemsFile().getValues(false);
        for (Object a : sec.values()) {
            var path = a.toString();
            path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
            if (!itemsFile.getBoolean(path + ".enabled"))
                continue;
            var itemMaterialString = itemsFile.getString(path + ".material");
            if (itemMaterialString == null)
                itemMaterialString = "stone";
            var itemMaterial = Material.matchMaterial(itemMaterialString);
            assert itemMaterial != null;
            var item = new ItemStack(itemMaterial);
            Item.hideAttributes(item);
            var itemMeta = item.getItemMeta();
            var data = itemMeta.getPersistentDataContainer();
            var itemRarity = itemsFile.getString(path + ".rarity");
            String itemRarityWithPath = "rarities." + itemRarity;
            if ("".equals(Misc.getMessage(itemRarityWithPath + ".lore"))) {
                itemRarity = "default";
                itemRarityWithPath = "rarities." + itemRarity;
            }
            String rarityLore = Misc.getMessage(itemRarityWithPath + ".lore");
            String rarityColor = Misc.getMessage(itemRarityWithPath + ".color_id");
            String displayName = Misc.coloured(rarityColor + itemsFile.getString(path + ".display_name"));
            var type = itemsFile.getString(path + ".type", "item");
            data.set(new NamespacedKey(OrbRPG.getInstance(), "item_id"), PersistentDataType.STRING, path);
            data.set(new NamespacedKey(OrbRPG.getInstance(), "item_type"), PersistentDataType.STRING, type);
            itemMeta.displayName(Component.text(displayName));
            float baseDamage = (float) itemsFile.getDouble(path + ".stats.base_damage");
            boolean isWeapon = Objects.equals(type, "weapon");
            List<String> loreList = new ArrayList<>();
            if (baseDamage != 0) {
                if (isWeapon) {
                    var damageDifference = itemsFile.getDouble(path + ".stats.difference");
                    float damage1 = (float) (baseDamage * (1 - damageDifference));
                    float damage2 = (float) (baseDamage * (1 + damageDifference));
                    var weaponDamage1 = new NamespacedKey(instance, "weapon_damage_1");
                    var weaponDamage2 = new NamespacedKey(instance, "weapon_damage_2");
                    data.set(weaponDamage1, PersistentDataType.FLOAT, damage1);
                    data.set(weaponDamage2, PersistentDataType.FLOAT, damage2);
                    loreList.add(Misc.coloured("&7Damage: &c" + damage1 + "↔" + damage2));
                } else {
                    data.set(new NamespacedKey(OrbRPG.getInstance(), "damage"), PersistentDataType.FLOAT, baseDamage);
                    loreList.add(Misc.coloured("&7Damage: &c" + baseDamage));
                }
                loreList.add(" ");
            }
            var sectionOne = false;
            float breakPower = (float) itemsFile.getDouble(path + ".stats.break_power");
            if (breakPower != 0) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "break_power"), PersistentDataType.FLOAT, breakPower);
                loreList.add(Misc.coloured("&7Break Power: &a" + breakPower));
                sectionOne = true;
            }
            float cooldown = (float) itemsFile.getDouble(path + ".stats.cooldown");
            if (cooldown != 0 && "bow".equals(type)) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "bow_cooldown"), PersistentDataType.FLOAT, cooldown);
                loreList.add(Misc.coloured("&7Cooldown: &a" + cooldown / 20 + "s"));
                sectionOne = true;
            }
            float speed = (float) itemsFile.getDouble(path + ".stats.speed");
            if (speed != 0) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "speed"), PersistentDataType.FLOAT, speed);
                loreList.add(Misc.coloured("&7Speed: &a" + speed));
                sectionOne = true;
            }
            float lifeSteal = (float) itemsFile.getDouble(path + ".stats.life_steal");
            if (lifeSteal != 0) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "life_steal"), PersistentDataType.FLOAT, lifeSteal);
                loreList.add(Misc.coloured("&7Life Steal: &a" + lifeSteal));
                sectionOne = true;
            }
            float tex = (float) itemsFile.getDouble(path + ".stats.tex");
            if (tex != 0) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "tex"), PersistentDataType.FLOAT, tex);
                loreList.add(Misc.coloured("&7Tex: &a" + tex));
                sectionOne = true;
            }
            if (sectionOne)
                loreList.add(" ");

            var sectionTwo = false;
            float health = (float) itemsFile.getDouble(path + ".stats.health");
            if (health != 0) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "health"), PersistentDataType.FLOAT, health);
                loreList.add(Misc.coloured("&7Health: &a" + health));
                sectionTwo = true;
            }
            float defense = (float) itemsFile.getDouble(path + ".stats.defense");
            if (defense != 0) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "defense"), PersistentDataType.FLOAT, defense);
                loreList.add(Misc.coloured("&7Defense: &a" + defense));
                sectionTwo = true;
            }
            if (sectionTwo)
                loreList.add(" ");
            var creator = itemsFile.getString(path + ".info.creator", "Server");
            data.set(new NamespacedKey(OrbRPG.getInstance(), "creator"), PersistentDataType.STRING, creator);
            var discordOfCreator = itemsFile.getString(path + ".info.discord", "Server");
            data.set(new NamespacedKey(OrbRPG.getInstance(), "creator_discord"), PersistentDataType.STRING,
                    discordOfCreator);
            loreList.add(rarityLore);
            item.setItemMeta(itemMeta);
            item.setLore(loreList);
            itemDataBase.set(path, item);
        }
        OrbRPG.getInstance().saveItemDatabase();
    }
}
