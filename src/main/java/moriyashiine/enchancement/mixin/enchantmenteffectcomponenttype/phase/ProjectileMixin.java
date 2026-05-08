/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.phase;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.PhaseComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Projectile.class)
public class ProjectileMixin {
	@ModifyReturnValue(method = "spawnProjectile(Lnet/minecraft/world/entity/projectile/Projectile;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;Ljava/util/function/Consumer;)Lnet/minecraft/world/entity/projectile/Projectile;", at = @At("RETURN"))
	private static <T extends Projectile> Projectile enchancement$phase(T original) {
		if (original.getOwner() instanceof LivingEntity owner) {
			PhaseComponent.maybeSet(owner, owner.getActiveItem(), original);
		}
		return original;
	}
}
