/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.buoy;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@SuppressWarnings("CancellableInjectionUsage")
	@Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
	protected void enchancement$buoy(FluidState state, CallbackInfoReturnable<Boolean> cir) {
	}

	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float enchancement$buoy(float value) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, this) && ModEntityComponents.EXTENDED_WATER.get(this).getTicksWet() > 0) {
			return Math.max(0, value - 3);
		}
		return value;
	}
}
