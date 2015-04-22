package recipeapi.api.recipes;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public final class ShapedRecipe implements CustomRecipe {

	public static Builder builder() {
		return new Builder();
	}

	private ItemStack result;
	private String[] shape;
	private HashMap<Character, Ingredient> ingredients;

	private ShapedRecipe(ItemStack result, String[] shape, HashMap<Character, Ingredient> ingredients) {
		this.result = result;
		this.shape = shape;
		this.ingredients = ingredients;
	}

	public ItemStack getResult() {
		return result;
	}

	public String[] getShape() {
		return shape.clone();
	}

	public Map<Character, Ingredient> getIngredients() {
		return Collections.unmodifiableMap(ingredients);
	}

	public static class Builder {

		private Builder() {
		}

		private ItemStack result;
		private String[] shape;
		private HashMap<Character, Ingredient> ingredients = new HashMap<Character, Ingredient>();

		public Builder setReuslt(ItemStack result) {
			this.result = result;
			return this;
		}

		public Builder setShape(String... rows) {
			shape = rows.clone();
			return this;
		}

		public Builder setIngredient(char shapeletter, Ingredient ingredient) {
			ingredients.put(shapeletter, ingredient);
			return this;
		}

		public boolean hasIngredients() {
			return !ingredients.isEmpty();
		}

		public ShapedRecipe build() {
			return new ShapedRecipe(result, shape, ingredients);
		}

	}

	@Override
	public String toString() {
		return "ShapedRecipe(Result: "+result+", Shape: "+Arrays.toString(shape)+", Ingredients: "+ingredients+")";
	}

}
