package orbrpg.functions;

import net.kyori.adventure.text.Component;
import orbrpg.Misc;
import orbrpg.OrbRPG;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RegisterItems {
    public RegisterItems() { run(); }
    public void run() {
        OrbRPG instance = OrbRPG.getInstance();
        ConfigurationSection itemDataBase = instance.getItemDatabase();
        ConfigurationSection itemsFile = instance.getItemsFile();
        Map<String, Object> sec = instance.getItemsFile().getValues(false);
        for (Object a : sec.values()){
            String path = a.toString();
            path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
            boolean isEnabled = itemsFile.getBoolean(path + ".enabled");
            if (!isEnabled)
                continue;
            String itemMaterialString = itemsFile.getString(path + ".material");
            if (itemMaterialString == null)
                itemMaterialString = "stone";
            Material itemMaterial = Material.matchMaterial(itemMaterialString);
            assert itemMaterial != null;
            ItemStack item = new ItemStack(itemMaterial);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
            itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            itemMeta.setUnbreakable(true);
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            String itemRarity = itemsFile.getString(path + ".rarity");
            String itemRarityWithPath = "rarities." + itemRarity;
            if ("".equals(Misc.getMessage(itemRarityWithPath + ".lore"))) {
                itemRarity = "default";
                itemRarityWithPath = "rarities." + itemRarity;
            }
            String rarityLore = Misc.getMessage(itemRarityWithPath + ".lore");
            String rarityColor = Misc.getMessage(itemRarityWithPath + ".color_id");
            String displayName = Misc.coloured(rarityColor + itemsFile.getString(path + ".display_name"));
            String type = itemsFile.getString(path + ".type", "item");
            data.set(new NamespacedKey(OrbRPG.getInstance(), "item_id"), PersistentDataType.STRING, path);
            data.set(new NamespacedKey(OrbRPG.getInstance(), "item_type"), PersistentDataType.STRING, type);
            itemMeta.displayName(Component.text(displayName));
            float baseDamage = (float) itemsFile.getDouble(path + ".stats.base_damage");
            boolean isWeapon = Objects.equals(type, "weapon");
            List<String> loreList = new ArrayList<>();
            if (baseDamage != 0) {
                if (isWeapon) {
                    double damageDifference = itemsFile.getDouble(path + ".stats.difference");
                    float damage1 = (float) (baseDamage * (1 - damageDifference));
                    float damage2 = (float) (baseDamage * (1 + damageDifference));
                    NamespacedKey weaponDamage1 = new NamespacedKey(instance, "weapon_damage_1");
                    NamespacedKey weaponDamage2 = new NamespacedKey(instance, "weapon_damage_2");
                    data.set(weaponDamage1, PersistentDataType.FLOAT, damage1);
                    data.set(weaponDamage2, PersistentDataType.FLOAT, damage2);
                    loreList.add(Misc.coloured("&7Damage: &c" + damage1 + "↔" + damage2));
                } else {
                    data.set(new NamespacedKey(OrbRPG.getInstance(), "damage"), PersistentDataType.FLOAT, baseDamage);
                    loreList.add(Misc.coloured("&7Damage: &c" + baseDamage));
                }
                loreList.add(" ");
            }
            boolean sectionOne = false;
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

            boolean sectionTwo = false;
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
            String creator = itemsFile.getString(path + ".info.creator", "Server");
            data.set(new NamespacedKey(OrbRPG.getInstance(), "creator"), PersistentDataType.STRING, creator);
            String discordOfCreator = itemsFile.getString(path + ".info.discord", "Server");
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
