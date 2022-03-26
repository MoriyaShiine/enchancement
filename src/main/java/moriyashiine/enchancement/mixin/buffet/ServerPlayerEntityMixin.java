package moriyashiine.enchancement.mixin.buffet;

import moriyashiine.enchancement.common.EnchantmentEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	@Shadow
	protected abstract void consumeItem();

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$buffetEatingSpeed(CallbackInfo ci) {
		EnchantmentEffects.tickBuffet(ServerPlayerEntity.class.cast(this), this::consumeItem);
	}
}
