package recipeapi.api.recipes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class DynamicRecipe implements CustomRecipe {

	public abstract boolean canCraft(Player player, ItemStack[] currentItemsInCrafting);

	public abstract ItemStack getResult(Player player, ItemStack[] currentItemsInCrafting);

}