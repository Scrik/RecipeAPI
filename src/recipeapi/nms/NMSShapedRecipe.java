package recipeapi.nms;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;

import recipeapi.api.recipes.Ingredient;
import recipeapi.api.recipes.ShapedRecipe;
import net.minecraft.server.v1_8_R2.InventoryCrafting;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.ShapedRecipes;
import net.minecraft.server.v1_8_R2.World;

public class NMSShapedRecipe extends ShapedRecipes {

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
	}

	@Override
	public boolean a(final InventoryCrafting inventorycrafting, final World world) {
		for (int i = 0; i <= 3 - this.width; ++i) {
			for (int j = 0; j <= 3 - this.height; ++j) {
				if (this.a(inventorycrafting, i, j, true)) {
					return true;
				}
				if (this.a(inventorycrafting, i, j, false)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean a(final InventoryCrafting inventorycrafting, final int i, final int j, final boolean flag) {
		for (int k = 0; k < 3; ++k) {
			for (int l = 0; l < 3; ++l) {
				final int i2 = k - i;
				final int j2 = l - j;
				ItemStack ingredient = null;
				if (i2 >= 0 && j2 >= 0 && i2 < this.width && j2 < this.height) {
					if (flag) {
						ingredient = this.items[this.width - i2 - 1 + j2 * this.width];
					} else {
						ingredient = this.items[i2 + j2 * this.width];
					}
				}
				final ItemStack itemstack = inventorycrafting.c(k, l);
				if (itemstack != null || ingredient != null) {
					if ((itemstack == null && ingredient != null) || (itemstack != null && ingredient == null)) {
						return false;
					}
					if (ingredient.getItem() != itemstack.getItem()) {
						return false;
					}
					if (ingredient.getData() != 32767 && ingredient.getData() != itemstack.getData()) {
						return false;
					}
					if (!Objects.equals(ingredient.getTag(), itemstack.getTag())) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
