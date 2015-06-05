package recipeapi.api.recipes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Ingredient implements ConfigurationSerializable {

	public static Ingredient fromItemStack(ItemStack itemstack, boolean isWildCard) {
		return new Ingredient(itemstack, isWildCard);
	}

	private Material material;
	private int data;
	private int amount;
	private ItemMeta itemmeta;

	private Ingredient(ItemStack itemstack, boolean isWildCard) {
		this.material = itemstack.getType();
		this.data = isWildCard ? Short.MAX_VALUE : itemstack.getDurability();
		this.amount = itemstack.getAmount();
		if (amount <= 0) {
			amount = 1;
		}
		this.itemmeta = Bukkit.getItemFactory().asMetaFor(itemstack.getItemMeta(), material);
	}

	public Material getMaterial() {
		return material;
	}

	public boolean isWildcardData() {
		return data == Short.MAX_VALUE;
	}

	public int getData() {
		return data;
	}

	public int getAmount() {
		return amount;
	}

	public ItemMeta getItemMeta() {
		return itemmeta;
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("material", getMaterial().toString());
		map.put("wildcard", isWildcardData());
		map.put("data", getData());
		map.put("amount", getAmount());
		map.put("meta", getItemMeta());
		return map;
	}

	public static Ingredient deserialize(Map<String, Object> map) {
		Material material = Material.getMaterial((String) map.get("material"));
		boolean isWildCard = (boolean) map.get("wildcard");
		int data = (int) map.get("data");
		int amount = (int) map.get("amount");
		ItemMeta itemmeta = (ItemMeta) map.get("meta");
		ItemStack itemstack = new ItemStack(material, amount, (short) data);
		itemstack.setItemMeta(itemmeta);
		return new Ingredient(itemstack, isWildCard);
	}

	@Override
	public String toString() {
		return "Ingredient(Material: "+getMaterial()+", Data: "+getData()+", Meta: "+getItemMeta()+")";
	}

}