package mods.flammpfeil.slashblade.item;

import com.google.common.collect.*;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.capability.inputstate.IInputState;
import mods.flammpfeil.slashblade.capability.slashblade.NamedBladeStateCapabilityProvider;
import mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState;
import mods.flammpfeil.slashblade.client.renderer.SlashBladeTEISR;
import mods.flammpfeil.slashblade.data.tag.SlashBladeItemTags;
import mods.flammpfeil.slashblade.entity.BladeItemEntity;
import mods.flammpfeil.slashblade.init.DefaultResources;
import mods.flammpfeil.slashblade.init.SBItems;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nullable;

import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ItemSlashBlade extends SwordItem {
    protected static final UUID ATTACK_DAMAGE_AMPLIFIER = UUID.fromString("2D988C13-595B-4E58-B254-39BB6FA077FD");
    protected static final UUID PLAYER_REACH_AMPLIFIER = UUID.fromString("2D988C13-595B-4E58-B254-39BB6FA077FE");

    public static final Capability<ISlashBladeState> BLADESTATE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<IInputState> INPUT_STATE = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final List<Enchantment> exEnchantment = List.of(Enchantments.SOUL_SPEED, Enchantments.POWER_ARROWS,
            Enchantments.FALL_PROTECTION);

    public ItemSlashBlade(Tier tier, int attackDamageIn, float attackSpeedIn, Properties builder) {
        super(tier, attackDamageIn, attackSpeedIn, builder);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (exEnchantment.contains(enchantment))
            return true;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> def = super.getAttributeModifiers(slot, stack);
        Multimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();

        result.putAll(Attributes.ATTACK_DAMAGE, def.get(Attributes.ATTACK_DAMAGE));
        result.putAll(Attributes.ATTACK_SPEED, def.get(Attributes.ATTACK_SPEED));

        if (slot == EquipmentSlot.MAINHAND) {
            LazyOptional<ISlashBladeState> state = stack.getCapability(BLADESTATE);
            state.ifPresent(s -> {
                var swordType = SwordType.from(stack);
                float baseAttackModifier = s.getBaseAttackModifier();
                AttributeModifier base = new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                        (double) baseAttackModifier, AttributeModifier.Operation.ADDITION);
                result.remove(Attributes.ATTACK_DAMAGE, base);
                result.put(Attributes.ATTACK_DAMAGE, base);

                float attackAmplifier = s.getAttackAmplifier() - 1F;
                if (s.isBroken() || s.isSealed())
                    attackAmplifier = 2 - baseAttackModifier;
                else
                    attackAmplifier += swordType.contains(SwordType.FIERCEREDGE) ? s.getRefine()
                            : s.getRefine() * SlashBladeConfig.REFINE_BOUNS.get();

                AttributeModifier amplifier = new AttributeModifier(ATTACK_DAMAGE_AMPLIFIER, "Weapon amplifier",
                        (double) attackAmplifier, AttributeModifier.Operation.ADDITION);

                result.remove(Attributes.ATTACK_DAMAGE, amplifier);
                result.put(Attributes.ATTACK_DAMAGE, amplifier);

                result.put(ForgeMod.ENTITY_REACH.get(),
                        new AttributeModifier(PLAYER_REACH_AMPLIFIER, "Reach amplifer",
                                s.isBroken() ? ReachModifier.BrokendReach() : ReachModifier.BladeReach(),
                                AttributeModifier.Operation.ADDITION));

            });
        }

        return result;
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        EnumSet<SwordType> type = SwordType.from(stack);
        if (type.contains(SwordType.BEWITCHED))
            return Rarity.EPIC;
        if (type.contains(SwordType.ENCHANTED))
            return Rarity.RARE;
        return Rarity.COMMON;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (handIn == InteractionHand.OFF_HAND && !(playerIn.getMainHandItem().getItem() instanceof ItemSlashBlade)) {
            return InteractionResultHolder.pass(itemstack);
        }
        boolean result = itemstack.getCapability(BLADESTATE).map((state) -> {

            playerIn.getCapability(INPUT_STATE).ifPresent((s) -> s.getCommands().add(InputCommand.R_CLICK));

            ResourceLocation combo = state.progressCombo(playerIn);

            playerIn.getCapability(INPUT_STATE).ifPresent((s) -> s.getCommands().remove(InputCommand.R_CLICK));

            if (!combo.equals(ComboStateRegistry.NONE.getId()))
                playerIn.swing(handIn);

            return true;
        }).orElse(false);

        playerIn.startUsingItem(handIn);
        return new InteractionResultHolder<>(result ? InteractionResult.SUCCESS : InteractionResult.FAIL, itemstack);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemstack, Player playerIn, Entity entity) {
        Optional<ISlashBladeState> stateHolder = itemstack.getCapability(BLADESTATE)
                .filter((state) -> !state.onClick());

        stateHolder.ifPresent((state) -> {
            playerIn.getCapability(INPUT_STATE).ifPresent((s) -> s.getCommands().add(InputCommand.L_CLICK));

            state.progressCombo(playerIn);

            playerIn.getCapability(INPUT_STATE).ifPresent((s) -> s.getCommands().remove(InputCommand.L_CLICK));
        });

        return stateHolder.isPresent();
    }

    static public final String BREAK_ACTION_TIMEOUT = "BreakActionTimeout";

    @Override
    public void setDamage(ItemStack stack, int damage) {
        int maxDamage = stack.getMaxDamage();
        var state = stack.getCapability(BLADESTATE).orElseThrow(NullPointerException::new);
        if (state.isBroken()) {
            if (damage <= 0 && !state.isSealed()) {
                state.setBroken(false);
            } else if (maxDamage < damage) {
                damage = Math.min(damage, maxDamage - 1);
            }
        }
        state.setDamage(damage);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (amount <= 0)
            return 0;

        var cap = stack.getCapability(BLADESTATE).orElseThrow(NullPointerException::new);
        boolean current = cap.isBroken();

        if (stack.getDamageValue() >= stack.getMaxDamage() - 1) {
            amount = 0;
            cap.setBroken(true);
        }

        if (current != cap.isBroken()) {
            onBroken.accept(entity);
            if (entity instanceof ServerPlayer player) {
                stack.getShareTag();
                CriteriaTriggers.CONSUME_ITEM.trigger(player, stack);
            }

            if (entity instanceof Player player)
                player.awardStat(Stats.ITEM_BROKEN.get(stack.getItem()));
        }

        if (cap.isBroken() && this.isDestructable(stack))
            stack.shrink(1);

        return amount;
    }

    public static Consumer<LivingEntity> getOnBroken(ItemStack stack) {
        return (user) -> {
            user.broadcastBreakEvent(user.getUsedItemHand());

            var state = stack.getCapability(ItemSlashBlade.BLADESTATE).orElseThrow(NullPointerException::new);
            if (stack.isEnchanted())
            {
                int count = Math.max(1, state.getProudSoulCount() / 1000);
                List<Enchantment> enchantments = ForgeRegistries.ENCHANTMENTS.getValues().stream().toList();
                for (int i = 0; i < count; i += 1)
                {
                    ItemStack enchanted_soul = new ItemStack(SBItems.proudsoul_tiny);
                    enchanted_soul.enchant(enchantments.get(user.getRandom().nextInt(0, enchantments.size())), 1);
                    ItemEntity itemEntity = new ItemEntity(user.level(), user.getX(), user.getY(), user.getZ(), enchanted_soul);
                    itemEntity.setDefaultPickUpDelay();
                    user.level().addFreshEntity(itemEntity);
                }
            }
            ItemStack soul = new ItemStack(SBItems.proudsoul_tiny);

            int count = state.getProudSoulCount() >= 1000 ? 10 : Math.max(1, state.getProudSoulCount() / 100);

            soul.setCount(count);
            state.setProudSoulCount(state.getProudSoulCount() - (count * 100));

            ItemEntity itementity = new ItemEntity(user.level(), user.getX(), user.getY(), user.getZ(), soul);
            BladeItemEntity e = new BladeItemEntity(SlashBlade.RegistryEvents.BladeItem, user.level()) {
                static final String isReleased = "isReleased";

                @Override
                public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource ds) {

                    CompoundTag tag = this.getPersistentData();

                    if (!tag.getBoolean(isReleased)) {
                        this.getPersistentData().putBoolean(isReleased, true);

                        if (this.level() instanceof ServerLevel) {
                            Entity thrower = getOwner();

                            if (thrower != null) {
                                thrower.getPersistentData().remove(BREAK_ACTION_TIMEOUT);
                            }
                        }
                    }

                    return super.causeFallDamage(distance, damageMultiplier, ds);
                }
            };

            e.restoreFrom(itementity);
            e.init();
            e.push(0, 0.4, 0);

            e.setModel(state.getModel().orElse(DefaultResources.resourceDefaultModel));
            e.setTexture(state.getTexture().orElse(DefaultResources.resourceDefaultTexture));

            e.setPickUpDelay(20 * 2);
            e.setGlowingTag(true);

            e.setAirSupply(-1);

            e.setThrower(user.getUUID());

            user.level().addFreshEntity(e);

            user.getPersistentData().putLong(BREAK_ACTION_TIMEOUT, user.level().getGameTime() + 20 * 5);
        };
    }

    @Override
    public boolean hurtEnemy(ItemStack stackF, LivingEntity target, LivingEntity attacker) {

        ItemStack stack = attacker.getMainHandItem();

        stack.getCapability(BLADESTATE).ifPresent((state) -> {
            ResourceLocation loc = state.resolvCurrentComboState(attacker);
            ComboState cs = ComboStateRegistry.REGISTRY.get().getValue(loc) != null
                    ? ComboStateRegistry.REGISTRY.get().getValue(loc)
                    : ComboStateRegistry.NONE.get();
            cs.hitEffect(target, attacker);
            stack.hurtAndBreak(1, attacker, ItemSlashBlade.getOnBroken(stack));

        });

        return true;
    }

    public boolean mineBlock(ItemStack stack, Level worldIn, BlockState state, BlockPos pos,
            LivingEntity entityLiving) {

        if (state.getDestroySpeed(worldIn, pos) != 0.0F) {
            stack.getCapability(BLADESTATE).ifPresent((s) -> {
                stack.hurtAndBreak(1, entityLiving, ItemSlashBlade.getOnBroken(stack));
            });
        }

        return true;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
        int elapsed = this.getUseDuration(stack) - timeLeft;

        if (!worldIn.isClientSide()) {

            stack.getCapability(BLADESTATE).ifPresent((state) -> {

                var swordType = SwordType.from(stack);
                if (state.isBroken() || state.isSealed() || !(swordType.contains(SwordType.ENCHANTED)))
                    return;

                ResourceLocation sa = state.doChargeAction(entityLiving, elapsed);

                // sa.tickAction(entityLiving);
                if (!sa.equals(ComboStateRegistry.NONE.getId())) {
                    stack.hurtAndBreak(1, entityLiving, ItemSlashBlade.getOnBroken(stack));
                    entityLiving.swing(InteractionHand.MAIN_HAND);
                }
            });
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int count) {

        stack.getCapability(BLADESTATE).ifPresent((state) -> {

            (ComboStateRegistry.REGISTRY.get().getValue(state.getComboSeq()) != null
                    ? ComboStateRegistry.REGISTRY.get().getValue(state.getComboSeq())
                    : ComboStateRegistry.NONE.get()).holdAction(player);
            var swordType = SwordType.from(stack);
            if (state.isBroken() || state.isSealed() || !(swordType.contains(SwordType.ENCHANTED)))
                return;
            if (!player.level().isClientSide()) {
                int ticks = player.getTicksUsingItem();
                int fullChargeTicks = state.getFullChargeTicks(player);
                if (0 < ticks) {
                    if (ticks == fullChargeTicks) {// state.getFullChargeTicks(player)){
                        Vec3 pos = player.getEyePosition(1.0f).add(player.getLookAngle());
                        ((ServerLevel) player.level()).sendParticles(ParticleTypes.PORTAL, pos.x, pos.y, pos.z, 7, 0.7,
                                0.7, 0.7, 0.02);
                    }
                }
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);

        if (!isSelected) {
            stack.getCapability(BLADESTATE).ifPresent((state) -> {
                var swordType = SwordType.from(stack);
                if (entityIn instanceof Player player) {
                    boolean hasHunger = player.hasEffect(MobEffects.HUNGER) && SlashBladeConfig.HUNGER_CAN_REPAIR.get();
                    if (swordType.contains(SwordType.BEWITCHED) || hasHunger) {
                        if (stack.getDamageValue() > 0 && player.getFoodData().getFoodLevel() > 0) {
                            int hungerAmplifier = hasHunger ? player.getEffect(MobEffects.HUNGER).getAmplifier() : 0;
                            int level = 1 + Math.abs(hungerAmplifier);
                            player.causeFoodExhaustion(
                                    SlashBladeConfig.BEWITCHED_HUNGER_EXHAUSTION.get().floatValue() * level);
                            stack.setDamageValue(stack.getDamageValue() - level);
                        }
                    }
                }
            });

            return;
        }

        if (stack == null)
            return;
        if (entityIn == null)
            return;

        stack.getCapability(BLADESTATE).ifPresent((state) -> {
            if (entityIn instanceof LivingEntity living) {
                entityIn.getCapability(INPUT_STATE).ifPresent(mInput -> {
                    mInput.getScheduler().onTick(living);
                });

                /*
                 * if(0.5f > state.getDamage()) state.setDamage(0.99f);
                 */
                ResourceLocation loc = state.resolvCurrentComboState(living);
                ComboState cs = ComboStateRegistry.REGISTRY.get().getValue(loc) != null
                        ? ComboStateRegistry.REGISTRY.get().getValue(loc)
                        : ComboStateRegistry.NONE.get();
                cs.tickAction(living);

                state.sendChanges(entityIn);
            }
        });
    }

    @Nullable
    @Override
    public CompoundTag getShareTag(ItemStack stack) {
    	var tag = super.getShareTag(stack) == null ? stack.getOrCreateTag() : super.getShareTag(stack);
        stack.getCapability(BLADESTATE).ifPresent(state -> tag.put("bladeState", state.serializeNBT()));
        return tag;
    }


    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
    	super.readShareTag(stack, nbt);
        if (nbt == null) return;
        if (!nbt.contains("bladeState")) return;
        stack.getCapability(BLADESTATE).ifPresent(state -> state.deserializeNBT(nbt.getCompound("bladeState")));
    }

    // damage ----------------------------------------------------------

    @Override
    public int getDamage(ItemStack stack) {
        return stack.getCapability(BLADESTATE).map(s->s.getDamage()).orElse(0);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return stack.getCapability(BLADESTATE).map(s->s.getMaxDamage()).orElse(super.getMaxDamage(stack));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return stack.getCapability(BLADESTATE).filter((s) -> !s.getTranslationKey().isEmpty())
                .map((state) -> state.getTranslationKey()).orElseGet(() -> super.getDescriptionId(stack));
    }

    public boolean isDestructable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {

        if (Ingredient.of(ItemTags.STONE_TOOL_MATERIALS).test(repair)) {
            return true;
        }

        /*
         * Tag<Item> tags = ItemTags.getCollection().get(new
         * ResourceLocation("slashblade","proudsouls"));
         * 
         * if(tags != null){ boolean result = Ingredient.fromTag(tags).test(repair); }
         */

        // todo: repair custom material
        if (repair.is(SlashBladeItemTags.PROUD_SOULS))
            return true;
        return super.isValidRepairItem(toRepair, repair);
    }

    RangeMap<Comparable<?>, Object> refineColor = ImmutableRangeMap.builder()
            .put(Range.lessThan(10), ChatFormatting.GRAY).put(Range.closedOpen(10, 50), ChatFormatting.YELLOW)
            .put(Range.closedOpen(50, 100), ChatFormatting.GREEN).put(Range.closedOpen(100, 150), ChatFormatting.AQUA)
            .put(Range.closedOpen(150, 200), ChatFormatting.BLUE).put(Range.atLeast(200), ChatFormatting.LIGHT_PURPLE)
            .build();

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
            this.appendSwordType(stack, worldIn, tooltip, flagIn);
            this.appendProudSoulCount(tooltip, s);
            this.appendKillCount(tooltip, s);
            this.appendSlashArt(stack, tooltip, s);
            this.appendRefineCount(tooltip, s);
        });

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public void appendSlashArt(ItemStack stack, List<Component> tooltip, @NotNull ISlashBladeState s) {
        var swordType = SwordType.from(stack);
        if (swordType.contains(SwordType.BEWITCHED) && !swordType.contains(SwordType.SEALED)) {
            tooltip.add(Component.translatable("slashblade.tooltip.slash_art", s.getSlashArts().getDescription())
                    .withStyle(ChatFormatting.GRAY));
        }
    }

    public void appendRefineCount(List<Component> tooltip, @NotNull ISlashBladeState s) {
        if (s.getRefine() > 0) {
            tooltip.add(Component.translatable("slashblade.tooltip.refine", s.getRefine())
                    .withStyle((ChatFormatting) refineColor.get(s.getRefine())));
        }
    }

    public void appendProudSoulCount(List<Component> tooltip, @NotNull ISlashBladeState s) {
        if (s.getProudSoulCount() > 0) {
            MutableComponent countComponent = Component
                    .translatable("slashblade.tooltip.proud_soul", s.getProudSoulCount())
                    .withStyle(ChatFormatting.GRAY);
            if (s.getProudSoulCount() > 1000)
                countComponent = countComponent.withStyle(ChatFormatting.DARK_PURPLE);
            tooltip.add(countComponent);
        }
    }

    public void appendKillCount(List<Component> tooltip, @NotNull ISlashBladeState s) {
        if (s.getKillCount() > 0) {
            MutableComponent killCountComponent = Component
                    .translatable("slashblade.tooltip.killcount", s.getKillCount()).withStyle(ChatFormatting.GRAY);
            if (s.getKillCount() > 1000)
                killCountComponent = killCountComponent.withStyle(ChatFormatting.DARK_PURPLE);
            tooltip.add(killCountComponent);
        }
    }

    public void appendSwordType(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        var swordType = SwordType.from(stack);
        if (swordType.contains(SwordType.BEWITCHED)) {
            tooltip.add(
                    Component.translatable("slashblade.sword_type.bewitched").withStyle(ChatFormatting.DARK_PURPLE));
        } else if (swordType.contains(SwordType.ENCHANTED)) {
            tooltip.add(Component.translatable("slashblade.sword_type.enchanted").withStyle(ChatFormatting.DARK_AQUA));
        } else {
            tooltip.add(Component.translatable("slashblade.sword_type.noname").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public @org.jetbrains.annotations.Nullable ICapabilityProvider initCapabilities(ItemStack stack,
            @org.jetbrains.annotations.Nullable CompoundTag nbt) {
    	if(!stack.isEmpty() && stack.getItem() instanceof ItemSlashBlade)
    		return new NamedBladeStateCapabilityProvider(stack);
    	return null;
    }

    /**
     * @return true = cancel : false = swing
     */
    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return !stack.getCapability(BLADESTATE).filter(s -> s.getLastActionTime() == entity.level().getGameTime())
                .isPresent();
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Nullable
    @Override
    public Entity createEntity(Level world, Entity location, ItemStack itemstack) {
        BladeItemEntity e = new BladeItemEntity(SlashBlade.RegistryEvents.BladeItem, world);
        e.restoreFrom(location);
        e.init();
        return e;
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, Level world) {
        return super.getEntityLifespan(itemStack, world);// Short.MAX_VALUE;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {

        consumer.accept(new IClientItemExtensions() {
            BlockEntityWithoutLevelRenderer renderer = new SlashBladeTEISR(
                    Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                    Minecraft.getInstance().getEntityModels());

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });

        super.initializeClient(consumer);
    }
}
