/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.berserk;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
	private float enchancement$berserk(float value, DamageSource source) {
		if (source.getSource() instanceof LivingEntity living && !living.getWorld().isClient) {
			return value + EnchancementUtil.getBonusBerserkDamage(living, living.getMainHandStack());
		}
		return value;
	}
}
