package mods.flammpfeil.slashblade.data.tag;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import mods.flammpfeil.slashblade.SlashBlade;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class SlashBladeEntityTypeTagProvider extends EntityTypeTagsProvider {

    public SlashBladeEntityTypeTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, String modId,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(Provider lookupProvider) {
        this.tag(EntityTypeTags.ATTACKABLE_BLACKLIST).add(EntityType.VILLAGER);
    }

    public static class EntityTypeTags {
        public static final TagKey<EntityType<?>> ATTACKABLE_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE,
                SlashBlade.prefix("blacklist/attackable"));
    }
}
