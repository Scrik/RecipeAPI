package recipeapi.nms;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R2.event.CraftEventFactory;

import net.minecraft.server.v1_8_R2.CraftingManager;
import net.minecraft.server.v1_8_R2.IRecipe;
import net.minecraft.server.v1_8_R2.InventoryCrafting;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.World;

public class CustomRecipeManager extends CraftingManager {

	public static void inject() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipes();
		Utils.setStaticFinalField(CraftingManager.class.getDeclaredField("a"), CustomRecipeManager.getInstance());
		CustomRecipeManager.getInstance().recipes = recipes;
	}

	private static CustomRecipeManager instance = new CustomRecipeManager();

	public static CustomRecipeManager getInstance() {
		return instance;
	}

	private ArrayList<IRecipe> additionalRecipes = new ArrayList<IRecipe>();

	public void addRecipe(IRecipe recipe) {
		additionalRecipes.add(recipe);
	}

	public void removeRecipe(IRecipe recipe) {
		additionalRecipes.remove(recipe);
	}

	@Override
	public ItemStack craft(InventoryCrafting inventorycrafting, World world) {
		for (IRecipe recipe : this.recipes) {
			if (recipe.a(inventorycrafting, world)) {
				inventorycrafting.currentRecipe = recipe;
				ItemStack result = recipe.a(inventorycrafting);
				return CraftEventFactory.callPreCraftEvent(inventorycrafting, result, this.lastCraftView, false);
			}
		}
		for (IRecipe recipe : this.additionalRecipes) {
			if (recipe.a(inventorycrafting, world)) {
				inventorycrafting.currentRecipe = recipe;
				ItemStack result = recipe.a(inventorycrafting);
				return CraftEventFactory.callPreCraftEvent(inventorycrafting, result, this.lastCraftView, false);
			}
		}
		inventorycrafting.currentRecipe = null;
		return null;
	}

	@Override
	public ItemStack[] b(final InventoryCrafting inventorycrafting, final World world) {
		for (final IRecipe irecipe : this.recipes) {
			if (irecipe.a(inventorycrafting, world)) {
				return irecipe.b(inventorycrafting);
			}
		}
		for (final IRecipe irecipe : this.additionalRecipes) {
			if (irecipe.a(inventorycrafting, world)) {
				return irecipe.b(inventorycrafting);
			}
		}
		final ItemStack[] aitemstack = new ItemStack[inventorycrafting.getSize()];
		for (int i = 0; i < aitemstack.length; ++i) {
			aitemstack[i] = inventorycrafting.getItem(i);
		}
		return aitemstack;
	}

}
