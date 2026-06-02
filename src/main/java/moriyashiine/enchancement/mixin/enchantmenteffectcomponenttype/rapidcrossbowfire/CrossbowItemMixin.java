/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rapidcrossbowfire;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin extends ProjectileWeaponItem {
	@Shadow
	abstract CrossbowItem.ChargingSounds getChargingSounds(ItemStack itemStack);

	@Shadow
	private static float getShootingPower(ChargedProjectiles projectiles) {
		throw new UnsupportedOperationException("Implemented via mixin");
	}

	public CrossbowItemMixin(Properties properties) {
		super(properties);
	}

	@Inject(method = "onUseTick", at = @At("HEAD"), cancellable = true)
	private void enchancement$rapidCrossbowFire(Level level, LivingEntity entity, ItemStack itemStack, int ticksRemaining, CallbackInfo ci) {
		if (EnchantmentHelper.has(itemStack, ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			ItemStack projectile = entity.getProjectile(itemStack);
			if (projectile.isEmpty()) {
				releaseUsing(itemStack, level, entity, ticksRemaining);
			} else {
				itemStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.of(new ItemStackTemplate(projectile.getItem(), projectile.getComponentsPatch())));
				if (ticksRemaining % 4 == 0) {
					List<ItemStack> firedProjectiles = draw(itemStack, projectile, entity);
					if (level instanceof ServerLevel serverLevel && !firedProjectiles.isEmpty()) {
						if (entity instanceof ServerPlayer player) {
							CriteriaTriggers.SHOT_CROSSBOW.trigger(player, itemStack);
							player.awardStat(Stats.ITEM_USED.get(this));
						}
						CrossbowItem.ChargingSounds sounds = getChargingSounds(itemStack);
						sounds.start().ifPresent(sound -> level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound.value(), entity.getSoundSource(), 0.5F, 1));
						sounds.end().ifPresent(sound -> level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), sound.value(), entity.getSoundSource(), 1, 1 / (level.getRandom().nextFloat() * 0.5F + 1) + 0.2F));
						shoot(serverLevel, entity, entity.getUsedItemHand(), itemStack, firedProjectiles, getShootingPower(ChargedProjectiles.EMPTY), 1, entity.slib$isPlayer(), null);
					}
				}
			}
			ci.cancel();
		}
	}

	@Inject(method = "releaseUsing", at = @At("HEAD"), cancellable = true)
	private void enchancement$rapidCrossbowFire(ItemStack itemStack, Level level, LivingEntity entity, int remainingTime, CallbackInfoReturnable<Boolean> cir) {
		if (EnchantmentHelper.has(itemStack, ModEnchantmentEffectComponentTypes.RAPID_CROSSBOW_FIRE)) {
			itemStack.set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
			entity.stopUsingItem();
			cir.setReturnValue(false);
		}
	}
}
