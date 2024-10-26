/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.nightvision.client;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
	@Unique
	private static boolean checkingStrength = false;

	@Shadow
	public static float getNightVisionStrength(LivingEntity entity, float tickDelta) {
		return 0;
	}

	@Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
	private static void enchancement$nightVision(LivingEntity entity, float tickDelta, CallbackInfoReturnable<Float> cir) {
		if (checkingStrength) {
			return;
		}
		float original = 0;
		if (entity.getActiveStatusEffects().containsKey(StatusEffects.NIGHT_VISION)) {
			checkingStrength = true;
			original = getNightVisionStrength(entity, tickDelta);
			checkingStrength = false;
		}
		float strength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.NIGHT_VISION, entity, 0);
		if (strength > 0) {
			cir.setReturnValue(Math.max(original, strength));
		}
	}
}
