package net.merchantpug.bovinesandbuttercups.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.merchantpug.bovinesandbuttercups.access.ChunkGeneratorAccess;
import net.merchantpug.bovinesandbuttercups.content.structure.RanchStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FeatureSorter;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin implements ChunkGeneratorAccess {
    @Unique
    private GenerationStep.Decoration bovinesandbuttercups$step;

    @Inject(method = "getStructureGeneratingAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/StructureManager;checkStructurePresence(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/levelgen/structure/Structure;Z)Lnet/minecraft/world/level/levelgen/structure/StructureCheckResult;"), cancellable = true)
    private static void bovinesandbuttercups$dontLocateRanchesInFluids(Set<Holder<Structure>> structureHoldersSet, LevelReader level, StructureManager structureManager, boolean skipKnownStructures, StructurePlacement placement, ChunkPos chunkPos, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir, @Local Holder<Structure> structure) {
        if (structure.isBound() && structure.value() instanceof RanchStructure ranch) {
            BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), ranch.getCurrentlyGeneratingHeight(), chunkPos.getMinBlockZ());
            if (!ranch.isAbleToGenerateInFluids() && !level.getFluidState(pos).isEmpty())
                cir.setReturnValue(null);
        }
    }

    @WrapWithCondition(method = "applyBiomeDecoration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/WorldgenRandom;setFeatureSeed(JII)V", ordinal = 0))
    private boolean bovinesandbuttercups$dontSetFeatureSeedWhenFluidFilledRanch(WorldgenRandom instance, long decorationSeed, int index, int decorationStep, WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager, @Local SectionPos sectionPos, @Local Structure structure) {
        if (structure instanceof RanchStructure ranch) {
            Optional<AABB> box =  structureManager.startsForStructure(sectionPos, structure).stream().map(start -> AABB.of(start.getBoundingBox())).reduce((start, start2) -> new AABB(Math.min(start.minX, start2.minX), Math.min(start.minY, start2.minY), Math.min(start.minZ, start2.minZ), Math.max(start.maxX, start2.maxX), Math.max(start.maxY, start2.maxY), Math.max(start.maxZ, start2.maxZ)));
            return box.isPresent() && (ranch.isAbleToGenerateInFluids() || BlockPos.betweenClosedStream(box.get()).allMatch(pos -> level.getFluidState(pos).isEmpty()));
        }
        return true;
    }

    @WrapWithCondition(method = "applyBiomeDecoration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setCurrentlyGenerating(Ljava/util/function/Supplier;)V", ordinal = 0))
    private boolean bovinesandbuttercups$dontSetFluidFilledRanchAsCurrentlyGenerating(WorldGenLevel instance, Supplier<String> currentlyGenerating, WorldGenLevel level, ChunkAccess access, StructureManager structureManager, @Local SectionPos sectionPos, @Local Structure structure) {
        if (structure instanceof RanchStructure ranch) {
            Optional<AABB> box =  structureManager.startsForStructure(sectionPos, structure).stream().map(start -> AABB.of(start.getBoundingBox())).reduce((start, start2) -> new AABB(Math.min(start.minX, start2.minX), Math.min(start.minY, start2.minY), Math.min(start.minZ, start2.minZ), Math.max(start.maxX, start2.maxX), Math.max(start.maxY, start2.maxY), Math.max(start.maxZ, start2.maxZ)));
            return box.isPresent() && (ranch.isAbleToGenerateInFluids() || BlockPos.betweenClosedStream(box.get()).allMatch(pos -> level.getFluidState(pos).isEmpty()));
        }
        return true;
    }

    @WrapOperation(method = "applyBiomeDecoration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/StructureManager;startsForStructure(Lnet/minecraft/core/SectionPos;Lnet/minecraft/world/level/levelgen/structure/Structure;)Ljava/util/List;"))
    private List<StructureStart> bovinesandbuttercups$dontGenerateFluidFilledRanch(StructureManager instance, SectionPos sectionPos, Structure structure, Operation<List<StructureStart>> original, WorldGenLevel level, ChunkAccess access, StructureManager structureManager) {
        if (structure instanceof RanchStructure ranch) {
            Optional<AABB> box =  structureManager.startsForStructure(sectionPos, structure).stream().map(start -> AABB.of(start.getBoundingBox())).reduce((start, start2) -> new AABB(Math.min(start.minX, start2.minX), Math.min(start.minY, start2.minY), Math.min(start.minZ, start2.minZ), Math.max(start.maxX, start2.maxX), Math.max(start.maxY, start2.maxY), Math.max(start.maxZ, start2.maxZ)));
            if (box.isPresent() && (ranch.isAbleToGenerateInFluids() || BlockPos.betweenClosedStream(box.get()).allMatch(pos -> level.getFluidState(pos).isEmpty())))
                return original.call(instance, sectionPos, structure);
            return List.of();
        }
        return original.call(instance, sectionPos, structure);
    }

    @Inject(method = "applyBiomeDecoration", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/StructureManager;shouldGenerateStructures()Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void bovinesandbuttercups$setStep(WorldGenLevel level, ChunkAccess chunkAccess, StructureManager structureManager, CallbackInfo ci, ChunkPos chunkPos, SectionPos sectionPos, BlockPos blockPos, Registry<Structure> structureRegistry, Map<Integer, List<Structure>> intToStructureMap, List<FeatureSorter.StepFeatureData> stepFeatureData, WorldgenRandom random, long decorationSeed, Set<Holder<Biome>> biomeHolder, int featureDataSize, Registry<PlacedFeature> placedFeatureRegistry, int stepMax, int stepValue) {
        for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
            if (step.ordinal() == stepValue) {
                bovinesandbuttercups$step = step;
                break;
            }
        }
    }

    @Override
    public GenerationStep.Decoration bovinesandbuttercups$getStep() {
        return bovinesandbuttercups$step;
    }
}
