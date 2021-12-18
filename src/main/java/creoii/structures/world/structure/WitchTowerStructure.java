package creoii.structures.world.structure;

import com.mojang.serialization.Codec;
import creoii.structures.StructuresMod;
import creoii.structures.registry.StructurePieceRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class WitchTowerStructure extends StructureFeature<DefaultFeatureConfig> {
    private static final Identifier BASE_TEMPLATE = new Identifier(StructuresMod.MOD_ID, "witch_tower/base");
    private static final Identifier[] ROOM_TEMPLATES_0 = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "witch_tower/rooms/room_0"), new Identifier(StructuresMod.MOD_ID, "witch_tower/rooms/room_2")};
    private static final Identifier[] ROOM_TEMPLATES_1 = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "witch_tower/rooms/room_1"), new Identifier(StructuresMod.MOD_ID, "witch_tower/rooms/room_3")};
    private static final Identifier[] TOP_ROOM_TEMPLATES_0 = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "witch_tower/top_rooms/room_0")};
    private static final Identifier[] TOP_ROOM_TEMPLATES_1 = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "witch_tower/top_rooms/room_1")};
    private static final Identifier[] ROOF_TEMPLATES = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "witch_tower/roofs/roof_0"), new Identifier(StructuresMod.MOD_ID, "witch_tower/roofs/roof_1")};

    public WitchTowerStructure(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), WitchTowerStructure::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        StructureManager manager = context.structureManager();
        BlockPos center = context.chunkPos().getCenterAtY(0);
        Random random = new Random();
        int towerHeight = random.nextInt(3) + 2;
        int landHeight = context.chunkGenerator().getHeightInGround(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());
        BlockPos blockPos = new BlockPos(center.getX(), landHeight, center.getZ());
        collector.addPiece(new WitchTowerStructure.Piece(manager, BASE_TEMPLATE, blockPos));

        blockPos = blockPos.add(0, 6, 0);
        boolean flag = true;
        for (int i = 0; i < towerHeight; ++i) {
            if (i < towerHeight - 2) {
                if (flag) collector.addPiece(new WitchTowerStructure.Piece(manager, ROOM_TEMPLATES_0[random.nextInt(ROOM_TEMPLATES_0.length)], blockPos.add(1, i * 4, 0)));
                else collector.addPiece(new WitchTowerStructure.Piece(manager, ROOM_TEMPLATES_1[random.nextInt(ROOM_TEMPLATES_1.length)], blockPos.add(1, i * 4, 0)));
                flag = !flag;
            } else if (i < towerHeight - 1) {
                if (flag) collector.addPiece(new WitchTowerStructure.Piece(manager, TOP_ROOM_TEMPLATES_0[random.nextInt(TOP_ROOM_TEMPLATES_0.length)], blockPos.add(1, i * 4, 0)));
                else collector.addPiece(new WitchTowerStructure.Piece(manager, TOP_ROOM_TEMPLATES_1[random.nextInt(TOP_ROOM_TEMPLATES_1.length)], blockPos.add(1, i * 4, 0)));
            } else {
                collector.addPiece(new WitchTowerStructure.Piece(manager, ROOF_TEMPLATES[random.nextInt(ROOF_TEMPLATES.length)], blockPos.add(0, i * 4, 1)));
            }
        }
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, Identifier identifier, BlockPos pos) {
            super(StructurePieceRegistry.WITCH_TOWER, 0, manager, identifier, identifier.toString(), createPlacementData(identifier), pos);
        }

        public Piece(StructureManager manager, NbtCompound nbt) {
            super(StructurePieceRegistry.WITCH_TOWER, nbt, manager, WitchTowerStructure.Piece::createPlacementData);
        }

        private static StructurePlacementData createPlacementData(Identifier identifier) {
            return new StructurePlacementData().setMirror(BlockMirror.NONE).setPosition(BlockPos.ORIGIN).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if ("cauldron".equals(metadata)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(pos.down(), Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, random.nextInt(3) + 1), 3);
            }
        }
    }
}
