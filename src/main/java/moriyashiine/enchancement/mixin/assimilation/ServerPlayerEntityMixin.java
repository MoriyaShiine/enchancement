package moriyashiine.enchancement.mixin.assimilation;

import moriyashiine.enchancement.common.EnchantmentEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$assimilationAutoFeed(CallbackInfo ci) {
		EnchantmentEffects.tickAssimilation(ServerPlayerEntity.class.cast(this));
	}
}
