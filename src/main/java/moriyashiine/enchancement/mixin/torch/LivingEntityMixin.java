/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.torch;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@ModifyExpressionValue(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 4))
	private boolean enchancement$torch(boolean value, DamageSource source) {
		if (source.getSource() instanceof TorchEntity) {
			return true;
		}
		return value;
	}
}
