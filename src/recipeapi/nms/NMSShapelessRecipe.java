package recipeapi.nms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import recipeapi.api.recipes.Ingredient;
import recipeapi.api.recipes.ShapelessRecipe;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.ShapelessRecipes;
import net.minecraft.server.v1_8_R3.World;

public class NMSShapelessRecipe extends ShapelessRecipes {

	static List<ItemStack> getNMSIngredients(Ingredient[] ingredients) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (Ingredient ingr : ingredients) {
			list.add(Utils.toNMSItemStack(ingr));
		}
		return list;
	}

	public NMSShapelessRecipe(ShapelessRecipe apirecipe) {
		super(CraftItemStack.asNMSCopy(apirecipe.getResult()), getNMSIngredients(apirecipe.getIngredients()));
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
							itemstack.getItem() == ingredient.getItem() &&
							(ingredient.getData() == 32767 || itemstack.getData() == ingredient.getData()) &&
							Objects.equals(itemstack.getTag(), ingredient.getTag())
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

}
