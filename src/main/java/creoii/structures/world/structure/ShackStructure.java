package creoii.structures.world.structure;

import com.mojang.serialization.Codec;
import creoii.structures.StructuresMod;
import creoii.structures.registry.StructurePieceRegistry;
import creoii.structures.world.config.ShackStructureConfig;
import net.minecraft.block.Blocks;
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
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class ShackStructure extends StructureFeature<ShackStructureConfig> {
    private static final Identifier[] SURFACE_SHACKS = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "shacks/big_mountain"), new Identifier(StructuresMod.MOD_ID, "shacks/small_mountain")};
    private static final Identifier[] UNDERGROUND_SHACKS = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "shacks/big_underground"), new Identifier(StructuresMod.MOD_ID, "shacks/small_underground")};

    public ShackStructure(Codec<ShackStructureConfig> configCodec) {
        super(configCodec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), ShackStructure::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<ShackStructureConfig> context) {
        StructureManager manager = context.structureManager();
        BlockPos center = context.chunkPos().getCenterAtY(0);
        if (context.config().surface()) collector.addPiece(new ShackStructure.Piece(manager, SURFACE_SHACKS[context.random().nextInt(SURFACE_SHACKS.length)], new BlockPos(center.getX(), context.chunkGenerator().getHeightInGround(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world()), center.getZ())));
        else collector.addPiece(new ShackStructure.Piece(manager, UNDERGROUND_SHACKS[context.random().nextInt(UNDERGROUND_SHACKS.length)], new BlockPos(center.getX(), context.chunkGenerator().getHeightInGround(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world()), center.getZ())));
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, Identifier identifier, BlockPos pos) {
            super(StructurePieceRegistry.SHACK, 0, manager, identifier, identifier.toString(), createPlacementData(identifier), pos);
        }

        public Piece(StructureManager manager, NbtCompound nbt) {
            super(StructurePieceRegistry.SHACK, nbt, manager, Piece::createPlacementData);
        }

        private static StructurePlacementData createPlacementData(Identifier identifier) {
            return new StructurePlacementData().setMirror(BlockMirror.NONE).setPosition(BlockPos.ORIGIN).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if ("mountain_chest".equals(metadata)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                BlockEntity blockEntity = world.getBlockEntity(pos.down());
                if (blockEntity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockEntity).setLootTable(LootTables.PILLAGER_OUTPOST_CHEST, random.nextLong());
                }
            } else if ("underground_chest".equals(metadata)) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
                BlockEntity blockEntity = world.getBlockEntity(pos.down());
                if (blockEntity instanceof ChestBlockEntity) {
                    ((ChestBlockEntity)blockEntity).setLootTable(LootTables.ABANDONED_MINESHAFT_CHEST, random.nextLong());
                }
            }
        }
    }
}