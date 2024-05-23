package net.merchantpug.bovinesandbuttercups.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;

public class ClientRegistryAccess {
    public static RegistryAccess registryAccess() {
        return Minecraft.getInstance().level.registryAccess();
    }
}
