package creoii.structures.world.structure;

import com.mojang.serialization.Codec;
import creoii.structures.StructuresMod;
import creoii.structures.registry.StructurePieceRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class TotemStructure extends StructureFeature<DefaultFeatureConfig> {
    private static final Identifier[] SEGMENT_TEMPLATES = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "totem/segments/segment_0"), new Identifier(StructuresMod.MOD_ID, "totem/segments/segment_1"), new Identifier(StructuresMod.MOD_ID, "totem/segments/segment_2"), new Identifier(StructuresMod.MOD_ID, "totem/segments/segment_3"), new Identifier(StructuresMod.MOD_ID, "totem/segments/segment_4")};
    private static final Identifier[] HEAD_TEMPLATES = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "totem/heads/head_0"), new Identifier(StructuresMod.MOD_ID, "totem/heads/head_1")};

    public TotemStructure(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), TotemStructure::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        StructureManager manager = context.structureManager();
        BlockPos center = context.chunkPos().getCenterAtY(0);
        Random random = new Random();
        int towerHeight = random.nextInt(4) + 3;
        int landHeight = context.chunkGenerator().getHeightInGround(center.getX(), center.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.world());
        BlockPos blockPos = new BlockPos(center.getX(), landHeight, center.getZ());
        for (int i = 0; i < towerHeight; ++i) {
            if (i < towerHeight - 1) {
                collector.addPiece(new TotemStructure.Piece(manager, SEGMENT_TEMPLATES[random.nextInt(SEGMENT_TEMPLATES.length)], blockPos.up(i * 2)));
            } else {
                collector.addPiece(new TotemStructure.Piece(manager, HEAD_TEMPLATES[random.nextInt(HEAD_TEMPLATES.length)], blockPos.up(i * 2).add(-1, 0, 0)));
            }
        }
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, Identifier identifier, BlockPos pos) {
            super(StructurePieceRegistry.TOTEM, 0, manager, identifier, identifier.toString(), createPlacementData(identifier), pos);
        }

        public Piece(StructureManager manager, NbtCompound nbt) {
            super(StructurePieceRegistry.TOTEM, nbt, manager, TotemStructure.Piece::createPlacementData);
        }

        private static StructurePlacementData createPlacementData(Identifier identifier) {
            return new StructurePlacementData().setMirror(BlockMirror.NONE).setPosition(BlockPos.ORIGIN).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if ("glazed_terracotta".equals(metadata)) {
                world.setBlockState(pos, random.nextBoolean() ? Blocks.RED_GLAZED_TERRACOTTA.getDefaultState() : Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState(), 3);
            }
        }
    }
}
