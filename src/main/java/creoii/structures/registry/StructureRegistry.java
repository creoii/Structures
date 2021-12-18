package creoii.structures.registry;

import creoii.structures.StructuresMod;
import creoii.structures.world.config.ShackStructureConfig;
import creoii.structures.world.structure.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.random.RandomSeed;

public class StructureRegistry {
    public static StructureFeature<DefaultFeatureConfig> JUNGLE_TEMPLE;
    public static StructureFeature<DefaultFeatureConfig> WITCH_TOWER;
    public static StructureFeature<ShackStructureConfig> SHACK;
    public static StructureFeature<DefaultFeatureConfig> TOTEM;
    public static StructureFeature<DefaultFeatureConfig> BEACH_HEAD;

    public static void register() {
        JUNGLE_TEMPLE = registerStructure(new Identifier(StructuresMod.MOD_ID, "jungle_temple"), new JungleTempleStructure(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES, 40, 25, false, false);
        WITCH_TOWER = registerStructure(new Identifier(StructuresMod.MOD_ID, "witch_tower"), new WitchTowerStructure(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES, 20, 15, false, false);
        SHACK = registerStructure(new Identifier(StructuresMod.MOD_ID, "shack"), new ShackStructure(ShackStructureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES, 18, 12, true, false);
        TOTEM = registerStructure(new Identifier(StructuresMod.MOD_ID, "totem"), new TotemStructure(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES, 16, 10, false, false);
        BEACH_HEAD = registerStructure(new Identifier(StructuresMod.MOD_ID, "beach_head"), new BeachHeadStructure(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES, 12, 8, true, false);
    }

    public static void modify() {
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.JUNGLE), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "jungle_temple")));
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.SWAMP), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "witch_tower")));
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.MOUNTAIN), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "mountain_shack")));
        //BiomeModifications.addStructure(BiomeSelectors.all(), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "underground_shack")));
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.SAVANNA), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "totem")));
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.BEACH), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "beach_head")));
    }

    public static <C extends FeatureConfig> StructureFeature<C> registerStructure(Identifier name, StructureFeature<C> structure, GenerationStep.Feature step, int spacing, int separation, boolean adjustSurface, boolean superflat) {
        FabricStructureBuilder<C, ?> builder = FabricStructureBuilder.create(name, structure).step(step).defaultConfig(spacing, separation, (int) RandomSeed.getSeed());
        if (adjustSurface) builder.adjustsSurface();
        if (superflat) builder.enableSuperflat();
        return builder.register();
    }
}
