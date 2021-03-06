package recipeapi.api.recipes;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

public final class ShapelessRecipe implements CustomRecipe {

	private ItemStack result;
	private Ingredient[] ingredients;

	public ShapelessRecipe(ItemStack result, Ingredient[] ingredients) {
		this.result = result;
		this.ingredients = ingredients;
	}

	public ItemStack getResult() {
		return result;
	}

	public Ingredient[] getIngredients() {
		return ingredients.clone();
	}

	@Override
	public String toString() {
		return "ShapelessRecipe(Result: "+result+", Ingredients: "+Arrays.toString(ingredients)+")";
	}

}
