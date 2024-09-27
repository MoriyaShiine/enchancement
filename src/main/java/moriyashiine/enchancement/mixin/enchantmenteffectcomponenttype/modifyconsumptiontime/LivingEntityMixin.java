/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.modifyconsumptiontime;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	protected ItemStack activeItemStack;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@WrapOperation(method = "setCurrentHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxUseTime(Lnet/minecraft/entity/LivingEntity;)I"))
	private int enchancement$modifyConsumptionTimeHand(ItemStack instance, LivingEntity user, Operation<Integer> original) {
		return modifyTimeLeft(original.call(instance, user));
	}

	@WrapOperation(method = "onTrackedDataSet", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getMaxUseTime(Lnet/minecraft/entity/LivingEntity;)I"))
	private int enchancement$modifyConsumptionTimeSet(ItemStack instance, LivingEntity user, Operation<Integer> original) {
		return modifyTimeLeft(original.call(instance, user));
	}

	@Unique
	private int modifyTimeLeft(int original) {
		if (activeItemStack.contains(DataComponentTypes.FOOD) || activeItemStack.getUseAction() == UseAction.DRINK) {
			float modifiedTime = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MODIFY_CONSUMPTION_TIME, ((LivingEntity) (Object) this), original);
			if (modifiedTime != original) {
				return Math.max(1, MathHelper.floor(modifiedTime));
			}
		}
		return original;
	}
}
