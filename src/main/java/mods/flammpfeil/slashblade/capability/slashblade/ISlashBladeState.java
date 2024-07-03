package mods.flammpfeil.slashblade.capability.slashblade;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;

import mods.flammpfeil.slashblade.client.renderer.CarryType;
import mods.flammpfeil.slashblade.event.BladeMotionEvent;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.network.ActiveStateSyncMessage;
import mods.flammpfeil.slashblade.network.NetworkManager;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.combo.ComboState;
import mods.flammpfeil.slashblade.specialattack.SlashArts;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.EnumSetConverter;
import mods.flammpfeil.slashblade.util.NBTHelper;
import mods.flammpfeil.slashblade.util.TimeValueHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ISlashBladeState extends INBTSerializable<CompoundTag>
{
    @Override
    default CompoundTag serializeNBT()
    {
        CompoundTag tag = new CompoundTag();
        // action state
        tag.putLong("lastActionTime", this.getLastActionTime());
        tag.putInt("TargetEntity", this.getTargetEntityId());
        tag.putBoolean("_onClick", this.onClick());
        tag.putFloat("fallDecreaseRate", this.getFallDecreaseRate());
        tag.putFloat("AttackAmplifier", this.getAttackAmplifier());
        tag.putString("currentCombo", this.getComboSeq().toString());
        tag.putInt("Damage", this.getDamage());
        tag.putInt("maxDamage", this.getMaxDamage());
        tag.putInt("proudSoul", this.getProudSoulCount());
        tag.putBoolean("isBroken", this.isBroken());

        // passive state
        tag.putBoolean("isSealed", this.isSealed());

        tag.putFloat("baseAttackModifier", this.getBaseAttackModifier());

        tag.putInt("killCount", this.getKillCount());
        tag.putInt("RepairCounter", this.getRefine());

        UUID bladeId = this.getUniqueId();
        tag.putUUID("BladeUniqueId", bladeId);

        // performance setting

        tag.putString("SpecialAttackType", Optional.ofNullable(this.getSlashArtsKey())
        .orElse(SlashArtsRegistry.JUDGEMENT_CUT.getId()).toString());
        tag.putBoolean("isDefaultBewitched", this.isDefaultBewitched());
        tag.putString("translationKey", this.getTranslationKey());

        // render info
        tag.putByte("StandbyRenderType", (byte) this.getCarryType().ordinal());
        tag.putInt("SummonedSwordColor", this.getColorCode());
        tag.putBoolean("SummonedSwordColorInverse", this.isEffectColorInverse());
        tag.put("adjustXYZ", NBTHelper.newDoubleNBTList(this.getAdjust()));

        this.getTexture().ifPresent(loc -> tag.putString("TextureName", loc.toString()));
        this.getModel().ifPresent(loc -> tag.putString("ModelName", loc.toString()));

        tag.putString("ComboRoot",
                      Optional.ofNullable(this.getComboRoot()).orElse(ComboStateRegistry.STANDBY.getId()).toString());
        return tag;
    }

    @Override
    default void deserializeNBT(CompoundTag tag)
    {
        if (tag == null) return;

        // action state
        this.setLastActionTime(tag.getLong("lastActionTime"));
        this.setTargetEntityId(tag.getInt("TargetEntity"));
        this.setOnClick(tag.getBoolean("_onClick"));
        this.setFallDecreaseRate(tag.getFloat("fallDecreaseRate"));
        this.setAttackAmplifier(tag.getFloat("AttackAmplifier"));
        this.setComboSeq(ResourceLocation.tryParse(tag.getString("currentCombo")));
        this.setDamage(tag.getInt("Damage"));
        this.setMaxDamage(tag.getInt("maxDamage"));
        this.setProudSoulCount(tag.getInt("proudSoul"));
        this.setBroken(tag.getBoolean("isBroken"));

        this.setHasChangedActiveState(true);

        // passive state
        this.setSealed(tag.getBoolean("isSealed"));

        this.setBaseAttackModifier(tag.getFloat("baseAttackModifier"));

        this.setKillCount(tag.getInt("killCount"));
        this.setRefine(tag.getInt("RepairCounter"));

        this.setUniqueId(tag.hasUUID("BladeUniqueId") ? tag.getUUID("BladeUniqueId") : UUID.randomUUID());

        // performance setting

        this.setSlashArtsKey(ResourceLocation.tryParse(tag.getString("SpecialAttackType")));
        this.setDefaultBewitched(tag.getBoolean("isDefaultBewitched"));

        this.setTranslationKey(tag.getString("translationKey"));

        // render info
        this.setCarryType(EnumSetConverter.fromOrdinal(CarryType.values(), tag.getByte("StandbyRenderType"),
                                                           CarryType.DEFAULT));
        this.setColorCode(tag.getInt("SummonedSwordColor"));
        this.setEffectColorInverse(tag.getBoolean("SummonedSwordColorInverse"));
        this.setAdjust(NBTHelper.getVector3d(tag, "adjustXYZ"));

        if (tag.contains("TextureName"))
            this.setTexture(new ResourceLocation(tag.getString("TextureName")));
        else
            this.setTexture(null);

        if (tag.contains("ModelName"))
            this.setModel(new ResourceLocation(tag.getString("ModelName")));
        else
            this.setModel(null);

        this.setComboRoot(ResourceLocation.tryParse(tag.getString("ComboRoot")));
    }

    long getLastActionTime();

    void setLastActionTime(long lastActionTime);

    default long getElapsedTime(LivingEntity user) {
        long ticks = (Math.max(0, user.level().getGameTime() - this.getLastActionTime()));

        if (user.level().isClientSide())
            ticks = Math.max(0, ticks + 1);

        return ticks;
    }

    boolean onClick();

    void setOnClick(boolean onClick);

    float getFallDecreaseRate();

    void setFallDecreaseRate(float fallDecreaseRate);

    float getAttackAmplifier();

    void setAttackAmplifier(float attackAmplifier);

    @Nonnull
    ResourceLocation getComboSeq();

    void setComboSeq(ResourceLocation comboSeq);

    boolean isBroken();

    void setBroken(boolean broken);

    boolean isSealed();

    void setSealed(boolean sealed);

    float getBaseAttackModifier();

    void setBaseAttackModifier(float baseAttackModifier);

    int getProudSoulCount();

    void setProudSoulCount(int psCount);

    int getKillCount();

    void setKillCount(int killCount);

    int getRefine();

    void setRefine(int refine);

    UUID getUniqueId();

    void setUniqueId(UUID id);

    @Nonnull
    default SlashArts getSlashArts() {
        ResourceLocation key = getSlashArtsKey();
        SlashArts result = null;
        if (key != null)
            result = SlashArtsRegistry.REGISTRY.get().containsKey(key) ? SlashArtsRegistry.REGISTRY.get().getValue(key)
                    : SlashArtsRegistry.JUDGEMENT_CUT.get();

        if (key == SlashArtsRegistry.NONE.getId())
            result = null;

        return result != null ? result : SlashArtsRegistry.JUDGEMENT_CUT.get();
    }

    void setSlashArtsKey(ResourceLocation slashArts);

    ResourceLocation getSlashArtsKey();

    boolean isDefaultBewitched();

    void setDefaultBewitched(boolean defaultBewitched);

    @Nonnull
    String getTranslationKey();

    void setTranslationKey(String translationKey);

    @Nonnull
    CarryType getCarryType();

    void setCarryType(CarryType carryType);

    @Nonnull
    Color getEffectColor();

    void setEffectColor(Color effectColor);

    boolean isEffectColorInverse();

    void setEffectColorInverse(boolean effectColorInverse);

    default void setColorCode(int colorCode) {
        setEffectColor(new Color(colorCode));
    }

    default int getColorCode() {
        return getEffectColor().getRGB();
    }

    @Nonnull
    Vec3 getAdjust();

    void setAdjust(Vec3 adjust);

    @Nonnull
    Optional<ResourceLocation> getTexture();

    void setTexture(ResourceLocation texture);

    @Nonnull
    Optional<ResourceLocation> getModel();

    void setModel(ResourceLocation model);

    int getTargetEntityId();

    void setTargetEntityId(int id);

    @Nullable
    default Entity getTargetEntity(Level world) {
        int id = getTargetEntityId();
        if (id < 0)
            return null;
        else
            return world.getEntity(id);
    }

    default void setTargetEntityId(Entity target) {
        if (target != null)
            this.setTargetEntityId(target.getId());
        else
            this.setTargetEntityId(-1);
    }

    default int getFullChargeTicks(LivingEntity user) {
        return SlashArts.ChargeTicks;
    }

    default boolean isCharged(LivingEntity user) {
        if (!(SwordType.from(user.getMainHandItem()).contains(SwordType.ENCHANTED)))
            return false;
        if (this.isBroken() || this.isSealed())
            return false;
        int elapsed = user.getTicksUsingItem();
        return getFullChargeTicks(user) < elapsed;
    }

    default ResourceLocation progressCombo(LivingEntity user, boolean isVirtual) {
        ResourceLocation currentloc = resolvCurrentComboState(user);
        ComboState current = ComboStateRegistry.REGISTRY.get().getValue(currentloc);
        ResourceLocation next = current.getNext(user);
        if (!next.equals(ComboStateRegistry.NONE.getId()) && next.equals(currentloc))
            return ComboStateRegistry.NONE.getId();

        ResourceLocation rootNext = ComboStateRegistry.REGISTRY.get().getValue(getComboRoot()).getNext(user);
        ComboState nextCS = ComboStateRegistry.REGISTRY.get().getValue(next);
        ComboState rootNextCS = ComboStateRegistry.REGISTRY.get().getValue(rootNext);
        ResourceLocation resolved = nextCS.getPriority() <= rootNextCS.getPriority() ? next : rootNext;

        if (!isVirtual) {
            this.updateComboSeq(user, resolved);
        }

        return resolved;
    }

    default ResourceLocation progressCombo(LivingEntity user) {
        return progressCombo(user, false);
    }

    default ResourceLocation doChargeAction(LivingEntity user, int elapsed) {
        Map.Entry<Integer, ResourceLocation> currentloc = resolvCurrentComboStateTicks(user);

        if (elapsed <= 2)
            return ComboStateRegistry.NONE.getId();

        ComboState current = ComboStateRegistry.REGISTRY.get().getValue(currentloc.getValue());

        if (this.isBroken() || this.isSealed())
            return ComboStateRegistry.NONE.getId();

        // Uninterrupted
        if (currentloc.getValue() != ComboStateRegistry.NONE.getId() && current.getNext(user) == currentloc.getValue())
            return ComboStateRegistry.NONE.getId();

        int fullChargeTicks = getFullChargeTicks(user);
        int justReceptionSpan = SlashArts.getJustReceptionSpan(user);
        int justChargePeriod = fullChargeTicks + justReceptionSpan;

        RangeMap<Integer, SlashArts.ArtsType> charge_accept = ImmutableRangeMap.<Integer, SlashArts.ArtsType>builder()
                .put(Range.lessThan(fullChargeTicks), SlashArts.ArtsType.Fail)
                .put(Range.closedOpen(fullChargeTicks, justChargePeriod), SlashArts.ArtsType.Jackpot)
                .put(Range.atLeast(justChargePeriod), SlashArts.ArtsType.Success).build();

        SlashArts.ArtsType type = charge_accept.get(elapsed);

        if (type != SlashArts.ArtsType.Jackpot) {
            // quick charge
            SlashArts.ArtsType result = current.releaseAction(user, currentloc.getKey());

            if (result != SlashArts.ArtsType.Fail)
                type = result;
        }

        ResourceLocation csloc = this.getSlashArts().doArts(type, user);
        ComboState cs = ComboStateRegistry.REGISTRY.get().getValue(csloc);
        if (current != cs && csloc != ComboStateRegistry.NONE.getId()) {
            if (current.getPriority() > cs.getPriority()) {
                if (type == SlashArts.ArtsType.Jackpot)
                    AdvancementHelper.grantedIf(Enchantments.SOUL_SPEED, user);

                updateComboSeq(user, csloc);
            }
        }
        return csloc;
    }

    default void updateComboSeq(LivingEntity entity, ResourceLocation loc) {
        this.setComboSeq(loc);
        this.setLastActionTime(entity.level().getGameTime());
        ComboState cs = ComboStateRegistry.REGISTRY.get().getValue(loc);
        cs.clickAction(entity);

        MinecraftForge.EVENT_BUS.post(new BladeMotionEvent(entity, cs));
    }

    default ResourceLocation resolvCurrentComboState(LivingEntity user) {
        return resolvCurrentComboStateTicks(user).getValue();
    }

    default Map.Entry<Integer, ResourceLocation> resolvCurrentComboStateTicks(LivingEntity user) {
        ResourceLocation current = ComboStateRegistry.REGISTRY.get().containsKey(getComboSeq()) ? getComboSeq()
                : ComboStateRegistry.NONE.getId();
        ComboState currentCS = ComboStateRegistry.REGISTRY.get().getValue(current) != null
                ? ComboStateRegistry.REGISTRY.get().getValue(current)
                : ComboStateRegistry.NONE.get();
        int time = (int) TimeValueHelper.getMSecFromTicks(getElapsedTime(user));

        while (!current.equals(ComboStateRegistry.NONE.getId()) && currentCS.getTimeoutMS() < time) {
            time -= currentCS.getTimeoutMS();

            current = currentCS.getNextOfTimeout(user);
            this.updateComboSeq(user, current);
        }

        int ticks = (int) TimeValueHelper.getTicksFromMSec(time);
        return new AbstractMap.SimpleImmutableEntry<>(ticks, current);
    }

    ResourceLocation getComboRoot();

    void setComboRoot(ResourceLocation resourceLocation);

    int getDamage();

    void setDamage(int damage);

    int getMaxDamage();

    void setMaxDamage(int damage);

    boolean hasChangedActiveState();

    void setHasChangedActiveState(boolean isChanged);

    default void sendChanges(Entity entityIn) {
        if (!entityIn.level().isClientSide() && this.hasChangedActiveState()) {
            ActiveStateSyncMessage msg = new ActiveStateSyncMessage();
            msg.activeTag = this.getActiveState();
            msg.id = entityIn.getId();
            NetworkManager.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entityIn), msg);

            this.setHasChangedActiveState(false);
        }
    }

    default CompoundTag getActiveState() {
        CompoundTag tag = new CompoundTag();

        NBTHelper.getNBTCoupler(tag)
                .put("BladeUniqueId", this.getUniqueId())
                .put("lastActionTime", this.getLastActionTime()).put("TargetEntity", this.getTargetEntityId())
                .put("_onClick", this.onClick())
                .put("fallDecreaseRate", this.getFallDecreaseRate())
                .put("AttackAmplifier", this.getAttackAmplifier()).put("currentCombo", this.getComboSeq().toString())
                .put("proudSoul", this.getProudSoulCount()).put("killCount", this.getKillCount())
                .put("Damage", this.getDamage()).put("isBroken", this.isBroken());

        return tag;
    }

    default void setActiveState(CompoundTag tag) {
        NBTHelper.getNBTCoupler(tag)
                .get("BladeUniqueId", this::setUniqueId)
                .get("lastActionTime", this::setLastActionTime)
                .get("TargetEntity", ((Integer id) -> this.setTargetEntityId(id))).get("_onClick", this::setOnClick)
                .get("fallDecreaseRate", this::setFallDecreaseRate).get("AttackAmplifier", this::setAttackAmplifier)
                .get("currentCombo", ((String s) -> this.setComboSeq(ResourceLocation.tryParse(s))))
                .get("proudSoul", this::setProudSoulCount).get("killCount", this::setKillCount)
                .get("Damage", this::setDamage).get("isBroken", this::setBroken);

        this.setHasChangedActiveState(false);
    }
}