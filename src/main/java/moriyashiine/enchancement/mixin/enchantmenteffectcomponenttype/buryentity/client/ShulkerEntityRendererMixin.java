/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.buryentity.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.render.entity.ShulkerEntityRenderer;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerEntityRenderer.class)
public class ShulkerEntityRendererMixin {
	@ModifyReturnValue(method = "getPositionOffset(Lnet/minecraft/entity/mob/ShulkerEntity;F)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"))
	private Vec3d enchancement$buryEntity(Vec3d original, ShulkerEntity shulkerEntity) {
		if (ModEntityComponents.BURY_ENTITY.get(shulkerEntity).getBuryPos() != null) {
			return original.add(0, -0.5, 0);
		}
		return original;
	}
}
