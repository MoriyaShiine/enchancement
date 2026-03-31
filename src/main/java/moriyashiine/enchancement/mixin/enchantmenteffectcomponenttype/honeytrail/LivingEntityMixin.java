/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.honeytrail;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModLevelComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void enchancement$honeyTrail(CallbackInfo ci) {
		if (asLivingEntity().slib$exists()) {
			LivingEntity living = (LivingEntity) (Object) this;
			int maxAge = Mth.floor(EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.HONEY_TRAIL, living, 0) * 20);
			if (maxAge > 0) {
				if (onGround() || level().clip(new ClipContext(position(), position().add(0, -1.5, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.BLOCK) {
					ModLevelComponents.HONEY_TRAIL.get(level()).addHoneySpot(living, maxAge);
				}
			}
		}
	}
}
