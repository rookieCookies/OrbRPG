package orbrpg.functions;

import net.kyori.adventure.key.Keyed;
import orbrpg.OrbRPG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.jetbrains.annotations.NotNull;
import utils.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterRecipes {
    public RegisterRecipes() { run(); }
    public void run() {
        var instance = OrbRPG.getInstance();
        ConfigurationSection recipesFile = instance.getRecipesFile();
        Map<String, Object> sec = instance.getRecipesFile().getValues(false);
        var recipeIterator = Bukkit.recipeIterator();
        List<NamespacedKey> l = new ArrayList<>();
        while (recipeIterator.hasNext()) {
            var recipe = recipeIterator.next();
            if (!(recipe instanceof Keyed keyedRecipe))
                continue;
            if ("minecraft".equals(keyedRecipe.key().namespace()))
                l.add((NamespacedKey) keyedRecipe.key());
        }
        for (NamespacedKey key : l)
            Bukkit.removeRecipe(key);
        for (Object a : sec.values()) {
            var path = a.toString();
            path = path.substring(path.indexOf("path='") + 6, path.indexOf("', root="));
            if (!recipesFile.getBoolean(path + ".enabled"))
                continue;
            var resultItemID = recipesFile.getString(path + ".result", path);
            var shapeless = recipesFile.getBoolean(path + ".shapeless", false);
            ItemStack item = Item.getItem(resultItemID);
            if ("air".equals(resultItemID))
                item = new ItemStack(Material.AIR);
            Recipe recipe;
            if (shapeless)
                recipe = registerShapelessRecipe(path, item);
            else recipe = registerShapedRecipe(path, item);
            // Finally, add the recipe to the bukkit recipes
            Bukkit.addRecipe(recipe);
        }
    }
    Recipe registerShapedRecipe(String path, @NotNull ItemStack item) {
        ConfigurationSection recipesFile = OrbRPG.getInstance().getRecipesFile();
        var key = new NamespacedKey(OrbRPG.getInstance(), path);
        item.setAmount(recipesFile.getInt(path + ".result_amount", 0));
        var recipe = new ShapedRecipe(key, item);
        recipe.shape("ABC", "DEF", "GHJ");
        var str = "ABCDEFGHJ";
        for (var i = 1; i < 10; i++) {
            var ingItemID = recipesFile.getString(path + ".recipe." + i);
            var ingItem = Item.getItem(ingItemID);
            if ("air".equals(ingItemID))
                ingItem = new ItemStack(Material.AIR);
            recipe.setIngredient(str.charAt(i - 1), ingItem);
        }
        return recipe;
    }
    Recipe registerShapelessRecipe(String path, ItemStack item) {
        ConfigurationSection recipesFile = OrbRPG.getInstance().getRecipesFile();
        item.setAmount(recipesFile.getInt(path + ".result_amount", 0));
        var key = new NamespacedKey(OrbRPG.getInstance(), path);
        var recipe = new ShapelessRecipe(key, item);
        for (var i = 1; i < 10; i++) {
            var ingItemID = recipesFile.getString(path + ".recipe." + i);
            if ("air".equals(ingItemID))
                continue;
            if (ingItemID == null) {
                var message = String.format("Item missing in recipes! %s > recipe > " + i, path);
                OrbRPG.getInstance().getLogger().info(message);
            }
            else {
                var ingItem = Item.getItem(ingItemID);
                recipe.addIngredient(ingItem);
            }
        }
        return recipe;
    }
}
