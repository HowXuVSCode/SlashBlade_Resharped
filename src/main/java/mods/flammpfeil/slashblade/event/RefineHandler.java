package mods.flammpfeil.slashblade.event;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.AnvilRepairEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RefineHandler {
    private static final class SingletonHolder {
        private static final RefineHandler instance = new RefineHandler();
    }

    public static RefineHandler getInstance() {
        return SingletonHolder.instance;
    }

    private RefineHandler() {
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onAnvilUpdateEvent(AnvilUpdateEvent event) {
        if (!event.getOutput().isEmpty())
            return;

        ItemStack base = event.getLeft();
        ItemStack material = event.getRight();

        if (base.isEmpty())
            return;
        if (!(base.getItem() instanceof ItemSlashBlade))
            return;
        if (material.isEmpty())
            return;

        boolean isRepairable = base.getItem().isValidRepairItem(base, material);

        if (!isRepairable)
            return;

        int level = material.getEnchantmentValue();

        if (level < 0)
            return;

        ItemStack result = base.copy();

        int refineLimit = Math.max(10, level);

        int cost = 0;
        while (cost < material.getCount()) {
            cost++;

            int damage = result.getCapability(ItemSlashBlade.BLADESTATE).map(s -> {
//                s.setDamage(s.getDamage() - (0.2f + 0.05f * level));
                result.setDamageValue(result.getDamageValue() - Math.max(1, level / 20));

                s.setProudSoulCount(s.getProudSoulCount() + Math.max(5000, level * 10));

                if (s.getRefine() < refineLimit)
                    s.setRefine(s.getRefine() + 1);
                return result.getDamageValue();
            }).orElse(0);

            if (damage <= 0)
                break;
        }

        event.setMaterialCost(cost);
        int levelCostBase = 1;
        event.setCost(levelCostBase * cost);
        event.setOutput(result);
    }

    static private final ResourceLocation REFINE = new ResourceLocation(SlashBlade.MODID, "tips/refine");

    @SubscribeEvent
    public void onAnvilRepairEvent(AnvilRepairEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer))
            return;

        ItemStack material = event.getRight();// .getIngredientInput();
        ItemStack base = event.getLeft();// .getItemInput();
        ItemStack output = event.getOutput();

        if (base.isEmpty())
            return;
        if (!(base.getItem() instanceof ItemSlashBlade))
            return;
        if (material.isEmpty())
            return;

        boolean isRepairable = base.getItem().isValidRepairItem(base, material);

        if (!isRepairable)
            return;

        int before = base.getCapability(ItemSlashBlade.BLADESTATE).map(s -> s.getRefine()).orElse(0);
        int after = output.getCapability(ItemSlashBlade.BLADESTATE).map(s -> s.getRefine()).orElse(0);

        if (before < after)
            AdvancementHelper.grantCriterion((ServerPlayer) event.getEntity(), REFINE);

    }

}
