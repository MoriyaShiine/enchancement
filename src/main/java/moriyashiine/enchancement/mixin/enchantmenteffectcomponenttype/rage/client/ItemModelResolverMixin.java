/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.rage.client;

import moriyashiine.enchancement.client.renderer.item.state.RageRenderState;
import moriyashiine.enchancement.common.world.item.effects.RageEffect;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
	@Inject(method = "appendItemLayers", at = @At("TAIL"))
	private void enchancement$rage(ItemStackRenderState output, ItemStack item, ItemDisplayContext displayContext, Level level, ItemOwner owner, int seed, CallbackInfo ci) {
		RageRenderState rageRenderState = new RageRenderState();
		if (owner != null && owner.asLivingEntity() instanceof LivingEntity living) {
			rageRenderState.color = RageEffect.getColor(living, item);
		}
		output.setData(RageRenderState.KEY, rageRenderState);
	}
}
