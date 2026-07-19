package moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.EnchancementEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.strawberrylib.api.event.ModifyMovementEvents;
import moriyashiine.strawberrylib.api.event.PreventFallDamageEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FluidWalkingEvent {
	public static void init() {
		ModifyMovementEvents.MOVEMENT_DELTA.register(new DolphinsGrace());
		PreventFallDamageEvent.EVENT.register(new FallImmunity());
	}

	public static boolean shouldApplyDolphinsGrace(LivingEntity entity) {
		return entity != null && entity.isInWater() && EnchancementUtil.hasAnyEnchantmentsWith(entity, EnchancementEnchantmentEffectComponentTypes.FLUID_WALKING);
	}

	private static class DolphinsGrace implements ModifyMovementEvents.MovementDelta {
		@Override
		public Vec3 modify(Vec3 delta, LivingEntity entity) {
			if (entity.hasEffect(MobEffects.DOLPHINS_GRACE) && shouldApplyDolphinsGrace(entity)) {
				return delta.scale(1.75);
			}
			return delta;
		}
	}

	private static class FallImmunity implements PreventFallDamageEvent {
		@Override
		public TriState preventsFallDamage(Level level, LivingEntity entity, double fallDistance, float damageModifier, DamageSource source) {
			if (fallDistance > entity.getMaxFallDistance() && !level.getFluidState(entity.getOnPos()).isEmpty() && EnchancementUtil.hasAnyEnchantmentsWith(entity, EnchancementEnchantmentEffectComponentTypes.FLUID_WALKING)) {
				return TriState.TRUE;
			}
			return TriState.DEFAULT;
		}
	}
}
