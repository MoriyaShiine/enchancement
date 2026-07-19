package moriyashiine.enchancement.mixin.api.event;

import moriyashiine.enchancement.api.event.CappedMultiplyDeltaMovementEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@ModifyArg(method = "handleRelativeFrictionAndCalculateMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;moveRelative(FLnet/minecraft/world/phys/Vec3;)V"))
	private float enchancement$cappedMultiplyDeltaMovement(float speed) {
		return speed * CappedMultiplyDeltaMovementEvent.getMovementMultiplier((LivingEntity) (Object) this, 1);
	}
}
