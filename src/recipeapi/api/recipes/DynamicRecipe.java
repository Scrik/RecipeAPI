package recipeapi.api.recipes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

public abstract class DynamicRecipe implements CustomRecipe {

	public abstract boolean matches(Player player, ItemStack[] currentItemsInCrafting);

	public abstract ItemStack getResult(Player player, ItemStack[] currentItemsInCrafting);

	public abstract void preCraftInventoryModify(CraftingInventory inventory);

	@Override
	public String toString() {
		return "DynamicRecipe(Class: "+getClass().getName()+")";
	}

}
