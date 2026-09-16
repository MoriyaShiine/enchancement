package moriyashiine.enchancement.mixin.config.enhancemobs.tridentspinattack;

import moriyashiine.enchancement.common.EnchancementConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	public LivingEntityMixin(EntityType<?> type, Level level) {
		super(type, level);
	}

	@SuppressWarnings("ConstantValue")
	@Inject(method = "doAutoAttackOnTouch", at = @At("HEAD"))
	private void enchancement$enhanceMobs(LivingEntity entity, CallbackInfo ci) {
		if (EnchancementConfig.enhanceMobs && level() instanceof ServerLevel level && (Object) this instanceof Mob mob) {
			mob.swingForAttack(InteractionHand.MAIN_HAND);
			mob.doHurtTarget(level, entity);
		}
	}
}
