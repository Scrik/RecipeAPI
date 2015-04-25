package recipeapi;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import recipeapi.api.recipes.CustomRecipe;
import recipeapi.api.recipes.Ingredient;
import recipeapi.api.registry.RecipeRegistry;
import recipeapi.builtin.RecipesStorage;
import recipeapi.builtin.commands.Commands;
import recipeapi.nms.CustomRecipeManager;

public class RecipeAPI extends JavaPlugin {

	@Override
	public void onEnable() {
		try {
			CustomRecipeManager.inject();
		} catch (Throwable t) {
			getLogger().log(Level.SEVERE, "Failed to inject CustomRecipeManager");
			t.printStackTrace();
			Bukkit.shutdown();
		}
		ConfigurationSerialization.registerClass(Ingredient.class);
		RecipesStorage.getInstance().load(this);
		for (CustomRecipe recipe : RecipesStorage.getInstance().getRecipes()) {
			RecipeRegistry.register(recipe);
		}
		getCommand("recipeapi").setExecutor(new Commands(this));
	}

	@Override
	public void onDisable() {
		RecipesStorage.getInstance().save(this);
		ConfigurationSerialization.unregisterClass(Ingredient.class);
		try {
			CustomRecipeManager.uninject();
		} catch (Throwable t) {
			getLogger().log(Level.SEVERE, "Failed to uninject CustomRecipeManager");
			t.printStackTrace();
			Bukkit.shutdown();
		}
	}

}
