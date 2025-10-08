/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@SuppressWarnings("InvokeAssignCanReplacedWithExpression")
@Mixin(value = PlayerEntity.class, priority = 1001)
public abstract class PlayerEntityMixin extends LivingEntity {
	@Unique
	private Predicate<ItemStack> heldItemsPredicate = null;

	@Unique
	private ItemStack lastStack = ItemStack.EMPTY;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getHeldProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$rebalanceEquipmentHeld(Predicate<ItemStack> value) {
		if (ModConfig.rebalanceEquipment) {
			heldItemsPredicate = value;
		}
		return value;
	}

	@ModifyVariable(method = "getProjectileType", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/RangedWeaponItem;getProjectiles()Ljava/util/function/Predicate;"))
	private Predicate<ItemStack> enchancement$rebalanceEquipment(Predicate<ItemStack> value) {
		if (heldItemsPredicate != null) {
			value = value.or(heldItemsPredicate);
			heldItemsPredicate = null;
		}
		return value;
	}

	@WrapOperation(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/registry/entry/RegistryEntry;)D", ordinal = 1))
	private double enchancement$rebalanceEquipment(PlayerEntity instance, RegistryEntry<EntityAttribute> registryEntry, Operation<Double> original) {
		double value = original.call(instance, registryEntry);
		if (ModConfig.rebalanceEquipment) {
			return Math.max(0.5F, value);
		}
		return value;
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(CallbackInfo ci) {
		lastStack = getMainHandStack();
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void enchancement$rebalanceEquipment(Entity target, CallbackInfo ci) {
		if (!ItemStack.areEqual(lastStack, getMainHandStack())) {
			ci.cancel();
		}
	}
}
