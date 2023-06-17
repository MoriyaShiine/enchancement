/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.vanillachanges.safechanneling;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LightningEntity.class)
public class LightningEntityMixin {
	@ModifyVariable(method = "tick", at = @At(value = "STORE", target = "Lnet/minecraft/world/World;getOtherEntities(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;)Ljava/util/List;"))
	private List<Entity> enchancement$safeChanneling(List<Entity> list) {
		if (ModEntityComponents.CHANNELING.get(this).isSafe()) {
			for (int i = list.size() - 1; i >= 0; i--) {
				if (list.get(i) instanceof ItemEntity) {
					list.remove(i);
				}
			}
		}
		return list;
	}

	@Inject(method = "spawnFire", at = @At("HEAD"), cancellable = true)
	private void enchancement$safeChanneling(int spreadAttempts, CallbackInfo ci) {
		if (ModEntityComponents.CHANNELING.get(this).isSafe()) {
			ci.cancel();
		}
	}
}
