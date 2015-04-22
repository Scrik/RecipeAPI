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
		if (isWildCard) {
			return new Ingredient(itemstack.getType(), itemstack.getItemMeta());
		} else {
			return new Ingredient(itemstack.getType(), itemstack.getDurability(), itemstack.getItemMeta());
		}
	}

	private Material material;
	private int data;
	private ItemMeta itemmeta;

	public Ingredient(Material material, ItemMeta itemmeta) {
		this.material = material;
		this.data = Short.MAX_VALUE;
		this.itemmeta = Bukkit.getItemFactory().asMetaFor(itemmeta, material);
	}

	public Ingredient(Material material, int data, ItemMeta itemmeta) {
		this(material, itemmeta);
		this.data = data;
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

	public ItemMeta getItemMeta() {
		return itemmeta;
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("material", getMaterial().toString());
		map.put("wildcard", isWildcardData());
		map.put("data", getData());
		map.put("meta", getItemMeta());
		return map;
	}

	public static Ingredient deserialize(Map<String, Object> map) {
		Material material = Material.getMaterial((String) map.get("material"));
		boolean isWildCard = (boolean) map.get("wildcard");
		int data = (int) map.get("data");
		ItemMeta itemmeta = (ItemMeta) map.get("meta");
		return isWildCard ? new Ingredient(material, itemmeta) : new Ingredient(material, data, itemmeta);
	}

}