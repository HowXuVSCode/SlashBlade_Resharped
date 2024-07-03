package mods.flammpfeil.slashblade.specialattack;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.capability.concentrationrank.ConcentrationRankCapabilityProvider;
import mods.flammpfeil.slashblade.entity.EntityDrive;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.KnockBacks;
import mods.flammpfeil.slashblade.util.VectorHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class WaveEdge
{
    public static void doSlash(LivingEntity playerIn, float roll, int lifetime, Vec3 centerOffset,
            boolean critical, double damage, float minSpeed, float maxSpeed, int count) {
        doSlash(playerIn, roll, lifetime, centerOffset, critical, damage, KnockBacks.cancel, minSpeed, maxSpeed, count);
    }

    public static void doSlash(LivingEntity playerIn, float roll, int lifetime, Vec3 centerOffset,
            boolean critical, double damage, KnockBacks knockback, float minSpeed, float maxSpeed, int count) {

        int colorCode = playerIn.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE)
                .map(state -> state.getColorCode()).orElse(0xFF3333FF);

        doSlash(playerIn, roll, lifetime, colorCode, centerOffset, critical, damage, knockback, minSpeed, maxSpeed, count);
    }

    public static void doSlash(LivingEntity playerIn, float roll, int lifetime, int colorCode, Vec3 centerOffset,
            boolean critical, double damage, KnockBacks knockback, float minSpeed, float maxSpeed, int count) {

        if (playerIn.level().isClientSide()) return;

        Vec3 pos = playerIn.position().add(0.0D, (double) playerIn.getEyeHeight() * 0.75D, 0.0D)
                .add(playerIn.getLookAngle().scale(0.3f));

        pos = pos.add(VectorHelper.getVectorForRotation(-90.0F, playerIn.getViewYRot(0)).scale(centerOffset.y))
                .add(VectorHelper.getVectorForRotation(0, playerIn.getViewYRot(0) + 90).scale(centerOffset.z))
                .add(playerIn.getLookAngle().scale(centerOffset.z));
        for (int i = 0; i <= count; i += 1)
        {
            EntityDrive drive = new EntityDrive(SlashBlade.RegistryEvents.Drive, playerIn.level());
            float speed = Mth.randomBetween(drive.level().getRandom(), minSpeed, maxSpeed);

            drive.setPos(pos.x, pos.y, pos.z);
            drive.setOwner(playerIn);
            drive.setRotationRoll(roll);
            drive.setYRot(playerIn.getYRot());
            drive.setXRot(0);

            drive.setColor(colorCode);
            drive.setIsCritical(critical);
            drive.setDamage(damage);
            drive.setSpeed(speed);
            drive.setKnockBack(knockback);

            drive.setLifetime(lifetime);

            if (playerIn != null)
                playerIn.getCapability(ConcentrationRankCapabilityProvider.RANK_POINT)
                    .ifPresent(rank -> drive.setRank(rank.getRankLevel(playerIn.level().getGameTime())));

            playerIn.level().addFreshEntity(drive);
            drive.shoot(playerIn.getLookAngle().x, playerIn.getLookAngle().y, playerIn.getLookAngle().z, drive.getSpeed(), 0);
        }
    }
}
