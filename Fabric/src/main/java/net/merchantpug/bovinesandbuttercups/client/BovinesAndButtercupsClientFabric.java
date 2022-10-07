package net.merchantpug.bovinesandbuttercups.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;

public class BovinesAndButtercupsClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BovinesAndButtercupsClientFabriclike.init();

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new CowTextureReloadListenerFabric());
    }
}
