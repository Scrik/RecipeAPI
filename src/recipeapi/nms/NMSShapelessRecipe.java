package recipeapi.nms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import recipeapi.api.recipes.Ingredient;
import recipeapi.api.recipes.ShapelessRecipe;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.ShapelessRecipes;
import net.minecraft.server.v1_8_R3.World;

public class NMSShapelessRecipe extends ShapelessRecipes implements CountAware {

	static List<ItemStack> getNMSIngredients(Ingredient[] ingredients) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (Ingredient ingr : ingredients) {
			list.add(Utils.toNMSItemStack(ingr));
		}
		return list;
	}

	private boolean hascount = false;

	public NMSShapelessRecipe(ShapelessRecipe apirecipe) {
		super(CraftItemStack.asNMSCopy(apirecipe.getResult()), getNMSIngredients(apirecipe.getIngredients()));
		for (ItemStack ingredient : getIngredients()) {
			if (ingredient != null && ingredient.count > 1) {
				hascount = true;
			}
		}
	}

	@Override
	public boolean a(final InventoryCrafting inventorycrafting, final World world) {
		final ArrayList<ItemStack> ingredients = new ArrayList<ItemStack>(this.getIngredients());
		for (int i = 0; i < inventorycrafting.h(); ++i) {
			for (int j = 0; j < inventorycrafting.i(); ++j) {
				final ItemStack itemstack = inventorycrafting.c(j, i);
				if (itemstack != null) {
					boolean isingredient = false;
					for (final ItemStack ingredient : ingredients) {
						if (
							CustomRecipeManager.isMatching(ingredient, itemstack) &&
							itemstack.count >= ingredient.count
						) {
							isingredient = true;
							ingredients.remove(ingredient);
							break;
						}
					}
					if (!isingredient) {
						return false;
					}
				}
			}
		}
		return ingredients.isEmpty();
	}

	@Override
	public void removeMoreItems(final InventoryCrafting inventorycrafting) {
		if (!hascount) {
			return;
		}
		for (ItemStack ingredient : this.getIngredients()) {
			if (ingredient.count > 1) {
				for (int slot = 0; slot < inventorycrafting.getSize(); slot++) {
					ItemStack itemstack = inventorycrafting.getItem(slot);
					if (itemstack != null) {
						if (CustomRecipeManager.isMatching(itemstack, ingredient)) {
							int toremove = ingredient.count - 1;
							if (itemstack.count > toremove) {
								itemstack.count -= toremove;
							} else {
								inventorycrafting.setItem(slot, null);
							}
							break;
						}
					}
				}
			}
		}
	}

}
