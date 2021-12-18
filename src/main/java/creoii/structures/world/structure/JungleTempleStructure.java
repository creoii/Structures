package creoii.structures.world.structure;

import com.mojang.serialization.Codec;
import creoii.structures.StructuresMod;
import creoii.structures.registry.StructurePieceRegistry;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class JungleTempleStructure extends StructureFeature<DefaultFeatureConfig> {
    private static final Identifier BASE_TEMPLATE = new Identifier(StructuresMod.MOD_ID, "jungle_temple/base");
    private static final Identifier[] TOP_TEMPLATES = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "jungle_temple/tops/top_0"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/tops/top_1")};
    private static final Identifier[] ROOM_TEMPLATES = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "jungle_temple/rooms/room_0"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/rooms/room_1"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/rooms/room_2"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/rooms/room_3"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/rooms/room_4"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/rooms/room_5")};
    private static final Identifier[] PIT_TEMPLATES = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "jungle_temple/pits/pit_0"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/pits/pit_1"), new Identifier(StructuresMod.MOD_ID, "jungle_temple/pits/pit_2")};
    private static final BlockPos[] ROOM_OFFSETS = new BlockPos[]{new BlockPos(8, 1, 8), new BlockPos(15, 1, 8), new BlockPos(8, 1, 15), new BlockPos(15, 1, 15)};

    public JungleTempleStructure(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), JungleTempleStructure::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        StructureManager manager = context.structureManager();
        BlockPos center = context.chunkPos().getCenterAtY(0);
        int landHeight = context.chunkGenerator().getHeightInGround(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());
        BlockPos blockPos = new BlockPos(center.getX(), landHeight, center.getZ());
        collector.addPiece(new JungleTempleStructure.Piece(manager, BASE_TEMPLATE, blockPos));

        Random random = context.random();
        for (BlockPos roomPos : ROOM_OFFSETS) {
            collector.addPiece(new JungleTempleStructure.Piece(manager, ROOM_TEMPLATES[random.nextInt(ROOM_TEMPLATES.length)], blockPos.add(roomPos)));
            if (random.nextInt(4) == 0) {
                collector.addPiece(new JungleTempleStructure.Piece(manager, PIT_TEMPLATES[random.nextInt(PIT_TEMPLATES.length)], blockPos.add(roomPos).down(6)));
            }
        }

        collector.addPiece(new JungleTempleStructure.Piece(manager, TOP_TEMPLATES[random.nextInt(TOP_TEMPLATES.length)], blockPos.add(new BlockPos(9, 10, 9))));
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, Identifier identifier, BlockPos pos) {
            super(StructurePieceRegistry.JUNGLE_TEMPLE, 0, manager, identifier, identifier.toString(), createPlacementData(identifier), pos);
        }

        public Piece(StructureManager manager, NbtCompound nbt) {
            super(StructurePieceRegistry.JUNGLE_TEMPLE, nbt, manager, Piece::createPlacementData);
        }

        private static StructurePlacementData createPlacementData(Identifier identifier) {
            return new StructurePlacementData().setMirror(BlockMirror.NONE).setPosition(BlockPos.ORIGIN).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if ("chest".equals(metadata)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                BlockEntity blockEntity = world.getBlockEntity(pos.down());
                if (blockEntity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockEntity).setLootTable(LootTables.JUNGLE_TEMPLE_CHEST, random.nextLong());
                }
            }
        }
    }
}