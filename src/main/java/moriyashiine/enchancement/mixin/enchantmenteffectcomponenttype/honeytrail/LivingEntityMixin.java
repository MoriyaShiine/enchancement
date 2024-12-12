/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.honeytrail;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModWorldComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean isPartOfGame();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$honeyTrail(CallbackInfo ci) {
		if (isPartOfGame()) {
			LivingEntity living = (LivingEntity) (Object) this;
			int maxAge = MathHelper.floor(EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.HONEY_TRAIL, living, 0) * 20);
			if (maxAge > 0) {
				if (isOnGround() || getWorld().raycast(new RaycastContext(getPos(), getPos().add(0, -1.5, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, this)).getType() == HitResult.Type.BLOCK) {
					ModWorldComponents.HONEY_TRAIL.get(getWorld()).addHoneySpot(living, maxAge);
				}
			}
		}
	}
}
