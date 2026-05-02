/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.entityxray;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TargetGoal.class)
public class TargetGoalMixin {
	@Shadow
	@Final
	protected Mob mob;

	@Definition(id = "mustSee", field = "Lnet/minecraft/world/entity/ai/goal/target/TargetGoal;mustSee:Z")
	@Expression("this.mustSee")
	@ModifyExpressionValue(method = "canContinueToUse", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
	private boolean enchancement$entityXray(boolean original) {
		return original && !EnchancementUtil.hasAnyEnchantmentsWith(mob, ModEnchantmentEffectComponentTypes.ENTITY_XRAY);
	}
}
