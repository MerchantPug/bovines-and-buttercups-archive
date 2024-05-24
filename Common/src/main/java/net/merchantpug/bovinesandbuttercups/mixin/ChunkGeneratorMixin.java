package net.merchantpug.bovinesandbuttercups.mixin;

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
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin implements ChunkGeneratorAccess {
    @Unique
    private GenerationStep.Decoration bovinesandbuttercups$step;

    @Inject(method = "getStructureGeneratingAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/StructureManager;checkStructurePresence(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/levelgen/structure/Structure;Z)Lnet/minecraft/world/level/levelgen/structure/StructureCheckResult;", shift = At.Shift.AFTER), cancellable = true)
    private static void bovinesandbuttercups$dontGenerateRanchesInFluids(Set<Holder<Structure>> structures, LevelReader level, StructureManager manager, boolean bypassCheck, StructurePlacement placement, ChunkPos chunkPos, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> cir, @Local Holder<Structure> structure) {
        if (structure.isBound() && structure.value() instanceof RanchStructure ranch) {
            BlockPos pos = new BlockPos(chunkPos.getMinBlockX(), ranch.getCurrentlyGeneratingHeight(), chunkPos.getMinBlockZ());
            if (!ranch.isAbleToGenerateInFluids() && !level.getFluidState(pos).isEmpty())
                cir.setReturnValue(null);
        }
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
