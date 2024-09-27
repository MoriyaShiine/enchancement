/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.entityxray.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Shadow
	@Nullable
	public ClientPlayerEntity player;

	@ModifyReturnValue(method = "hasOutline", at = @At("RETURN"))
	private boolean enchancement$entityXray(boolean original, Entity entity) {
		if (!original && player != null && entity instanceof LivingEntity living && !living.isSneaking() && !living.isInvisible()) {
			float distance = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.ENTITY_XRAY, player, 0);
			if (distance > 0 && entity.distanceTo(player) < distance && !EnchancementUtil.hasAnyEnchantmentsWith(living, ModEnchantmentEffectComponentTypes.HIDE_LABEL_BEHIND_WALLS) && !living.canSee(player)) {
				return true;
			}
		}
		return original;
	}
}
