/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmententityeffecttype.freeze.client;

import moriyashiine.enchancement.common.component.entity.enchantmenteffecttype.FrozenSquidComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.client.renderer.entity.state.SquidRenderState;
import net.minecraft.world.entity.animal.squid.Squid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidRenderer.class)
public class SquidRendererMixin<T extends Squid> {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/animal/squid/Squid;Lnet/minecraft/client/renderer/entity/state/SquidRenderState;F)V", at = @At("TAIL"))
	private void enchancement$freeze(T entity, SquidRenderState state, float partialTicks, CallbackInfo ci) {
		if (EnchancementEntityComponents.FROZEN.get(entity).isFrozen()) {
			FrozenSquidComponent frozenSquid = EnchancementEntityComponents.FROZEN_SQUID.get(entity);
			state.tentacleAngle = frozenSquid.getForcedTentacleAngle();
			state.xBodyRot = frozenSquid.getForcedXBodyRot();
			state.zBodyRot = frozenSquid.getForcedZBodyRot();
		}
	}
}
