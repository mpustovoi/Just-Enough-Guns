package ttv.alanorMiga.jeg.crafting;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;

/**
 * Author: MrCrayfish
 */
public class GunniteWorkbenchRecipeSerializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<GunniteWorkbenchRecipe>
{
    @Override
    public GunniteWorkbenchRecipe fromJson(ResourceLocation recipeId, JsonObject parent)
    {
        ImmutableList.Builder<GunniteWorkbenchIngredient> builder = ImmutableList.builder();
        JsonArray input = GsonHelper.getAsJsonArray(parent, "materials");
        for(int i = 0; i < input.size(); i++)
        {
            JsonObject object = input.get(i).getAsJsonObject();
            builder.add(GunniteWorkbenchIngredient.fromJson(object));
        }
        if(!parent.has("result"))
        {
            throw new JsonSyntaxException("Missing result item entry");
        }
        JsonObject resultObject = GsonHelper.getAsJsonObject(parent, "result");
        ItemStack resultItem = ShapedRecipe.itemStackFromJson(resultObject);
        return new GunniteWorkbenchRecipe(recipeId, resultItem, builder.build());
    }

    @Nullable
    @Override
    public GunniteWorkbenchRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer)
    {
        ItemStack result = buffer.readItem();
        ImmutableList.Builder<GunniteWorkbenchIngredient> builder = ImmutableList.builder();
        int size = buffer.readVarInt();
        for(int i = 0; i < size; i++)
        {
            builder.add((GunniteWorkbenchIngredient) Ingredient.fromNetwork(buffer));
        }
        return new GunniteWorkbenchRecipe(recipeId, result, builder.build());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, GunniteWorkbenchRecipe recipe)
    {
        buffer.writeItem(recipe.getItem());
        buffer.writeVarInt(recipe.getMaterials().size());
        for(GunniteWorkbenchIngredient ingredient : recipe.getMaterials())
        {
            ingredient.toNetwork(buffer);
        }
    }
}
