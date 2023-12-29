/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.buoy;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyArg(method = "applyMovementInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;updateVelocity(FLnet/minecraft/util/math/Vec3d;)V"))
	private float enchancement$buoy(float value) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, this) && EnchancementUtil.isSubmerged(this, true, false)) {
			value *= 1.5F;
			int depthStriderLevel = EnchantmentHelper.getDepthStrider((LivingEntity) (Object) this);
			while (depthStriderLevel > 0) {
				depthStriderLevel--;
				value *= 1.2F;
			}
		}
		return value;
	}

	@SuppressWarnings("CancellableInjectionUsage")
	@Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
	protected void enchancement$buoy(FluidState state, CallbackInfoReturnable<Boolean> cir) {
	}
}
