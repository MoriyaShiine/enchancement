/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.hidenamebehindwalls.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
	@Unique
	private static final Minecraft client = Minecraft.getInstance();

	@ModifyReturnValue(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At("RETURN"))
	private boolean enchancement$hideNameBehindWalls(boolean original, T entity) {
		Player player = client.player;
		if (player != null && !player.isSpectator()) {
			if (!entity.isCurrentlyGlowing() && EnchancementUtil.hasAnyEnchantmentsWith(entity, ModEnchantmentEffectComponentTypes.HIDE_NAME_BEHIND_WALLS) && !EnchancementUtil.hasAnyEnchantmentsWith(player, ModEnchantmentEffectComponentTypes.ENTITY_XRAY) && !player.hasLineOfSight(entity)) {
				return false;
			}
		}
		return original;
	}
}
