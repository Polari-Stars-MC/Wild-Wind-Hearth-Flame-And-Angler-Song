package org.polaris2023.wwhfas.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties PIRANHA = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1F)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600), 0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 600), 0.5f)
            .build();
    public static final FoodProperties COOKED_PIRANHA = new FoodProperties.Builder()
            .nutrition(6)
            .saturationModifier(0.8F)
            .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 300), 0.3f)
            .build();

}
