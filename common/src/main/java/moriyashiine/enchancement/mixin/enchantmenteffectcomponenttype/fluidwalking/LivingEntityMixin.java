package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.fluidwalking;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype.BoostInFluidComponent;
import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.module.SLibUtils;
import moriyashiine.strawberrylib.api.objects.enums.SubmersionGate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@ModifyReturnValue(method = "getEffectiveGravity", at = @At("RETURN"))
	private double enchancement$fluidWalking(double original) {
		if (EnchancementUtil.hasAnyEnchantmentsWith(this, EnchancementEnchantmentEffectComponentTypes.FLUID_WALKING) && SLibUtils.isSubmerged(this, SubmersionGate.ALL)) {
			BoostInFluidComponent boostInFluid = EnchancementEntityComponents.BOOST_IN_FLUID.getNullable(this);
			if (boostInFluid == null || !boostInFluid.isBoosting()) {
				return original / 3;
			}
		}
		return original;
	}

	@ModifyReturnValue(method = "getLiquidCollisionShape", at = @At("RETURN"))
	private VoxelShape enchancement$fluidWalking(VoxelShape original) {
		return EnchancementUtil.shouldFluidWalk(this) ? EnchancementUtil.FLUID_WALKING_SHAPE : original;
	}

	@ModifyExpressionValue(method = "shouldTravelInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;canStandOnFluid(Lnet/minecraft/world/level/material/FluidState;)Z"))
	protected boolean enchancement$fluidWalking(boolean original) {
		return original || EnchancementUtil.shouldFluidWalk(this);
	}

	@Inject(method = "floatInWaterWhileRidden", at = @At("HEAD"), cancellable = true)
	private void enchancement$fluidWalking(CallbackInfo ci) {
		if (EnchancementUtil.hasAnyEnchantmentsWith(this, EnchancementEnchantmentEffectComponentTypes.FLUID_WALKING)) {
			ci.cancel();
		}
	}
}
