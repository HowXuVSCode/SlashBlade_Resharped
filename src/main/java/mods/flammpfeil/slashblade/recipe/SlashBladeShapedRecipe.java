package mods.flammpfeil.slashblade.recipe;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class SlashBladeShapedRecipe extends ShapedRecipe {
    // TODO: 处理这边的BUG
    public static final RecipeSerializer<SlashBladeShapedRecipe> SERIALIZER = new SlashBladeShapedRecipeSerializer<>(
            RecipeSerializer.SHAPED_RECIPE, SlashBladeShapedRecipe::new);

    private final ResourceLocation outputBlade;

    public SlashBladeShapedRecipe(ShapedRecipe compose, ResourceLocation outputBlade) {
        super(compose.getId(), compose.getGroup(), compose.category(), compose.getWidth(), compose.getHeight(),
                compose.getIngredients(), getResultBlade(outputBlade));
        this.outputBlade = outputBlade;
    }

    private static ItemStack getResultBlade(ResourceLocation outputBlade) {
        Item bladeItem = ForgeRegistries.ITEMS.containsKey(outputBlade) ? ForgeRegistries.ITEMS.getValue(outputBlade)
                : SBItems.slashblade;

        return bladeItem.getDefaultInstance();
    }

    public ResourceLocation getOutputBlade() {
        return outputBlade;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess access) {
        ItemStack result = SlashBladeShapedRecipe.getResultBlade(this.getOutputBlade());

        if (!ForgeRegistries.ITEMS.getKey(result.getItem()).equals(getOutputBlade())) {
            result = SlashBlade.getSlashBladeDefinitionRegistry(access).get(getOutputBlade())
                    .getBlade(result.getItem());
        }

        return result;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess access) {
        var result = this.getResultItem(access);
        if (!(result.getItem() instanceof ItemSlashBlade)) {

            result = new ItemStack(SBItems.slashblade);
        }
        var resultState = result.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);
        for (var stack : container.getItems()) {
            if (!(stack.getItem() instanceof ItemSlashBlade))
                continue;
            var ingredientState = stack.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);

            resultState.setProudSoulCount(resultState.getProudSoulCount() + ingredientState.getProudSoulCount());
            resultState.setKillCount(resultState.getKillCount() + ingredientState.getKillCount());
            resultState.setRefine(resultState.getRefine() + ingredientState.getRefine());
            updateEnchantment(result, stack);
        }

        return result;
    }

    private void updateEnchantment(ItemStack result, ItemStack ingredient) {
        var newItemEnchants = result.getAllEnchantments();
        var oldItemEnchants = ingredient.getAllEnchantments();
        for (Enchantment enchantIndex : oldItemEnchants.keySet()) {
            Enchantment enchantment = enchantIndex;

            int destLevel = newItemEnchants.containsKey(enchantIndex) ? newItemEnchants.get(enchantIndex) : 0;
            int srcLevel = oldItemEnchants.get(enchantIndex);

            srcLevel = Math.max(srcLevel, destLevel);
            srcLevel = Math.min(srcLevel, enchantment.getMaxLevel());

            boolean canApplyFlag = enchantment.canApplyAtEnchantingTable(result);
            if (canApplyFlag) {
                for (Enchantment curEnchantIndex : newItemEnchants.keySet()) {
                    if (curEnchantIndex != enchantIndex
                            && !enchantment.isCompatibleWith(curEnchantIndex) /* canApplyTogether */) {
                        canApplyFlag = false;
                        break;
                    }
                }
                if (canApplyFlag)
                    newItemEnchants.put(enchantIndex, Integer.valueOf(srcLevel));
            }
        }
        EnchantmentHelper.setEnchantments(newItemEnchants, result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

}
