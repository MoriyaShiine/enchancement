package moriyashiine.enchancement.mixin.vanillachanges.negateenderpearldamage;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin {
	@ModifyArg(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
	private float enchancement$negateEnderPearlDamage(float value) {
		if (Enchancement.getConfig().negateEnderPearlDamage) {
			return 0;
		}
		return value;
	}
}
