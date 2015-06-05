package recipeapi.nms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;

import net.minecraft.server.v1_8_R3.CraftingManager;
import net.minecraft.server.v1_8_R3.IRecipe;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.World;

public class CustomRecipeManager extends CraftingManager {

	public static void inject() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipes();
		Utils.setStaticFinalField(CraftingManager.class.getDeclaredField("a"), CustomRecipeManager.getInstance());
		CustomRecipeManager.getInstance().recipes = recipes;
	}

	public static void uninject() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		List<IRecipe> recipes = CustomRecipeManager.getInstance().getRecipes();
		Utils.setStaticFinalField(CraftingManager.class.getDeclaredField("a"), new CraftingManager());
		CraftingManager.getInstance().recipes = recipes;
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
		IRecipe currentRecipe = inventorycrafting.currentRecipe;
		if (currentRecipe != null && currentRecipe.a(inventorycrafting, world)) {
			if (currentRecipe instanceof CountAware) {
				((CountAware) currentRecipe).removeMoreItems(inventorycrafting);
			}
			return inventorycrafting.currentRecipe.b(inventorycrafting);
		}
		for (final IRecipe irecipe : this.recipes) {
			if (irecipe.a(inventorycrafting, world)) {
				return irecipe.b(inventorycrafting);
			}
		}
		for (final IRecipe irecipe : this.additionalRecipes) {
			if (irecipe.a(inventorycrafting, world)) {
				if (irecipe instanceof CountAware) {
					((CountAware) irecipe).removeMoreItems(inventorycrafting);
				}
				return irecipe.b(inventorycrafting);
			}
		}
		final ItemStack[] aitemstack = new ItemStack[inventorycrafting.getSize()];
		for (int i = 0; i < aitemstack.length; ++i) {
			aitemstack[i] = inventorycrafting.getItem(i);
		}
		return aitemstack;
	}

	public static boolean isMatching(ItemStack ingredient, ItemStack itemstack) {
		if (ingredient == null && itemstack == null) {
			return true;
		}
		if (ingredient == null || itemstack == null) {
			return false;
		}
		return 	
		itemstack.getItem() == ingredient.getItem() &&
		(ingredient.getData() == 32767 || itemstack.getData() == ingredient.getData()) &&
		Objects.equals(itemstack.getTag(), ingredient.getTag());
	}

}
