package mods.flammpfeil.slashblade.data.builtin;

import java.util.List;

import org.apache.commons.compress.utils.Lists;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.item.SwordType;
import mods.flammpfeil.slashblade.registry.SlashArtsRegistry;
import mods.flammpfeil.slashblade.registry.slashblade.EnchantmentDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.PropertiesDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.RenderDefinition;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;

public class SlashBladeBuiltInRegistry {
    //拔刀剑注册

    public static final ResourceKey<SlashBladeDefinition> NIHIL = register("nihil"); //妖刀「似蛭」
    public static final ResourceKey<SlashBladeDefinition> BX_NIHIL = register("bx_nihil"); //血刀「似蛭」
    public static final ResourceKey<SlashBladeDefinition> LAEMMLE = register("laemmle"); //幻魔炼金拵

    public static final ResourceKey<SlashBladeDefinition> CC = register("cc"); //妖刀「红樱」
    public static final ResourceKey<SlashBladeDefinition> CX_NIHIL = register("cx_nihil"); //狱刀「似蛭」
    public static final ResourceKey<SlashBladeDefinition> PM_NIHIL = register("pm_nihil");//炼狱刀「死念」

    public static final ResourceKey<SlashBladeDefinition> NIER = register("nier");//尼尔
    public static final ResourceKey<SlashBladeDefinition> ART = register("art");//阿尔忒弥斯「耀月」






    public static final ResourceKey<SlashBladeDefinition> YAMATO = register("yamato");
    public static final ResourceKey<SlashBladeDefinition> YAMATO_BROKEN = register("yamato_broken");

    public static final ResourceKey<SlashBladeDefinition> TUKUMO = register("yuzukitukumo");
    public static final ResourceKey<SlashBladeDefinition> MURAMASA = register("muramasa");
    public static final ResourceKey<SlashBladeDefinition> RUBY = register("ruby");
    public static final ResourceKey<SlashBladeDefinition> SANGE = register("sange");
    public static final ResourceKey<SlashBladeDefinition> FOX_BLACK = register("fox_black");
    public static final ResourceKey<SlashBladeDefinition> FOX_WHITE = register("fox_white");

    public static final ResourceKey<SlashBladeDefinition> RODAI_WOODEN = register("rodai_wooden");
    public static final ResourceKey<SlashBladeDefinition> RODAI_STONE = register("rodai_stone");
    public static final ResourceKey<SlashBladeDefinition> RODAI_IRON = register("rodai_iron");
    public static final ResourceKey<SlashBladeDefinition> RODAI_GOLDEN = register("rodai_golden");
    public static final ResourceKey<SlashBladeDefinition> RODAI_DIAMOND = register("rodai_diamond");
    public static final ResourceKey<SlashBladeDefinition> RODAI_NETHERITE = register("rodai_netherite");

    public static final ResourceKey<SlashBladeDefinition> TAGAYASAN = register("tagayasan");
    public static final ResourceKey<SlashBladeDefinition> AGITO = register("agito");
    public static final ResourceKey<SlashBladeDefinition> AGITO_RUST = register("agito_rust");
    public static final ResourceKey<SlashBladeDefinition> OROTIAGITO = register("orotiagito");
    public static final ResourceKey<SlashBladeDefinition> OROTIAGITO_SEALED = register("orotiagito_sealed");
    public static final ResourceKey<SlashBladeDefinition> OROTIAGITO_RUST = register("orotiagito_rust");

    public static final ResourceKey<SlashBladeDefinition> YASHA = register("yasha");
    public static final ResourceKey<SlashBladeDefinition> YASHA_TRUE = register("yasha_true");

    public static final ResourceKey<SlashBladeDefinition> SABIGATANA = register("sabigatana");
    public static final ResourceKey<SlashBladeDefinition> SABIGATANA_BROKEN = register("sabigatana_broken");
    public static final ResourceKey<SlashBladeDefinition> DOUTANUKI = register("doutanuki");

