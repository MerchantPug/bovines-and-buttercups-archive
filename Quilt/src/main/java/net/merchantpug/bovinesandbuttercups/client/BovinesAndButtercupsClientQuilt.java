package net.merchantpug.bovinesandbuttercups.client;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.merchantpug.bovinesandbuttercups.client.item.CustomFlowerItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.item.CustomHugeMushroomItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.item.CustomMushroomItemRenderer;
import net.merchantpug.bovinesandbuttercups.client.particle.BloomParticle;
import net.merchantpug.bovinesandbuttercups.client.particle.ModelLocationParticle;
import net.merchantpug.bovinesandbuttercups.client.particle.ShroomParticle;
import net.merchantpug.bovinesandbuttercups.client.renderer.block.*;
import net.merchantpug.bovinesandbuttercups.client.renderer.entity.FlowerCowRenderer;
import net.merchantpug.bovinesandbuttercups.content.item.NectarBowlItem;
import net.merchantpug.bovinesandbuttercups.network.BovinePacketsS2C;
import net.merchantpug.bovinesandbuttercups.registry.*;
import net.merchantpug.bovinesandbuttercups.util.Color;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.server.packs.PackType;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

public class BovinesAndButtercupsClientQuilt implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        BovinePacketsS2C.registerS2C();
        BovinesAndButtercupsClient.init();

        EntityModelLayerRegistry.registerModelLayer(BovineModelLayers.MOOBLOOM_MODEL_LAYER, CowModel::createBodyLayer);
        EntityRendererRegistry.register(BovineEntityTypes.MOOBLOOM.get(), FlowerCowRenderer::new);

        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.CUSTOM_FLOWER.get(), CustomFlowerRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.CUSTOM_MUSHROOM.get(), CustomMushroomRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.POTTED_CUSTOM_FLOWER.get(), CustomFlowerPotBlockRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.POTTED_CUSTOM_MUSHROOM.get(), CustomMushroomPotBlockRenderer::new);
        BlockEntityRendererRegistry.register(BovineBlockEntityTypes.CUSTOM_MUSHROOM_BLOCK.get(), CustomHugeMushroomBlockRenderer::new);

        BuiltinItemRendererRegistry.INSTANCE.register(BovineItems.CUSTOM_FLOWER.get(), new CustomFlowerItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(BovineItems.CUSTOM_MUSHROOM.get(), new CustomMushroomItemRenderer());
        BuiltinItemRendererRegistry.INSTANCE.register(BovineItems.CUSTOM_MUSHROOM_BLOCK.get(), new CustomHugeMushroomItemRenderer());

        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.MODEL_LOCATION.get(), new ModelLocationParticle.Provider());
        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.BLOOM.get(), BloomParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(BovineParticleTypes.SHROOM.get(), ShroomParticle.Provider::new);

        ColorProviderRegistry.ITEM.register((stack, i) ->
                        i == 0 ? -1 : NectarBowlItem.getCowTypeFromStack(stack) != null ? Color.asInt(Color.saturateForNectar(NectarBowlItem.getCowTypeFromStack(stack).getConfiguration().getColor())) : -1,
                BovineItems.NECTAR_BOWL.get());

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new CowTextureReloadListenerQuilt());
    }
}
