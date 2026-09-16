package moriyashiine.enchancement.mixin.item.maceeffect.client;

import moriyashiine.enchancement.client.renderer.entity.state.UsingMaceRenderState;
import moriyashiine.enchancement.common.component.entity.internal.UsingMaceComponent;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.item.ItemStack;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
	@Inject(method = "getArmPose(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"), cancellable = true)
	private static void enchancement$maceEffect(Avatar avatar, ItemStack itemInHand, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
		if (hand == InteractionHand.MAIN_HAND) {
			for (ComponentKey<?> key : avatar.asComponentProvider().getComponentContainer().keys()) {
				if (avatar.getComponent(key) instanceof UsingMaceComponent usingMaceComponent && usingMaceComponent.isUsing()) {
					cir.setReturnValue(HumanoidModel.ArmPose.THROW_TRIDENT);
					return;
				}
			}
		}
	}

	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
	private void enchancement$maceEffect(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
		UsingMaceRenderState usingMaceRenderState = new UsingMaceRenderState();
		for (ComponentKey<?> key : entity.asComponentProvider().getComponentContainer().keys()) {
			if (entity.getComponent(key) instanceof UsingMaceComponent usingMaceComponent && usingMaceComponent.isUsing()) {
				usingMaceRenderState.usingMace = true;
				break;
			}
		}
		state.setData(UsingMaceRenderState.KEY, usingMaceRenderState);
	}
}
