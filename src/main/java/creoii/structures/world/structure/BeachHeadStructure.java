package creoii.structures.world.structure;

import com.mojang.serialization.Codec;
import creoii.structures.StructuresMod;
import creoii.structures.registry.StructurePieceRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Random;

public class BeachHeadStructure extends StructureFeature<DefaultFeatureConfig> {
    private static final Identifier[] TEMPLATES = new Identifier[]{new Identifier(StructuresMod.MOD_ID, "beach_heads/stone"), new Identifier(StructuresMod.MOD_ID, "beach_heads/calcite"), new Identifier(StructuresMod.MOD_ID, "beach_heads/volcanic")};

    public BeachHeadStructure(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec, StructureGeneratorFactory.simple(StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG), BeachHeadStructure::addPieces));
    }

    private static void addPieces(StructurePiecesCollector collector, StructurePiecesGenerator.Context<DefaultFeatureConfig> context) {
        StructureManager manager = context.structureManager();
        BlockRotation rotation = BlockRotation.random(context.random());
        BlockPos center = context.chunkPos().getCenterAtY(0);
        Random random = context.random();
        int landHeight = context.chunkGenerator().getHeightInGround(center.getX(), center.getZ(), Heightmap.Type.OCEAN_FLOOR_WG, context.world());
        BlockPos blockPos = new BlockPos(center.getX(), landHeight, center.getZ());
        collector.addPiece(new BeachHeadStructure.Piece(manager, TEMPLATES[random.nextInt(TEMPLATES.length)], rotation, blockPos));
    }

    public static class Piece extends SimpleStructurePiece {
        public Piece(StructureManager manager, Identifier identifier, BlockRotation rotation, BlockPos pos) {
            super(StructurePieceRegistry.BEACH_HEAD, 0, manager, identifier, identifier.toString(), createPlacementData(rotation), pos);
        }

        public Piece(StructureManager manager, NbtCompound nbt) {
            super(StructurePieceRegistry.BEACH_HEAD, nbt, manager, (identifier) -> {
                return createPlacementData(BlockRotation.valueOf(nbt.getString("Rot")));
            });
        }

        private static StructurePlacementData createPlacementData(BlockRotation rotation) {
            return new StructurePlacementData().setRotation(rotation).setMirror(BlockMirror.NONE).setPosition(BlockPos.ORIGIN).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
        }

        protected void writeNbt(StructureContext context, NbtCompound nbt) {
            super.writeNbt(context, nbt);
            nbt.putString("Rot", this.placementData.getRotation().name());
        }

        @Override
        protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox boundingBox) {
            if ("stone".equals(metadata)) {
                if (random.nextInt(3) == 0) world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3);
                else world.setBlockState(pos, random.nextBoolean() ? Blocks.GOLD_ORE.getDefaultState() : Blocks.EMERALD_ORE.getDefaultState(), 3);
            } else if ("volcanic".equals(metadata)) {
                if (random.nextInt(3) == 0) world.setBlockState(pos, Blocks.SMOOTH_BASALT.getDefaultState(), 3);
                else world.setBlockState(pos, random.nextBoolean() ? Blocks.LAVA.getDefaultState() : random.nextBoolean() ? Blocks.DEEPSLATE_DIAMOND_ORE.getDefaultState() : Blocks.DEEPSLATE_EMERALD_ORE.getDefaultState(), 3);
            } else if ("calcite".equals(metadata)) {
                if (random.nextInt(3) == 0) world.setBlockState(pos, Blocks.CALCITE.getDefaultState(), 3);
                else world.setBlockState(pos, random.nextInt(3) == 0 ? Blocks.WATER.getDefaultState() : Blocks.RAW_GOLD_BLOCK.getDefaultState(), 3);
            }
        }
    }
}