    public static void registerAll(BootstapContext<SlashBladeDefinition> bootstrap) {

        //妖刀「似蛭」
        bootstrap.register(NIHIL, new SlashBladeDefinition(SlashBlade.prefix("nihil"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/howxu/nihil.png"))
                        .modelName(SlashBlade.prefix("model/named/howxu/nihil.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(8.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.DRIVE_HORIZONTAL.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 1),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 1),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 2))));

//血刀「似蛭」
        bootstrap.register(BX_NIHIL, new SlashBladeDefinition(SlashBlade.prefix("bx_nihil"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/howxu/nihilex.png"))
                        .modelName(SlashBlade.prefix("model/named/howxu/nihilex.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(10.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.BANE_OF_ARTHROPODS), 1),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.MOB_LOOTING), 1),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 3))));

        //幻魔炼金拵
        bootstrap.register(LAEMMLE, new SlashBladeDefinition(SlashBlade.prefix("laemmle"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/howxu/laemmle.png"))
                        .modelName(SlashBlade.prefix("model/named/howxu/laemmle.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.JUDGEMENT_CUT.getId())
                        .defaultSwordType(List.of(SwordType.ENCHANTED)).build(),
                List.of(
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 3))));

        //妖刀「红樱」
        bootstrap.register(CC, new SlashBladeDefinition(SlashBlade.prefix("cc"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/howxu/crimsoncherry.png"))
                        .modelName(SlashBlade.prefix("model/named/howxu/crimsoncherry.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(11.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.SAKURA_END.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.BANE_OF_ARTHROPODS), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 5))));

        //狱刀「似蛭」
        bootstrap.register(CX_NIHIL, new SlashBladeDefinition(SlashBlade.prefix("cx_nihil"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/howxu/cx_nihil.png"))
                        .modelName(SlashBlade.prefix("model/named/howxu/cx_nihil.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(12.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.BANE_OF_ARTHROPODS), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.MOB_LOOTING), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 5))));
//炼狱刀「死念」
        bootstrap.register(PM_NIHIL, new SlashBladeDefinition(SlashBlade.prefix("pm_nihil"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/howxu/pm_nihil.png"))
                        .modelName(SlashBlade.prefix("model/named/howxu/pm_nihil.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(13.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.SAKURA_END.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 5),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.BANE_OF_ARTHROPODS), 5),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 5))));
//尼尔
        bootstrap.register(NIER, new SlashBladeDefinition(SlashBlade.prefix("nier"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/howxu/nier.png"))
                        .modelName(SlashBlade.prefix("model/named/howxu/nier.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(9.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.CIRCLE_SLASH.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.POWER_ARROWS), 8),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 1),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 5))));



















        bootstrap.register(SABIGATANA,
                new SlashBladeDefinition(SlashBlade.prefix("sabigatana"),
                        RenderDefinition.Builder
                                .newInstance().textureName(SlashBlade.prefix("model/named/muramasa/sabigatana.png"))
                                .modelName(SlashBlade.prefix("model/named/muramasa/muramasa.obj")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(1200).build(),
                        Lists.newArrayList()));

        bootstrap.register(SABIGATANA_BROKEN, new SlashBladeDefinition(SlashBlade.prefix("sabigatana"),
                RenderDefinition.Builder
                        .newInstance().textureName(SlashBlade.prefix("model/named/muramasa/sabigatana.png"))
                        .modelName(SlashBlade.prefix("model/named/muramasa/muramasa.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(1200)
                        .defaultSwordType(List.of(SwordType.BROKEN, SwordType.SEALED)).build(),
                Lists.newArrayList()));

        bootstrap.register(DOUTANUKI,
                new SlashBladeDefinition(SlashBlade.prefix("doutanuki"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(SlashBlade.prefix("model/named/muramasa/doutanuki.png"))
                                .modelName(SlashBlade.prefix("model/named/muramasa/muramasa.obj")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(1200)
                                .slashArtsType(SlashArtsRegistry.CIRCLE_SLASH.getId())
                                .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                        Lists.newArrayList()));

        bootstrap.register(TAGAYASAN,
                new SlashBladeDefinition(SlashBlade.prefix("tagayasan"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(SlashBlade.prefix("model/named/tagayasan.png")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(1200)
                                .slashArtsType(SlashArtsRegistry.DRIVE_VERTICAL.getId())
                                .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                        List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 3),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3))));

        bootstrap.register(YASHA, new SlashBladeDefinition(SlashBlade.prefix("yasha"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/yasha/yasha.png"))
                        .modelName(SlashBlade.prefix("model/named/yasha/yasha.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F)
                        .slashArtsType(SlashArtsRegistry.SAKURA_END.getId()).maxDamage(1200).build(),
                Lists.newArrayList()));

        bootstrap.register(YASHA_TRUE, new SlashBladeDefinition(SlashBlade.prefix("yasha_true"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/yasha/yasha.png"))
                        .modelName(SlashBlade.prefix("model/named/yasha/yasha_true.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F)
                        .slashArtsType(SlashArtsRegistry.CIRCLE_SLASH.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).maxDamage(1200).build(),
                Lists.newArrayList()));

        bootstrap.register(AGITO_RUST, new SlashBladeDefinition(SlashBlade.prefix("agito_rust"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/agito_rust.png"))
                        .modelName(SlashBlade.prefix("model/named/agito.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(1200)
                        .defaultSwordType(List.of(SwordType.SEALED)).build(),
                Lists.newArrayList()));

        bootstrap.register(AGITO, new SlashBladeDefinition(SlashBlade.prefix("agito"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/agito_false.png"))
                        .modelName(SlashBlade.prefix("model/named/agito.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId()).maxDamage(1200).build(),
                Lists.newArrayList()));

        bootstrap.register(OROTIAGITO_RUST, new SlashBladeDefinition(SlashBlade.prefix("orotiagito_rust"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/agito_rust_true.png"))
                        .modelName(SlashBlade.prefix("model/named/agito.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(1200)
                        .defaultSwordType(List.of(SwordType.SEALED)).build(),
                Lists.newArrayList()));

        bootstrap.register(OROTIAGITO_SEALED, new SlashBladeDefinition(SlashBlade.prefix("orotiagito_sealed"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/agito_true.png"))
                        .modelName(SlashBlade.prefix("model/named/agito.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId()).maxDamage(1200).build(),
                Lists.newArrayList()));

        bootstrap.register(OROTIAGITO, new SlashBladeDefinition(SlashBlade.prefix("orotiagito"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/orotiagito.png"))
                        .modelName(SlashBlade.prefix("model/named/agito.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F)
                        .slashArtsType(SlashArtsRegistry.WAVE_EDGE.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).maxDamage(1200).build(),
                Lists.newArrayList()));

        bootstrap.register(RODAI_WOODEN,
                new SlashBladeDefinition(SlashBlade.prefix("rodai_wooden"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/rodai_wooden.png"))
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(2.0F).maxDamage(60).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_STONE,
                new SlashBladeDefinition(SlashBlade.prefix("rodai_stone"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/rodai_stone.png"))
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(3.0F).maxDamage(132).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_IRON,
                new SlashBladeDefinition(SlashBlade.prefix("rodai_iron"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/rodai_iron.png"))
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(4.0F).maxDamage(250).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_GOLDEN,
                new SlashBladeDefinition(SlashBlade.prefix("rodai_golden"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/rodai_golden.png"))
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(2.0F).maxDamage(1200).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_DIAMOND,
                new SlashBladeDefinition(SlashBlade.prefix("rodai_diamond"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/rodai_diamond.png"))
                                .build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F).maxDamage(1561).build(),
                        Lists.newArrayList()));

        bootstrap.register(RODAI_NETHERITE,
                new SlashBladeDefinition(SlashBlade.prefix("rodai_netherite"),
                        RenderDefinition.Builder.newInstance()
                                .textureName(SlashBlade.prefix("model/rodai_netherite.png")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(8.0F).maxDamage(2031).build(),
                        Lists.newArrayList()));

        bootstrap.register(RUBY,
                new SlashBladeDefinition(SlashBlade.prefix("ruby"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/ruby.png")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(1200).build(),
                        Lists.newArrayList()));

        bootstrap.register(FOX_BLACK, new SlashBladeDefinition(SlashBlade.prefix("fox_black"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/sange/black.png"))
                        .modelName(SlashBlade.prefix("model/named/sange/sange.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.VOID_SLASH.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SMITE), 4),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.KNOCKBACK), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2))));

        bootstrap.register(FOX_WHITE, new SlashBladeDefinition(SlashBlade.prefix("fox_white"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/sange/white.png"))
                        .modelName(SlashBlade.prefix("model/named/sange/sange.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(5.0F).maxDamage(1200)
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.KNOCKBACK), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.BANE_OF_ARTHROPODS), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 2),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.MOB_LOOTING), 3))));

        bootstrap.register(YAMATO,
                new SlashBladeDefinition(SlashBlade.prefix("yamato"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/yamato.png"))
                                .modelName(SlashBlade.prefix("model/named/yamato.obj")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F)
                                .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                        List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.SOUL_SPEED), 2),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.POWER_ARROWS), 5),
                                new EnchantmentDefinition(getEnchantmentID(Enchantments.FALL_PROTECTION), 4))));

        bootstrap.register(YAMATO_BROKEN,
                new SlashBladeDefinition(SlashBlade.prefix("yamato"),
                        RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/yamato.png"))
                                .modelName(SlashBlade.prefix("model/named/yamato.obj")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F)
                                .defaultSwordType(List.of(SwordType.BROKEN, SwordType.SEALED)).build(),
                        List.of()));

        bootstrap.register(TUKUMO, new SlashBladeDefinition(SlashBlade.prefix("yuzukitukumo"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/a_tukumo.png"))
                        .modelName(SlashBlade.prefix("model/named/agito.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F)
                        .slashArtsType(SlashArtsRegistry.DRIVE_HORIZONTAL.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                List.of(new EnchantmentDefinition(getEnchantmentID(Enchantments.FIRE_ASPECT), 1),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.SHARPNESS), 4),
                        new EnchantmentDefinition(getEnchantmentID(Enchantments.UNBREAKING), 3))));

        bootstrap.register(MURAMASA,
                new SlashBladeDefinition(SlashBlade.prefix("muramasa"),
                        RenderDefinition.Builder
                                .newInstance().textureName(SlashBlade.prefix("model/named/muramasa/muramasa.png"))
                                .modelName(SlashBlade.prefix("model/named/muramasa/muramasa.obj")).build(),
                        PropertiesDefinition.Builder.newInstance().baseAttackModifier(7.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.DRIVE_VERTICAL.getId()).build(),
                        Lists.newArrayList()));

        bootstrap.register(SANGE, new SlashBladeDefinition(SlashBlade.prefix("sange"),
                RenderDefinition.Builder.newInstance().textureName(SlashBlade.prefix("model/named/sange/sange.png"))
                        .modelName(SlashBlade.prefix("model/named/sange/sange.obj")).build(),
                PropertiesDefinition.Builder.newInstance().baseAttackModifier(6.0F).maxDamage(1200)
                        .slashArtsType(SlashArtsRegistry.SAKURA_END.getId())
                        .defaultSwordType(List.of(SwordType.BEWITCHED)).build(),
                Lists.newArrayList()));

    }

    private static ResourceLocation getEnchantmentID(Enchantment enchantment) {
        return ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
    }

    private static ResourceKey<SlashBladeDefinition> register(String id) {
        ResourceKey<SlashBladeDefinition> loc = ResourceKey.create(SlashBladeDefinition.REGISTRY_KEY,
                SlashBlade.prefix(id));
        return loc;
    }
}
