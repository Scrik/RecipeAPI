package recipeapi.api.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import net.minecraft.server.v1_8_R3.IRecipe;
import recipeapi.api.recipes.DynamicRecipe;
import recipeapi.api.recipes.CustomRecipe;
import recipeapi.api.recipes.ShapedRecipe;
import recipeapi.api.recipes.ShapelessRecipe;
import recipeapi.nms.CustomRecipeManager;
import recipeapi.nms.NMSDynamicRecipe;
import recipeapi.nms.NMSShapedRecipe;
import recipeapi.nms.NMSShapelessRecipe;

public class RecipeRegistry {

	private static final HashMap<CustomRecipe, IRecipe> recipes = new HashMap<CustomRecipe, IRecipe>();

	public static void register(CustomRecipe recipe) {
		IRecipe nmsrecipe = null;
		if (recipe instanceof DynamicRecipe) {
			nmsrecipe = new NMSDynamicRecipe((DynamicRecipe) recipe);
		} else
		if (recipe instanceof ShapelessRecipe) {
			nmsrecipe = new NMSShapelessRecipe((ShapelessRecipe) recipe);
		} else
		if (recipe instanceof ShapedRecipe) {
			nmsrecipe = new NMSShapedRecipe((ShapedRecipe) recipe);
		}
		if (nmsrecipe != null) {
			CustomRecipeManager.getInstance().addRecipe(nmsrecipe);
			recipes.put(recipe, nmsrecipe);
		} else {
			throw new IllegalArgumentException("Don't know how to register "+recipe.getClass());
		}
	}

	public static void unregister(CustomRecipe recipe) {
		IRecipe nmsrecipe = recipes.get(recipe);
		if (nmsrecipe != null) {
			CustomRecipeManager.getInstance().removeRecipe(nmsrecipe);
		}
	}

	public static boolean isRegistered(CustomRecipe recipe) {
		return recipes.containsKey(recipe);
	}

	public static Set<CustomRecipe> getRecipes() {
		return Collections.unmodifiableSet(recipes.keySet());
	}

}
