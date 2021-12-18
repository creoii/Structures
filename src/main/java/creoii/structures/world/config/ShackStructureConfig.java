package creoii.structures.world.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.FeatureConfig;

public record ShackStructureConfig(boolean surface) implements FeatureConfig {
    public static final Codec<ShackStructureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.BOOL.fieldOf("surface").forGetter((shackStructureConfig) -> {
            return shackStructureConfig.surface;
        })).apply(instance, ShackStructureConfig::new);
    });
}
