package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.lightningdash.client;

import moriyashiine.enchancement.client.renderer.entity.state.LightningDashRenderState;
import moriyashiine.enchancement.common.world.item.effects.LightningDashEffect;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
	private void enchancement$lightningDash(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		LightningDashRenderState lightningDashRenderState = new LightningDashRenderState();
		if (entity instanceof LivingEntity living && LightningDashEffect.getFloatTime(entity.getRandom(), living.getUseItem()) != 0) {
			lightningDashRenderState.usingLightningDash = true;
		}
		state.setData(LightningDashRenderState.KEY, lightningDashRenderState);
	}
}
