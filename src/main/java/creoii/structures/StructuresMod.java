package creoii.structures;

import creoii.structures.registry.StructurePieceRegistry;
import creoii.structures.registry.StructureRegistry;
import net.fabricmc.api.ModInitializer;

public class StructuresMod implements ModInitializer {
    public static final String MOD_ID = "structures";

    //TODO: Underground Shacks, Pillager Castles, Terracotta Tombs, Larger Monster Dungeons, Big Beehives, Halls

    @Override
    public void onInitialize() {
        StructureRegistry.register();
        StructurePieceRegistry.register();
        StructureRegistry.modify();
    }
}
