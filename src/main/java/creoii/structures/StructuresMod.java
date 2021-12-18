package creoii.structures;

import creoii.structures.registry.StructurePieceRegistry;
import creoii.structures.registry.StructureRegistry;
import net.fabricmc.api.ModInitializer;

public class StructuresMod implements ModInitializer {
    public static final String MOD_ID = "structures";

    //TODO: Cabins, Underground Cabins, Pillager Castles

    @Override
    public void onInitialize() {
        StructureRegistry.register();
        StructurePieceRegistry.register();
        StructureRegistry.modify();
    }
}
