package creoii.structures.registry;

import creoii.structures.world.structure.BeachHeadStructure;
import creoii.structures.world.structure.JungleTempleStructure;
import creoii.structures.world.structure.TotemStructure;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class StructurePieceRegistry {
    public static StructurePieceType JUNGLE_TEMPLE;
    public static StructurePieceType WITCH_TOWER;
    public static StructurePieceType SHACK;
    public static StructurePieceType TOTEM;
    public static StructurePieceType BEACH_HEAD;

    public static void register() {
        JUNGLE_TEMPLE = registerPiece(JungleTempleStructure.Piece::new, "TeJ");
        WITCH_TOWER = registerPiece(JungleTempleStructure.Piece::new, "WT");
        SHACK = registerPiece(JungleTempleStructure.Piece::new, "Sha");
        TOTEM = registerPiece(TotemStructure.Piece::new, "Ttm");
        BEACH_HEAD = registerPiece(BeachHeadStructure.Piece::new, "BeHe");
    }

    public static StructurePieceType registerPiece(StructurePieceType.ManagerAware piece, String name) {
        return Registry.register(Registry.STRUCTURE_PIECE, name.toLowerCase(Locale.ROOT), piece);
    }
}
