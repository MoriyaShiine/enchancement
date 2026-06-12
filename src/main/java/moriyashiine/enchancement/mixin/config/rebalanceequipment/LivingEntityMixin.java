/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@SuppressWarnings("ConstantValue")
	@ModifyArg(method = "canGlide", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canGlideUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/EquipmentSlot;)Z"))
	private ItemStack enchancement$rebalanceEquipment(ItemStack itemStack) {
		if (EnchancementConfig.rebalanceEquipment && (Object) this instanceof Player player && player.getCooldowns().isOnCooldown(itemStack)) {
			return ItemStack.EMPTY;
		}
		return itemStack;
	}

	@Inject(method = "hurtServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/component/BlocksAttacks;onBlocked(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/LivingEntity;)V"))
	private void enchancement$rebalanceEquipment(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Float> cir, @Local(name = "itemInUse") ItemStack itemInUse, @Local(name = "damageBlocked") float damageBlocked) {
		if (EnchancementConfig.rebalanceEquipment) {
			EnchancementEntityComponents.LIMIT_BLOCKS_ATTACKS.maybeGet(this).ifPresent(limitBlocksAttacks -> {
				float toDamage = damageBlocked;
				boolean player = false;
				if (source.getDirectEntity() instanceof LivingEntity attacker) {
					toDamage += attacker.getSecondsToDisableBlocking();
					player = attacker.slib$isPlayer();
				}
				limitBlocksAttacks.damage(itemInUse, toDamage, player);
				limitBlocksAttacks.sync();
			});
		}
	}
}
