package moriyashiine.enchancement.mixin.frostbite;

import moriyashiine.enchancement.common.enchantment.FrostbiteEnchantment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Inject(method = "applyDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/player/PlayerEntity;getHealth()F", ordinal = 0))
	private void enchancement$frostbite(DamageSource source, float amount, CallbackInfo ci) {
		FrostbiteEnchantment.applyEffect(PlayerEntity.class.cast(this), source, amount);
	}
}
