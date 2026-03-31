/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.config.rebalanceenchantments;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LightningBolt.class)
public class LightningBoltMixin {
	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private List<Entity> enchancement$rebalanceEnchantments(List<Entity> original) {
		if (ModEntityComponents.SAFE_LIGHTNING.get(this).isSafe()) {
			for (int i = original.size() - 1; i >= 0; i--) {
				if (original.get(i) instanceof ItemEntity) {
					original.remove(i);
				}
			}
		}
		return original;
	}

	@Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
	private void enchancement$rebalanceEnchantments(int additionalSources, CallbackInfo ci) {
		if (ModEntityComponents.SAFE_LIGHTNING.get(this).isSafe()) {
			ci.cancel();
		}
	}
}
