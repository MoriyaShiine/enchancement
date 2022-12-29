/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.impact;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "pushAwayFrom", at = @At("HEAD"))
	private void enchancement$impact(Entity entity, CallbackInfo ci) {
		if (!world.isClient) {
			ModEntityComponents.IMPACT.maybeGet(entity).ifPresent(impactComponent -> {
				if (impactComponent.isFalling() && EnchancementUtil.shouldHurt(entity, this)) {
					damage(DamageSource.anvil(entity), 10);
				}
			});
		}
	}
}
