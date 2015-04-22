package recipeapi.builtin.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.ValidatingPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import recipeapi.RecipeAPI;
import recipeapi.api.recipes.CustomRecipe;
import recipeapi.api.recipes.Ingredient;
import recipeapi.api.recipes.ShapelessRecipe;
import recipeapi.api.registry.RecipeRegistry;
import recipeapi.builtin.RecipesStorage;

public class ShapelessRecipeConversation {

	public static Conversation create(final RecipeAPI plugin, Player player) {
		ConversationFactory factory = new ConversationFactory(plugin);
		HashMap<Object, Object> initialdata = new HashMap<>();
		initialdata.put("ingredients", new ArrayList<Ingredient>());
		factory
		.withEscapeSequence("cancel")
		.withInitialSessionData(initialdata)
		.withFirstPrompt(new RecipeCreatePromt())
		.withPrefix(new ConversationPrefix() {
			@Override
			public String getPrefix(ConversationContext ctx) {
				return ChatColor.YELLOW + "[" + plugin.getName() + "] ";
			}
		});
		return factory.buildConversation(player);
	}

	static ItemStack getItemInHand(Conversable conv) {
		return ((Player) conv).getItemInHand();
	}

	@SuppressWarnings("unchecked")
	static ArrayList<Ingredient> getIngredients(ConversationContext ctx) {
		return (ArrayList<Ingredient>) ctx.getSessionData("ingredients");
	}

	private static class RecipeCreatePromt extends ValidatingPrompt {

		@Override
		public String getPromptText(ConversationContext ctx) {
			return "Select itemstack you want to use as an result and type use, or cancel to cancel creating recipe";
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext ctx, String input) {
			ctx.getAllSessionData().put("result", getItemInHand(ctx.getForWhom()));
			return new RecipeAddIngredientPromt();
		}

		@Override
		protected boolean isInputValid(ConversationContext ctx, String input) {
			return input.equalsIgnoreCase("use"); 
		}

	}

	private static class RecipeAddIngredientPromt extends ValidatingPrompt {

		@Override
		public String getPromptText(ConversationContext arg0) {
			return "Select itemstack you want to use as an ingredient and type use or wuse (same as use, but ingredient ignores data), finish for finish creating the recipe, or cancel to can cancel creating recipe";
		}

		@Override
		protected Prompt acceptValidatedInput(ConversationContext ctx, String input) {
			switch (input.toLowerCase()) {
				case "use": {
					getIngredients(ctx).add(Ingredient.fromItemStack(getItemInHand(ctx.getForWhom()), false));
					return this;
				}
				case "wuse": {
					getIngredients(ctx).add(Ingredient.fromItemStack(getItemInHand(ctx.getForWhom()), true));
					return this;
				}
				case "finish": {
					CustomRecipe recipe = new ShapelessRecipe((ItemStack) ctx.getSessionData("result"), getIngredients(ctx).toArray(new Ingredient[0]));
					RecipesStorage.getInstance().addRecipe(recipe);
					RecipeRegistry.register(recipe);
					ctx.getForWhom().sendRawMessage(ChatColor.YELLOW + "Recipe created and registerd");
					return END_OF_CONVERSATION;
				}
				default: {
					return this;
				}
			}
		}

		@Override
		protected boolean isInputValid(ConversationContext ctx, String input) {
			switch (input.toLowerCase()) {
				case "use":
				case "wuse": {
					return true;
				}
				case "finish": {
					return !getIngredients(ctx).isEmpty();
				}
				default: {
					return false;
				}
			}
		}

	}

}
