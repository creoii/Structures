package creoii.structures;

import creoii.structures.registry.StructurePieceRegistry;
import creoii.structures.registry.StructureRegistry;
import net.fabricmc.api.ModInitializer;

public class StructuresMod implements ModInitializer {
    public static final String MOD_ID = "structures";

    //TODO: Underground Shacks, Pillager Castles, Terracotta Tombs, Larger Monster Dungeons, Big Beehives, Halls, Totems, Easter Island Heads
    // Shacks should be biome dependent: Stone Peaks, Snowy Peaks, etc
    // Jungle Pyramids should have Emerald Blocks
    // Halls are biome dependent: Ice, Normal, Desert, Badlands, Dark Oak
    
    @Override
    public void onInitialize() {
        StructureRegistry.register();
        StructurePieceRegistry.register();
        StructureRegistry.modify();
    }
}
