package net.merchantpug.bovinesandbuttercups.data.block;

import net.merchantpug.bovinesandbuttercups.util.MobEffectUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;
import java.util.Optional;

public record FlowerType(
        Optional<MobEffectInstance> stewEffectInstance,
        Optional<ItemStack> dyeCraftResult) {

    public static final MapCodec<FlowerType> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            MobEffectUtil.CODEC.optionalFieldOf("stew_effect").forGetter(FlowerType::stewEffectInstance),
            ItemStack.CODEC.optionalFieldOf("dye_craft_result").forGetter(FlowerType::dyeCraftResult)
    ).apply(builder, FlowerType::new));

    public static final FlowerType MISSING = new FlowerType(Optional.empty(), Optional.empty());

    @Override
    public boolean equals(final Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof FlowerType other))
            return false;

        return other.stewEffectInstance.equals(this.stewEffectInstance) && other.dyeCraftResult.equals(this.dyeCraftResult);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.stewEffectInstance, this.dyeCraftResult);
    }
}