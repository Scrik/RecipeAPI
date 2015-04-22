package recipeapi.nms;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;

import net.minecraft.server.v1_8_R2.ItemStack;
import recipeapi.api.recipes.Ingredient;

public class Utils {

	public static ItemStack toNMSItemStack(Ingredient ingr) {
		if (ingr == null) {
			return null;
		}
		org.bukkit.inventory.ItemStack bitemstack = new org.bukkit.inventory.ItemStack(ingr.getMaterial());
		bitemstack.setItemMeta(ingr.getItemMeta());
		ItemStack nmsstack = CraftItemStack.asNMSCopy(bitemstack);
		nmsstack.setData(ingr.getData());
		return nmsstack;
	}

	public static <T extends AccessibleObject> T setAccessible(T object) {
		object.setAccessible(true);
		return object;
	}

	public static void setStaticFinalField(Field field, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		setAccessible(Field.class.getDeclaredField("modifiers")).setInt(field, field.getModifiers() & ~Modifier.FINAL);
		setAccessible(Field.class.getDeclaredField("root")).set(field, null);
		setAccessible(Field.class.getDeclaredField("overrideFieldAccessor")).set(field, null);
		setAccessible(field).set(null, newValue);
	}

}
