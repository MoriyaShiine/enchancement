package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.modifydetectionrange;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.tag.EnchancementEntityTypeTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyArg(method = "getVisibilityPercent", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(DDD)D"), index = 0)
	private double enchancement$modifyDetectionRange(double original, @Local(argsOnly = true) @Nullable Entity targetingEntity) {
		if (targetingEntity == null || (targetingEntity instanceof Enemy && !targetingEntity.is(EnchancementEntityTypeTags.VEIL_IMMUNE))) {
			return EnchancementUtil.getValue(EnchancementEnchantmentEffectComponentTypes.MODIFY_DETECTION_RANGE, (LivingEntity) (Object) this, (float) original);
		}
		return original;
	}
}
