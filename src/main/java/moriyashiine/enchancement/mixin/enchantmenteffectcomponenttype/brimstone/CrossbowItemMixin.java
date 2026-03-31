/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.client.payload.PlayBrimstoneFireSoundPayload;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.world.item.effects.BrimstoneEffect;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
	@Shadow
	abstract CrossbowItem.ChargingSounds getChargingSounds(ItemStack itemStack);

	@Shadow
	private static boolean tryLoadProjectiles(LivingEntity shooter, ItemStack heldItem) {
		return false;
	}

	@Shadow
	public abstract int getUseDuration(ItemStack itemStack, LivingEntity user);

	@WrapOperation(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;getPowerForTime(ILnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)F"))
	private float enchancement$brimstone(int timeHeld, ItemStack itemStack, LivingEntity holder, Operation<Float> original, ItemStack itemStack0, Level level, LivingEntity entity, int remainingTime) {
		if (EnchantmentHelper.has(itemStack, ModEnchantmentEffectComponentTypes.BRIMSTONE)) {
			itemStack.remove(ModComponentTypes.BRIMSTONE_UUID);
			int damage = BrimstoneEffect.getBrimstoneDamage(CrossbowItem.getPowerForTime(getUseDuration(itemStack, holder) - remainingTime, itemStack, holder));
			if (damage > 0 && !CrossbowItem.isCharged(itemStack) && tryLoadProjectiles(holder, itemStack)) {
				itemStack.set(ModComponentTypes.BRIMSTONE_DAMAGE, damage);
				getChargingSounds(itemStack).end().ifPresent(sound -> level.playSound(null, holder.getX(), holder.getY(), holder.getZ(), sound.value(), holder.getSoundSource(), 1, 1 / (level.getRandom().nextFloat() * 0.5F + 1) + 0.2F));
				return 1;
			}
		}
		return original.call(timeHeld, itemStack, holder);
	}

	@Inject(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;getChargingSounds(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/CrossbowItem$ChargingSounds;"))
	private void enchancement$brimstone(Level level, LivingEntity entity, ItemStack itemStack, int ticksRemaining, CallbackInfo ci) {
		if (EnchantmentHelper.has(itemStack, ModEnchantmentEffectComponentTypes.BRIMSTONE)) {
			UUID[] uuid = {itemStack.get(ModComponentTypes.BRIMSTONE_UUID)};
			if (uuid[0] == null) {
				uuid[0] = UUID.randomUUID();
				itemStack.set(ModComponentTypes.BRIMSTONE_UUID, uuid[0]);
			}
			if (ticksRemaining == getUseDuration(itemStack, entity)) {
				PlayerLookup.tracking(entity).forEach(foundPlayer -> PlayBrimstoneFireSoundPayload.send(foundPlayer, entity, uuid[0]));
				if (entity instanceof ServerPlayer player) {
					PlayBrimstoneFireSoundPayload.send(player, entity, uuid[0]);
				}
			}
		}
	}

	@ModifyExpressionValue(method = "onUseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;isCharged(Lnet/minecraft/world/item/ItemStack;)Z"))
	private boolean enchancement$brimstone(boolean original, Level level, LivingEntity entity, ItemStack itemStack) {
		return original || EnchantmentHelper.has(itemStack, ModEnchantmentEffectComponentTypes.BRIMSTONE);
	}

	@ModifyReturnValue(method = "getChargeDuration", at = @At("RETURN"))
	private static int enchancement$brimstone(int original, ItemStack crossbow, LivingEntity user) {
		float value = BrimstoneEffect.getChargeTimeMultiplier(user.getRandom(), crossbow);
		if (value > 0) {
			return (int) Math.max(1, original * 2.4F / value);
		}
		return original;
	}

	@WrapOperation(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"))
	private void enchancement$brimstone(Level instance, Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, Operation<Void> original, LivingEntity livingEntity) {
		int damage = livingEntity.getItemBySlot(livingEntity.getUsedItemHand().asEquipmentSlot()).getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
		if (damage > 0) {
			sound = getFireSound(damage);
		}
		original.call(instance, except, x, y, z, sound, source, volume, pitch);
	}

	@Unique
	private static SoundEvent getFireSound(int damage) {
		if (damage >= 12) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_6;
		} else if (damage >= 10) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_5;
		} else if (damage >= 8) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_4;
		} else if (damage >= 6) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_3;
		} else if (damage >= 4) {
			return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_2;
		}
		return ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_1;
	}
}
