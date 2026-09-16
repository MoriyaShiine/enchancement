package moriyashiine.enchancement.mixin.item.maceeffect.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.client.renderer.entity.state.UsingMaceRenderState;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.item.ItemUseAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemsRendererMixin {
	@ModifyExpressionValue(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/ItemUseAnimation;"))
	private ItemUseAnimation enchancement$maceEffect(ItemUseAnimation original, PlayerRenderState playerState) {
		if (playerState.avatarRenderState != null) {
			UsingMaceRenderState usingMaceRenderState = playerState.avatarRenderState.getData(UsingMaceRenderState.KEY);
			if (usingMaceRenderState != null && usingMaceRenderState.usingMace) {
				return ItemUseAnimation.TRIDENT;
			}
		}
		return original;
	}
}
