package net.merchantpug.bovinesandbuttercups.content.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.type.ConfiguredCowType;
import net.merchantpug.bovinesandbuttercups.registry.BovineStructureTypes;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class RanchStructure extends Structure {
    public static final int MAX_TOTAL_STRUCTURE_RANGE = 128;
    public static final Codec<RanchStructure> CODEC = RecordCodecBuilder.<RanchStructure>mapCodec(builder -> builder.group(
            settingsCodec(builder),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(RanchStructure::getStartPool),
            ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(RanchStructure::getStartJigsawName),
            Codec.intRange(0, 7).fieldOf("size").forGetter(RanchStructure::getMaxDepth),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(RanchStructure::getStartHeight),
            Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(RanchStructure::getProjectStartToHeightmap),
            Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(RanchStructure::getMaxDistanceFromCenter),
            RegistryCodecs.homogeneousList(Registries.CONFIGURED_FEATURE).optionalFieldOf("allowed_features").forGetter(RanchStructure::getAllowedFeatures),
            Codec.BOOL.optionalFieldOf("generate_in_fluids", true).forGetter(RanchStructure::isAbleToGenerateInFluids)
    ).apply(builder, RanchStructure::new)).flatXmap(verifyRange(), verifyRange()).codec();
    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final Optional<HolderSet<ConfiguredFeature<?, ?>>> allowedFeatures;
    private final boolean generateInFluids;
    private int currentlyGeneratingHeight;

    private static Function<RanchStructure, DataResult<RanchStructure>> verifyRange() {
        return (structure) -> {
            byte distanceModifier = switch (structure.terrainAdaptation()) {
                case NONE -> 0;
                case BURY, BEARD_THIN, BEARD_BOX -> 12;
            };

            return structure.maxDistanceFromCenter + (int) distanceModifier > MAX_TOTAL_STRUCTURE_RANGE ? DataResult.error(() -> "Structure size including terrain adaptation must not exceed 128") : DataResult.success(structure);
        };
    }

    public RanchStructure(StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName, int maxDepth, HeightProvider startHeight, Optional<Heightmap.Types> projectStartToHeightmap, int maxDistanceFromCenter, Optional<HolderSet<ConfiguredFeature<?, ?>>> allowedFeatures, boolean generateInFluids) {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.generateInFluids = generateInFluids;
        this.allowedFeatures = allowedFeatures;
    }

    public Holder<StructureTemplatePool> getStartPool() {
        return startPool;
    }

    public Optional<ResourceLocation> getStartJigsawName() {
        return startJigsawName;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public HeightProvider getStartHeight() {
        return startHeight;
    }

    public Optional<Heightmap.Types> getProjectStartToHeightmap() {
        return projectStartToHeightmap;
    }

    public int getMaxDistanceFromCenter() {
        return maxDistanceFromCenter;
    }

    public Optional<HolderSet<ConfiguredFeature<?, ?>>> getAllowedFeatures() {
        return allowedFeatures;
    }

    public boolean isAbleToGenerateInFluids() {
        return generateInFluids;
    }

    public int getCurrentlyGeneratingHeight() {
        return currentlyGeneratingHeight;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int height = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), height, chunkPos.getMinBlockZ());
        currentlyGeneratingHeight = height;
        return JigsawPlacement.addPieces(context, this.startPool, this.startJigsawName, this.maxDepth, pos, false, this.projectStartToHeightmap, this.maxDistanceFromCenter);
    }

    public StructureType<?> type() {
        return BovineStructureTypes.RANCH.get();
    }
}
