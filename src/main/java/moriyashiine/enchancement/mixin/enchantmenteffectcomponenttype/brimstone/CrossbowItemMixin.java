/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.brimstone;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.client.payload.PlayBrimstoneFireSoundPayload;
import moriyashiine.enchancement.common.enchantment.effect.BrimstoneEffect;
import moriyashiine.enchancement.common.init.ModComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
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
	public abstract int getMaxUseTime(ItemStack stack, LivingEntity user);

	@Shadow
	private static float getPullProgress(int useTicks, ItemStack stack, LivingEntity user) {
		throw new UnsupportedOperationException();
	}

	@WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullProgress(ILnet/minecraft/item/ItemStack;Lnet/minecraft/entity/LivingEntity;)F"))
	private float enchancement$brimstone(int useTicks, ItemStack stack, LivingEntity user, Operation<Float> original, ItemStack stack0, World world, LivingEntity user0, int remainingUseTicks) {
		if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.BRIMSTONE)) {
			stack.remove(ModComponentTypes.BRIMSTONE_UUID);
			int damage = BrimstoneEffect.getBrimstoneDamage(getPullProgress(getMaxUseTime(stack, user) - remainingUseTicks, stack, user));
			if (damage > 0) {
				stack.set(ModComponentTypes.BRIMSTONE_DAMAGE, damage);
				return 1;
			}
		}
		return original.call(useTicks, stack, user);
	}

	@Inject(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getLoadingSounds(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/CrossbowItem$LoadingSounds;"))
	private void enchancement$brimstone(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
		if (EnchantmentHelper.hasAnyEnchantmentsWith(stack, ModEnchantmentEffectComponentTypes.BRIMSTONE)) {
			UUID[] uuid = {stack.get(ModComponentTypes.BRIMSTONE_UUID)};
			if (uuid[0] == null) {
				uuid[0] = UUID.randomUUID();
				stack.set(ModComponentTypes.BRIMSTONE_UUID, uuid[0]);
			}
			if (remainingUseTicks == getMaxUseTime(stack, user)) {
				PlayerLookup.tracking(user).forEach(foundPlayer -> PlayBrimstoneFireSoundPayload.send(foundPlayer, user.getId(), uuid[0]));
				if (user instanceof ServerPlayerEntity player) {
					PlayBrimstoneFireSoundPayload.send(player, user.getId(), uuid[0]);
				}
			}
		}
	}

	@ModifyReturnValue(method = "getPullTime", at = @At("RETURN"))
	private static int enchancement$brimstone(int original, ItemStack stack, LivingEntity user) {
		float value = BrimstoneEffect.getChargeTimeMultiplier(user.getRandom(), stack);
		if (value > 0) {
			return (int) Math.max(1, original * 2.4F / value);
		}
		return original;
	}

	@WrapOperation(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private void enchancement$brimstone(World instance, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, Operation<Void> original, LivingEntity shooter) {
		int damage = shooter.getEquippedStack(LivingEntity.getSlotForHand(shooter.getActiveHand())).getOrDefault(ModComponentTypes.BRIMSTONE_DAMAGE, 0);
		if (damage > 0) {
			sound = getFireSound(damage);
		}
		original.call(instance, source, x, y, z, sound, category, volume, pitch);
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
