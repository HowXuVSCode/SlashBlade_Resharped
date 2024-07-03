package mods.flammpfeil.slashblade.capability.slashblade;

import mods.flammpfeil.slashblade.client.renderer.CarryType;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.util.EnumSetConverter;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Furia on 2017/01/10.
 */
public class SimpleBladeStateCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {

    protected LazyOptional<ISlashBladeState> state;

    public SimpleBladeStateCapabilityProvider(ItemStack blade, ResourceLocation model, ResourceLocation texture, float attack,
            int damage) {
        state = LazyOptional.of(() -> new SimpleSlashBladeState(blade, model, texture, attack, damage));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    	return ItemSlashBlade.BLADESTATE.orEmpty(cap, state);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        state.ifPresent(instance -> {
            // action state
            tag.putLong("lastActionTime", instance.getLastActionTime());
            tag.putInt("TargetEntity", instance.getTargetEntityId());
            tag.putBoolean("_onClick", instance.onClick());
            tag.putFloat("fallDecreaseRate", instance.getFallDecreaseRate());
            tag.putFloat("AttackAmplifier", instance.getAttackAmplifier());
            tag.putString("currentCombo", instance.getComboSeq().toString());
            tag.putInt("Damage", instance.getDamage());
            tag.putInt("proudSoul", instance.getProudSoulCount());
            tag.putBoolean("isBroken", instance.isBroken());

            // passive state
            tag.putBoolean("isSealed", instance.isSealed());

            tag.putInt("killCount", instance.getKillCount());
            tag.putInt("RepairCounter", instance.getRefine());

            UUID bladeId = instance.getUniqueId();
            tag.putUUID("BladeUniqueId", bladeId);

            // performance setting

            tag.putString("SpecialAttackType", Optional.ofNullable(instance.getSlashArtsKey())
                    .orElse(SlashArtsRegistry.JUDGEMENT_CUT.getId()).toString());
            // render info
            tag.putByte("StandbyRenderType", (byte) instance.getCarryType().ordinal());
            tag.putInt("SummonedSwordColor", instance.getColorCode());
            tag.putBoolean("SummonedSwordColorInverse", instance.isEffectColorInverse());
            tag.put("adjustXYZ", NBTHelper.newDoubleNBTList(instance.getAdjust()));

            tag.putString("ComboRoot",
                    Optional.ofNullable(instance.getComboRoot()).orElse(ComboStateRegistry.STANDBY.getId()).toString());

        });

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {

        state.ifPresent(instance -> {
            // action state
            instance.setLastActionTime(tag.getLong("lastActionTime"));
            instance.setTargetEntityId(tag.getInt("TargetEntity"));
            instance.setOnClick(tag.getBoolean("_onClick"));
            instance.setFallDecreaseRate(tag.getFloat("fallDecreaseRate"));
            instance.setAttackAmplifier(tag.getFloat("AttackAmplifier"));
            instance.setComboSeq(ResourceLocation.tryParse(tag.getString("currentCombo")));
            instance.setDamage(tag.getInt("Damage"));
            instance.setProudSoulCount(tag.getInt("proudSoul"));

            instance.setBroken(tag.getBoolean("isBroken"));
            instance.setHasChangedActiveState(true);

            // passive state
            instance.setSealed(tag.getBoolean("isSealed"));
            instance.setKillCount(tag.getInt("killCount"));
            instance.setRefine(tag.getInt("RepairCounter"));
            instance.setUniqueId(tag.hasUUID("BladeUniqueId") ? tag.getUUID("BladeUniqueId") : UUID.randomUUID());

            // render info
            instance.setCarryType(EnumSetConverter.fromOrdinal(CarryType.values(), tag.getByte("StandbyRenderType"),
                    CarryType.DEFAULT));
            instance.setColorCode(tag.getInt("SummonedSwordColor"));
            instance.setEffectColorInverse(tag.getBoolean("SummonedSwordColorInverse"));
            instance.setAdjust(NBTHelper.getVector3d(tag, "adjustXYZ"));

            instance.setComboRoot(ResourceLocation.tryParse(tag.getString("ComboRoot")));
        });
    }
}
