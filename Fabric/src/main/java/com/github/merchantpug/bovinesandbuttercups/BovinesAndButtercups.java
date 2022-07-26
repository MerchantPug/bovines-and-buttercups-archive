package com.github.merchantpug.bovinesandbuttercups;

import com.github.merchantpug.bovinesandbuttercups.command.EffectLockdownCommand;
import com.github.merchantpug.bovinesandbuttercups.entity.FlowerCow;
import com.github.merchantpug.bovinesandbuttercups.entity.type.FlowerCowLoaderFabric;
import com.github.merchantpug.bovinesandbuttercups.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.MobCategory;

public class BovinesAndButtercups implements ModInitializer {
	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
			String version = modContainer.getMetadata().getVersion().getFriendlyString();
			if (version.contains("+")) {
				version = version.split("\\+")[0];
			}
			if (version.contains("-")) {
				version = version.split("-")[0];
			}
			BovinesAndButtercupsCommon.VERSION = version;
		});
		BovinesAndButtercupsCommon.init();
		BovineSpawnRestrictions.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			EffectLockdownCommand.register(dispatcher);
		});

		FabricLoader.getInstance().getModContainer(Constants.MOD_ID).ifPresent(modContainer -> {
			ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercupsCommon.resourceLocation("mojang"), modContainer, "Mojang Textures", ResourcePackActivationType.NORMAL);
			ResourceManagerHelper.registerBuiltinResourcePack(BovinesAndButtercupsCommon.resourceLocation("no_grass"), modContainer, "No Grass Back", ResourcePackActivationType.NORMAL);
		});

		BiomeModifications.addSpawn(biomeSelectionContext -> true, MobCategory.CREATURE, BovineEntityTypes.MOOBLOOM.get(), 60, 2, 4);
		FabricDefaultAttributeRegistry.register(BovineEntityTypes.MOOBLOOM.get(), FlowerCow.createAttributes());

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new FlowerCowLoaderFabric());
	}
}
