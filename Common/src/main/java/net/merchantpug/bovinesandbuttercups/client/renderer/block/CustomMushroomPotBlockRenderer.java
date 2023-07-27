package net.merchantpug.bovinesandbuttercups.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.api.BovineRegistryUtil;
import net.merchantpug.bovinesandbuttercups.api.bovinestate.BovineStatesAssociationRegistry;
import net.merchantpug.bovinesandbuttercups.client.resources.BovineBlockstateTypes;
import net.merchantpug.bovinesandbuttercups.content.block.entity.CustomMushroomPotBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.Optional;

public class CustomMushroomPotBlockRenderer implements BlockEntityRenderer<CustomMushroomPotBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public CustomMushroomPotBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void render(CustomMushroomPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ModelResourceLocation modelResourceLocation = new ModelResourceLocation(BovinesAndButtercups.asResource("bovinesandbuttercups/potted_missing_mushroom"), "");
        if (blockEntity.getMushroomType() != null) {
            Optional<ResourceLocation> modelLocationWithoutVariant = BovineStatesAssociationRegistry.getBlock(BovineRegistryUtil.getMushroomTypeKey(blockEntity.getMushroomType()), BovineBlockstateTypes.POTTED_MUSHROOM);
            if (modelLocationWithoutVariant.isPresent()) {
                modelResourceLocation = new ModelResourceLocation(modelLocationWithoutVariant.get(), "bovinesandbuttercups_" + BlockModelShaper.statePropertiesToString(blockEntity.getBlockState().getValues()));
            }
        }

        BakedModel pottedMushroomModel = Minecraft.getInstance().getModelManager().getModel(modelResourceLocation);

        blockRenderDispatcher.getModelRenderer().tesselateBlock(blockEntity.getLevel(), pottedMushroomModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, bufferSource.getBuffer(RenderType.cutout()), false, RandomSource.create(), blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()), OverlayTexture.NO_OVERLAY);
    }
}
