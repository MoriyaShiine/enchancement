/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.enhancemobs.directionburst;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.DirectionBurstPayload;
import moriyashiine.enchancement.common.world.item.effects.DirectionBurstEffect;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
	@Inject(method = "doPostAttackEffectsWithItemSourceOnBreak", at = @At("TAIL"))
	private static void enchancement$enhanceMobs(ServerLevel serverLevel, Entity victim, DamageSource damageSource, @Nullable ItemStack source, @Nullable Consumer<Item> attackerlessOnBreak, CallbackInfo ci) {
		if (ModConfig.enhanceMobs) {
			if (damageSource.getEntity() instanceof Mob mob) {
				DirectionBurstComponent directionBurstComponent = ModEntityComponents.DIRECTION_BURST.get(mob);
				if (directionBurstComponent.hasEffect() && directionBurstComponent.canUse()) {
					float strength = mob.onGround() ? DirectionBurstEffect.getGroundStrength(mob) : DirectionBurstEffect.getAirStrength(mob);
					Vec3 inputDelta = source != null && source.is(ConventionalItemTags.RANGED_WEAPON_TOOLS) ? switch (mob.getRandom().nextInt(4)) {
						case 3 -> new Vec3(strength, 0, 0);
						case 2 -> new Vec3(-strength, 0, 0);
						case 1 -> new Vec3(0, 0, strength);
						default -> new Vec3(0, 0, -strength);
					} : new Vec3(strength, 0, 0);
					Vec3 delta = directionBurstComponent.createDelta(inputDelta);
					DirectionBurstPayload.use(mob, delta, directionBurstComponent);
				}
			}
		}
	}
}
