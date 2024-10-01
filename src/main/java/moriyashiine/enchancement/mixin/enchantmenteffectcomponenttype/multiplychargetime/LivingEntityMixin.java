/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.multiplychargetime;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.enchantment.effect.MultiplyChargeTimeEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract ItemStack getActiveItem();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyReturnValue(method = "getItemUseTimeLeft", at = @At("RETURN"))
	private int enchancement$multiplyChargeTime(int original) {
		ItemStack stack = getActiveItem();
		int max = stack.getMaxUseTime((LivingEntity) (Object) this);
		return MathHelper.floor(max - (max - original) / MultiplyChargeTimeEffect.getMultiplier(getRandom(), stack));
	}
}
