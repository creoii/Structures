package creoii.structures.registry;

import creoii.structures.StructuresMod;
import creoii.structures.world.structure.JungleTempleStructure;
import creoii.structures.world.structure.WitchTowerStructure;
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

    public static void register() {
        JUNGLE_TEMPLE = registerStructure(new Identifier(StructuresMod.MOD_ID, "jungle_temple"), new JungleTempleStructure(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES, 40, 25, true, false);
        WITCH_TOWER = registerStructure(new Identifier(StructuresMod.MOD_ID, "witch_tower"), new WitchTowerStructure(DefaultFeatureConfig.CODEC), GenerationStep.Feature.SURFACE_STRUCTURES, 20, 15, false, false);
    }

    public static void modify() {
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.JUNGLE), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "jungle_temple")));
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.SWAMP), RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, new Identifier(StructuresMod.MOD_ID, "witch_tower")));
    }

    public static <C extends FeatureConfig> StructureFeature<C> registerStructure(Identifier name, StructureFeature<C> structure, GenerationStep.Feature step, int spacing, int separation, boolean adjustSurface, boolean superflat) {
        FabricStructureBuilder<C, ?> builder = FabricStructureBuilder.create(name, structure).step(step).defaultConfig(spacing, separation, (int) RandomSeed.getSeed());
        if (adjustSurface) builder.adjustsSurface();
        if (superflat) builder.enableSuperflat();
        return builder.register();
    }
}
