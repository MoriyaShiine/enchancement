/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.phase;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.PhaseComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyExpressionValue(method = "applyItemBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/AbstractArrow;getPierceLevel()B"))
	private byte enchancement$phase(byte original, ServerLevel level, @Local(name = "abstractArrow") AbstractArrow abstractArrow) {
		PhaseComponent phaseComponent = ModEntityComponents.PHASE.get(abstractArrow);
		if (phaseComponent.bypassShields()) {
			level.gameEvent(GameEvent.TELEPORT, abstractArrow.position(), GameEvent.Context.of(abstractArrow));
			level.sendParticles(ParticleTypes.REVERSE_PORTAL, abstractArrow.getX(), abstractArrow.getY(), abstractArrow.getZ(), 6, abstractArrow.getBbWidth() / 2, abstractArrow.getBbHeight() / 2, abstractArrow.getBbWidth() / 2, 0);
			SLibUtils.playSound(abstractArrow, ModSoundEvents.GENERIC_TELEPORT, 0.75F, 1);
			phaseComponent.disable();
			return 1;
		}
		return original;
	}
}
