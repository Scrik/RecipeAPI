package recipeapi.nms;

import java.util.Collections;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;

import recipeapi.api.recipes.DynamicRecipe;
import net.minecraft.server.v1_8_R2.IRecipe;
import net.minecraft.server.v1_8_R2.InventoryCrafting;
import net.minecraft.server.v1_8_R2.ItemStack;
import net.minecraft.server.v1_8_R2.World;

public class NMSDynamicRecipe implements IRecipe {

	private DynamicRecipe apirecipe;

	public NMSDynamicRecipe(DynamicRecipe apirecipe) {
		this.apirecipe = apirecipe;
	}

	@Override
	public int a() {
		return 10;
	}

	@Override
	public ItemStack a(InventoryCrafting inv) {
		org.bukkit.inventory.ItemStack[] items = new org.bukkit.inventory.ItemStack[inv.getSize()];
		for (int i = 0; i < inv.getSize(); i++) {
			items[i] = CraftItemStack.asCraftMirror(inv.getItem(i));
		}
		return CraftItemStack.asNMSCopy(apirecipe.getResult((Player) inv.getOwner(), items));
	}

	@Override
	public boolean a(InventoryCrafting inv, World world) {
		org.bukkit.inventory.ItemStack[] items = new org.bukkit.inventory.ItemStack[inv.getSize()];
		for (int i = 0; i < inv.getSize(); i++) {
			items[i] = CraftItemStack.asCraftMirror(inv.getItem(i));
		}
		return apirecipe.matches((Player) inv.getOwner(), items);
	}

	@Override
	public ItemStack b() {
		return null;
	}

	@Override
	public ItemStack[] b(InventoryCrafting inv) {
		ItemStack[] aitemstack = new ItemStack[inv.getSize()];
		for (int i = 0; i < aitemstack.length; ++i) {
			ItemStack itemstack = inv.getItem(i);
			if (itemstack != null && itemstack.getItem().r()) {
				aitemstack[i] = new ItemStack(itemstack.getItem().q());
			}
		}
		return aitemstack;
	}

	@Override
	public List<ItemStack> getIngredients() {
		return Collections.emptyList();
	}

	@Override
	public Recipe toBukkitRecipe() {
		return new Recipe() {
			@Override
			public org.bukkit.inventory.ItemStack getResult() {
				return null;
			}
		};
	}

}
