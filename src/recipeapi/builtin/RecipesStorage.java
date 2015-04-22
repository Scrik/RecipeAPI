package recipeapi.builtin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import recipeapi.RecipeAPI;
import recipeapi.api.recipes.CustomRecipe;
import recipeapi.api.recipes.Ingredient;
import recipeapi.api.recipes.ShapedRecipe;
import recipeapi.api.recipes.ShapelessRecipe;

public class RecipesStorage {

	private static final RecipesStorage instance = new RecipesStorage();

	public static RecipesStorage getInstance() {
		return instance;
	}

	private ArrayList<CustomRecipe> recipes = new ArrayList<CustomRecipe>();

	public void addRecipe(CustomRecipe recipe) {
		recipes.add(recipe);
	}

	public void removeRecipe(CustomRecipe recipe) {
		recipes.remove(recipe);
	}

	public List<CustomRecipe> getRecipes() {
		return Collections.unmodifiableList(recipes);
	}

	@SuppressWarnings("unchecked")
	public void load(RecipeAPI plugin) {
		File file = new File(plugin.getDataFolder(), "config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (String key : config.getKeys(false)) {
			String type = config.getString(key+".type");
			switch (type) {
				case "shapeless": {
					ItemStack result = config.getItemStack(key+".result");
					List<Ingredient> ingredients = (List<Ingredient>) config.getList(key+".ingredients");
					addRecipe(new ShapelessRecipe(result, ingredients.toArray(new Ingredient[0])));
					break;
				}
				case "shaped": {
					ItemStack result = config.getItemStack(key+".result");
					String[] shape = config.getStringList(key+".shape").toArray(new String[0]);
					ConfigurationSection section = config.getConfigurationSection(key+".ingredients");
					ShapedRecipe.Builder builder = ShapedRecipe.builder().setReuslt(result).setShape(shape);
					for (String characterstring : section.getKeys(false)) {
						builder.setIngredient(characterstring.charAt(0), (Ingredient) section.get(characterstring));
					}
					addRecipe(builder.build());
					break;
				}
			}
		}
	}

	public void save(RecipeAPI plugin) {
		File file = new File(plugin.getDataFolder(), "config.yml");
		YamlConfiguration config = new YamlConfiguration();
		int count = 1;
		for (CustomRecipe recipe : recipes) {
			if (recipe instanceof ShapelessRecipe) {
				config.set(count+".type", "shapeless");
				config.set(count+".result", ((ShapelessRecipe) recipe).getResult());
				config.set(count+".ingredients", Arrays.asList(((ShapelessRecipe) recipe).getIngredients()));
			} else
			if (recipe instanceof ShapedRecipe) {
				config.set(count+".type", "shaped");
				config.set(count+".result", ((ShapedRecipe) recipe).getResult());
				config.set(count+".shape", Arrays.asList(((ShapedRecipe) recipe).getShape()));
				for (Entry<Character, Ingredient> entry : ((ShapedRecipe) recipe).getIngredients().entrySet()) {
					config.set(count+".ingredients."+entry.getKey(), entry.getValue());
				}
			}
			count++;
		}
		try {
			config.save(file);
		} catch (IOException e) {
		}
	}

}
