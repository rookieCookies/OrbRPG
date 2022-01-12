package orbrpg.functions;

import net.kyori.adventure.text.Component;
import orbrpg.OrbRPG;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import utils.FileM;
import utils.Item;
import utils.Misc;
import utils.i.StringM;

import java.util.*;

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
            var itemMaterialString = itemsFile.getString(path + ".material", "stone").toUpperCase(Locale.ROOT);
            var itemMaterial = Material.matchMaterial(itemMaterialString);
            if (itemMaterial == null)
                itemMaterial = Material.STONE;
            var item = new ItemStack(itemMaterial);
            Item.hideAttributes(item);
            var itemMeta = item.getItemMeta();
            var data = itemMeta.getPersistentDataContainer();
            var itemRarity = itemsFile.getString(path + ".rarity", "default");
            String itemRarityWithPath = "rarities." + itemRarity;
            String rarityLore = FileM.getMessage(itemRarityWithPath + ".lore");
            String rarityColor = FileM.getMessage(itemRarityWithPath + ".color_id");
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
                    loreList.add(Misc.coloured("&7Damage: &c" + StringM.getFormattedNumber(damage1) + "━" + StringM.getFormattedNumber(damage2)));
                } else {
                    data.set(new NamespacedKey(OrbRPG.getInstance(), "damage"), PersistentDataType.FLOAT, baseDamage);
                    loreList.add(Misc.coloured("&7Damage: &c" + StringM.getFormattedNumber(baseDamage)));
                }
                loreList.add(" ");
            }
            item.setItemMeta(itemMeta);
            item = firstSection(item, path, loreList);

            // Adding lore
            List<String> lore = itemsFile.getStringList(path + ".lore");
            for (String str : lore)
                loreList.add(Misc.coloured(str));
            item = secondSection(item, path, loreList);
            itemMeta = item.getItemMeta();
            data = itemMeta.getPersistentDataContainer();

            // Creator data
            var creator = itemsFile.getString(path + ".info.creator", "Server");
            data.set(new NamespacedKey(OrbRPG.getInstance(), "creator"), PersistentDataType.STRING, creator);
            var discordOfCreator = itemsFile.getString(path + ".info.discord", "Server");
            data.set(new NamespacedKey(OrbRPG.getInstance(), "creator_discord"), PersistentDataType.STRING,
                    discordOfCreator);

            // Adding crystal data
            var crystalID = itemsFile.getString(path + ".crystal_id", "");
            if ("crystal".equals(type))
                data.set(new NamespacedKey(OrbRPG.getInstance(), "crystal_id"), PersistentDataType.STRING,
                        crystalID);
            var crystalCooldown = itemsFile.getLong(path + ".crystal_cooldown", itemsFile.getLong(path + ".cc", 1L));
            if ("crystal".equals(type)) {
                data.set(new NamespacedKey(OrbRPG.getInstance(), "crystal_cooldown"), PersistentDataType.LONG,
                        crystalCooldown);
                itemMeta.setDisplayName(Misc.coloured(itemMeta.getDisplayName() + String.format(" (%ss)", crystalCooldown / 20F)));
            }

            // Finishing touches
            loreList.add(rarityLore);
            data.set(new NamespacedKey(OrbRPG.getInstance(), "rarity"), PersistentDataType.STRING, itemRarity);
            itemMeta.setCustomModelData(itemsFile.getInt(path + ".custom_model_data", itemsFile.getInt(path + ".cmd", 0)));
            item.setItemMeta(itemMeta);
            item.setLore(loreList);

            // Coloring leather items
            if (item.getType() == Material.LEATHER_HELMET ||
                    item.getType() == Material.LEATHER_CHESTPLATE ||
                    item.getType() == Material.LEATHER_LEGGINGS ||
                    item.getType() == Material.LEATHER_BOOTS) {
                LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
                var r = itemsFile.getInt(path + ".color.r", 0);
                var g = itemsFile.getInt(path + ".color.g", 0);
                var b = itemsFile.getInt(path + ".color.b", 0);
                meta.setColor(Color.fromRGB(r, g, b));
                item.setItemMeta(meta);
            }
            var glowing = itemsFile.getBoolean(path + ".glowing");
            if (glowing)
                item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            itemDataBase.set(path, item);
        }
        OrbRPG.getInstance().saveItemDatabase();
    }
    ItemStack firstSection(ItemStack item, String path, List<String> loreList) {
        ConfigurationSection itemsFile = OrbRPG.getInstance().getItemsFile();
        var itemMeta = item.getItemMeta();
        var data = itemMeta.getPersistentDataContainer();
        var sectionOne = false;
        float health = (float) itemsFile.getDouble(path + ".stats.health");
        if (health != 0) {
            data.set(new NamespacedKey(OrbRPG.getInstance(), "health"), PersistentDataType.FLOAT, health);
            loreList.add(Misc.coloured("&7Health: &a" + StringM.getFormattedNumber(health)));
            sectionOne = true;
        }
        float defense = (float) itemsFile.getDouble(path + ".stats.defense");
        if (defense != 0) {
            data.set(new NamespacedKey(OrbRPG.getInstance(), "defense"), PersistentDataType.FLOAT, defense);
            loreList.add(Misc.coloured("&7Defense: &a" + StringM.getFormattedNumber(defense)));
            sectionOne = true;
        }
        var efficiency = itemsFile.getInt(path + ".stats.efficiency");
        if (efficiency != 0) {
            item.addUnsafeEnchantment(Enchantment.DIG_SPEED, efficiency);
            loreList.add(Misc.coloured("&7Break Power: &a" + StringM.getFormattedNumber(efficiency)));
            sectionOne = true;
        }
        float breakPower = (float) itemsFile.getDouble(path + ".stats.break_power");
        if (breakPower != 0) {
            data.set(new NamespacedKey(OrbRPG.getInstance(), "break_power"), PersistentDataType.FLOAT, breakPower);
            loreList.add(Misc.coloured("&7Break Power: &a" + StringM.getFormattedNumber(breakPower)));
            sectionOne = true;
        }
        if (sectionOne)
            loreList.add(" ");
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        return item;
    }
    ItemStack secondSection(ItemStack item, String path, List<String> loreList) {
        ConfigurationSection itemsFile = OrbRPG.getInstance().getItemsFile();
        var itemMeta = item.getItemMeta();
        var type = itemsFile.getString(path + ".type", "item");
        var data = itemMeta.getPersistentDataContainer();
        var sectionTwo = false;
        float cooldown = (float) itemsFile.getDouble(path + ".stats.cooldown");
        if (cooldown != 0 && "bow".equals(type)) {
            data.set(new NamespacedKey(OrbRPG.getInstance(), "bow_cooldown"), PersistentDataType.FLOAT, cooldown);
            loreList.add(Misc.coloured("&7Cooldown: &a" + StringM.getFormattedNumber(cooldown / 20) + "s"));
            sectionTwo = true;
        }
        float speed = (float) itemsFile.getDouble(path + ".stats.speed");
        if (speed != 0) {
            data.set(new NamespacedKey(OrbRPG.getInstance(), "speed"), PersistentDataType.FLOAT, speed);
            loreList.add(Misc.coloured("&7Speed: &a" + StringM.getFormattedNumber(speed)));
            sectionTwo = true;
        }
        float lifeSteal = (float) itemsFile.getDouble(path + ".stats.life_steal");
        if (lifeSteal != 0) {
            data.set(new NamespacedKey(OrbRPG.getInstance(), "life_steal"), PersistentDataType.FLOAT, lifeSteal);
            loreList.add(Misc.coloured("&7Life Steal: &a" + StringM.getFormattedNumber(lifeSteal)));
            sectionTwo = true;
        }
        float tex = (float) itemsFile.getDouble(path + ".stats.tex");
        if (tex != 0) {
            data.set(new NamespacedKey(OrbRPG.getInstance(), "tex"), PersistentDataType.FLOAT, tex);
            loreList.add(Misc.coloured("&7Tex: &a" + StringM.getFormattedNumber(tex)));
            sectionTwo = true;
        }
        if (sectionTwo)
            loreList.add(" ");
        itemMeta.setLore(loreList);
        item.setItemMeta(itemMeta);
        return item;
    }
}
