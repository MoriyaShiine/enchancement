/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.mixin.leech;

import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isFire()Z", ordinal = 0))
	private void enchancement$leech(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (source.getSource() instanceof TridentEntity trident) {
			ModEntityComponents.LEECH.maybeGet(trident).ifPresent(leechComponent -> {
				if (leechComponent.hasLeech() && leechComponent.getStuckEntity() == null) {
					LivingEntity living = LivingEntity.class.cast(this);
					world.getEntitiesByClass(TridentEntity.class, getBoundingBox().expand(1), foundTrident -> true).forEach(otherTrident -> ModEntityComponents.LEECH.maybeGet(otherTrident).ifPresent(otherLeech -> {
						if (otherLeech.getStuckEntity() == living) {
							otherLeech.setStuckEntityId(-2);
							otherLeech.sync();
						}
					}));
					leechComponent.setStuckEntityId(getId());
					leechComponent.sync();
				}
			});
		}
	}
}
