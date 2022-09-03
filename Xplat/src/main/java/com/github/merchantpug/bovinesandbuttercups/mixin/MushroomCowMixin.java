package com.github.merchantpug.bovinesandbuttercups.mixin;

import com.github.merchantpug.bovinesandbuttercups.item.CustomFlowerItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(MushroomCow.class)
public abstract class MushroomCowMixin extends Animal {
    protected MushroomCowMixin(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "getEffectFromItemStack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;getBlock()Lnet/minecraft/world/level/block/Block;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void bovinesandbuttercups$getEffectFromCustomFlowerStack(ItemStack itemStack, CallbackInfoReturnable<Optional<Pair<MobEffect, Integer>>> cir, Item item) {
        if (item instanceof CustomFlowerItem) {
            cir.setReturnValue(Optional.of(Pair.of(CustomFlowerItem.getSuspiciousStewEffect(this.getLevel(), itemStack), CustomFlowerItem.getSuspiciousStewDuration(this.getLevel(), itemStack))));
        }
    }
}
