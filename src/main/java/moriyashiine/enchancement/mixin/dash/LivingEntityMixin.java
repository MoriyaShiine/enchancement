package moriyashiine.enchancement.mixin.dash;

import moriyashiine.enchancement.common.component.entity.DashComponent;
import moriyashiine.enchancement.common.packet.SyncDashPacket;
import moriyashiine.enchancement.common.registry.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private boolean updateVelocity = false;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyVariable(method = "jump", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", ordinal = 0))
	private Vec3d enchancement$dash(Vec3d value) {
		if (world.isClient) {
			DashComponent dashComponent = ModComponents.DASH.getNullable(this);
			if (dashComponent != null && dashComponent.shouldWavedash()) {
				dashComponent.setDashCooldown(0);
				updateVelocity = true;
				return value.multiply(2.5);
			}
		}
		return value;
	}

	@Inject(method = "jump", at = @At("TAIL"))
	private void enchancement$dash(CallbackInfo ci) {
		if (updateVelocity) {
			SyncDashPacket.send(false, getVelocity());
		}
	}
}
