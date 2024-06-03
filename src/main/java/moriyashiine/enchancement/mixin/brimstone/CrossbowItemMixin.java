/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.client.payload.PlayBrimstoneSoundPayload;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
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
	public abstract int getMaxUseTime(ItemStack stack);

	@Shadow
	private static float getPullProgress(int useTicks, ItemStack stack) {
		throw new UnsupportedOperationException();
	}

	@WrapOperation(method = "onStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;getPullProgress(ILnet/minecraft/item/ItemStack;)F"))
	private float enchancement$brimstone(int useTicks, ItemStack stack, Operation<Float> original, ItemStack stack0, World world, LivingEntity user, int remainingUseTicks) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack)) {
			stack.remove(ModDataComponentTypes.BRIMSTONE_UUID);
			int damage = EnchancementUtil.getBrimstoneDamage(getPullProgress(getMaxUseTime(stack) - remainingUseTicks, stack));
			if (damage > 0) {
				stack.set(ModDataComponentTypes.BRIMSTONE_DAMAGE, damage);
				return 1;
			}
		}
		return original.call(useTicks, stack);
	}

	@Inject(method = "usageTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
	private void enchancement$brimstone(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BRIMSTONE, stack)) {
			UUID[] uuid = {stack.get(ModDataComponentTypes.BRIMSTONE_UUID)};
			if (uuid[0] == null) {
				uuid[0] = UUID.randomUUID();
				stack.set(ModDataComponentTypes.BRIMSTONE_UUID, uuid[0]);
			}
			if (remainingUseTicks == getMaxUseTime(stack)) {
				PlayerLookup.tracking(user).forEach(foundPlayer -> PlayBrimstoneSoundPayload.send(foundPlayer, user.getId(), uuid[0]));
				if (user instanceof ServerPlayerEntity player) {
					PlayBrimstoneSoundPayload.send(player, user.getId(), uuid[0]);
				}
			}
		}
	}

	@ModifyReturnValue(method = "getPullTime", at = @At("RETURN"))
	private static int enchancement$brimstone(int original, ItemStack stack) {
		int level = EnchantmentHelper.getLevel(ModEnchantments.BRIMSTONE, stack);
		if (level > 0) {
			return (int) Math.max(1, original * 4.8F / level);
		}
		return original;
	}

	@WrapOperation(method = "shoot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"))
	private void enchancement$brimstone(World instance, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, Operation<Void> original, LivingEntity shooter) {
		int damage = shooter.getEquippedStack(LivingEntity.getSlotForHand(shooter.getActiveHand())).getOrDefault(ModDataComponentTypes.BRIMSTONE_DAMAGE, 0);
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
