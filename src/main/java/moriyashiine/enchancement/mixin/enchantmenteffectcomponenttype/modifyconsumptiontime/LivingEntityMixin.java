/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.modifyconsumptiontime;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	protected ItemStack useItem;

	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@WrapOperation(method = "startUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"))
	private int enchancement$modifyConsumptionTimeHand(ItemStack instance, LivingEntity user, Operation<Integer> original) {
		return modifyTimeLeft(original.call(instance, user));
	}

	@WrapOperation(method = "onSyncedDataUpdated", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration(Lnet/minecraft/world/entity/LivingEntity;)I"))
	private int enchancement$modifyConsumptionTimeSet(ItemStack instance, LivingEntity user, Operation<Integer> original) {
		return modifyTimeLeft(original.call(instance, user));
	}

	@Unique
	private int modifyTimeLeft(int original) {
		if (useItem.has(DataComponents.CONSUMABLE)) {
			float modifiedTime = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MODIFY_CONSUMPTION_TIME, ((LivingEntity) (Object) this), original);
			if (modifiedTime != original) {
				return Math.max(1, Mth.floor(modifiedTime));
			}
		}
		return original;
	}
}
