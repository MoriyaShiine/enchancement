package moriyashiine.enchancement.mixin.config.rebalanceenchantments.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.renderer.entity.state.EnchantedFireRenderState;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SubmitNodeCollection.class)
public class SubmitNodeCollectionMixin {
	@ModifyArg(method = "submitFlame", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
	private <E> E enchancement$rebalanceEnchantments(E e, @Local(argsOnly = true) EntityRenderState renderState) {
		if (e instanceof EnchantedFireRenderState.Submit submit) {
			@Nullable EnchantedFireRenderState enchantedFireRenderState = renderState.getData(EnchantedFireRenderState.KEY);
			if (enchantedFireRenderState != null) {
				submit.enchancement$setRenderEnchantedFire(enchantedFireRenderState.renderEnchantedFire);
			}
		}
		return e;
	}
}
