package recipeapi.nms;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import recipeapi.api.recipes.Ingredient;
import recipeapi.api.recipes.ShapedRecipe;
import net.minecraft.server.v1_8_R3.InventoryCrafting;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.ShapedRecipes;
import net.minecraft.server.v1_8_R3.World;

public class NMSShapedRecipe extends ShapedRecipes implements CountAware {

	static ItemStack[] getNMSIngredients(String[] shape, Map<Character, Ingredient> map) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for (String shapestring : shape) {
			for (char shapechar : shapestring.toCharArray()) {
				list.add(Utils.toNMSItemStack(map.get(shapechar)));
			}
		}
		return list.toArray(new ItemStack[list.size()]);
	}

	private int width;
	private int height;
	private ItemStack[] items;

	private boolean hascount = false;

	public NMSShapedRecipe(ShapedRecipe apirecipe) {
		super(
			apirecipe.getShape()[0].length(),
			apirecipe.getShape().length,
			getNMSIngredients(apirecipe.getShape(), apirecipe.getIngredients()),
			CraftItemStack.asNMSCopy(apirecipe.getResult())
		);
		this.width = apirecipe.getShape()[0].length();
		this.height = apirecipe.getShape().length;
		this.items = this.getIngredients().toArray(new ItemStack[0]);
		for (ItemStack ingredient : items) {
			if (ingredient != null && ingredient.count > 1) {
				hascount = true;
			}
		}
	}

	@Override
	public boolean a(final InventoryCrafting inventorycrafting, final World world) {
		for (int column = 0; column <= 3 - this.width; ++column) {
			for (int row = 0; row <= 3 - this.height; ++row) {
				if (this.a(inventorycrafting, column, row, true)) {
					return true;
				}
				if (this.a(inventorycrafting, column, row, false)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean a(final InventoryCrafting inventorycrafting, final int columnshift, final int rowshift, final boolean leftToRight) {
		for (int invColumn = 0; invColumn < 3; ++invColumn) {
			for (int invRow = 0; invRow < 3; ++invRow) {
				final int column = invColumn - columnshift;
				final int row = invRow - rowshift;
				ItemStack ingredient = null;
				if (column >= 0 && row >= 0 && column < this.width && row < this.height) {
					if (leftToRight) {
						ingredient = this.items[this.width - column - 1 + row * this.width];
					} else {
						ingredient = this.items[column + row * this.width];
					}
				}
				final ItemStack itemstack = inventorycrafting.c(invColumn, invRow);
				if (itemstack != null || ingredient != null) {
					if (!CustomRecipeManager.isMatching(ingredient, itemstack)) {
						return false;
					}
					if (itemstack.count < ingredient.count) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void removeMoreItems(final InventoryCrafting inventorycrafting) {
		if (!hascount) {
			return;
		}
		for (int column = 0; column <= 3 - this.width; ++column) {
			for (int row = 0; row <= 3 - this.height; ++row) {
				if (this.a(inventorycrafting, column, row, true)) {
					removeItems(inventorycrafting, column, row, true);
					return;
				}
				if (this.a(inventorycrafting, column, row, false)) {
					removeItems(inventorycrafting, column, row, false);
					return;
				}
			}
		}
	}

	private void removeItems(final InventoryCrafting inventorycrafting, final int columnShift, final int rowShift, final boolean leftToRight) {
		for (int invColumn = 0; invColumn < 3; ++invColumn) {
			for (int invRow = 0; invRow < 3; ++invRow) {
				final int column = invColumn - columnShift;
				final int row = invRow - rowShift;
				ItemStack ingredient = null;
				if (column >= 0 && row >= 0 && column < this.width && row < this.height) {
					if (leftToRight) {
						ingredient = this.items[this.width - column - 1 + row * this.width];
					} else {
						ingredient = this.items[column + row * this.width];
					}
				}
				if (ingredient == null) {
					continue;
				}
				final ItemStack itemstack = inventorycrafting.c(invColumn, invRow);
				int toremove = ingredient.count - 1;
				if (itemstack.count > toremove) {
					itemstack.count -= toremove;
				} else {
					inventorycrafting.setItem(invColumn + invRow * inventorycrafting.i(), null);
				}
			}
		}
	}

}
