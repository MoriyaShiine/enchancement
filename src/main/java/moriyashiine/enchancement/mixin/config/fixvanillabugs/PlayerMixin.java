/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.fixvanillabugs;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
	protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
		super(type, level);
	}

	@WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
	private boolean enchancement$fixVanillaBugs(Player instance) {
		return !ModConfig.fixVanillaBugs;
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;postPiercingAttack()V"))
	private void enchancement$fixVanillaBugs(Entity entity, CallbackInfo ci) {
		if (ModConfig.fixVanillaBugs && level().isClientSide() && getWeaponItem().getItem() instanceof MaceItem mace && entity instanceof LivingEntity victim) {
			mace.postHurtEnemy(getWeaponItem(), victim, this);
		}
	}
}
