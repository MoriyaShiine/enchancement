/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceconsumables;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin extends Projectile {
	@Shadow
	public abstract ItemStack getPickupItem();

	@Shadow
	public abstract byte getPierceLevel();

	@Shadow
	public AbstractArrow.Pickup pickup;

	public AbstractArrowMixin(EntityType<? extends Projectile> type, Level level) {
		super(type, level);
	}

	@Inject(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
	private void enchancement$rebalanceConsumables(EntityHitResult hitResult, CallbackInfo ci) {
		if (level() instanceof ServerLevel level && shouldApply()) {
			Entity entity = hitResult.getEntity();
			if (entity instanceof EnderDragonPart part) {
				entity = part.parentMob;
			}
			if (entity instanceof LivingEntity living) {
				ItemStack stack = getPickupItem();
				if (stack.is(Items.TIPPED_ARROW)) {
					stack = Items.ARROW.getDefaultInstance();
				}
				if (stack.is(ItemTags.ARROWS)) {
					ItemEntity drop = living.spawnAtLocation(level, stack, 1);
					if (drop != null && getOwner() != null) {
						drop.setTarget(getOwner().getUUID());
					}
				}
			}
		}
	}

	@WrapWithCondition(method = "onHitEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setArrowCount(I)V"))
	private boolean enchancement$rebalanceConsumables(LivingEntity instance, int count) {
		return !shouldApply();
	}

	@Unique
	private boolean shouldApply() {
		return ModConfig.rebalanceConsumables && pickup == AbstractArrow.Pickup.ALLOWED && getPierceLevel() == 0 && getOwner() instanceof Player;
	}
}
