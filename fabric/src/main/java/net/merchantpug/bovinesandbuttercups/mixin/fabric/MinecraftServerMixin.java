package net.merchantpug.bovinesandbuttercups.mixin.fabric;

import net.merchantpug.bovinesandbuttercups.BovinesAndButtercups;
import net.merchantpug.bovinesandbuttercups.BovinesAndButtercupsFabric;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class, priority = 500)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void bovinesandbuttercups$setServer(CallbackInfo ci) {
        BovinesAndButtercupsFabric.setServer((MinecraftServer)(Object)this);
    }
}
