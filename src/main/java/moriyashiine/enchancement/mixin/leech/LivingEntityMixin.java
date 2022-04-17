package moriyashiine.enchancement.mixin.leech;

import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean isDead();

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void enchancement$leechStuckEntity(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!isInvulnerableTo(source) && !isDead() && source.getSource() instanceof TridentEntity trident) {
			ModEntityComponents.LEECH.maybeGet(trident).ifPresent(leechComponent -> {
				if (leechComponent.hasLeech() && leechComponent.getStuckEntity() == null) {
					LivingEntity living = LivingEntity.class.cast(this);
					world.getEntitiesByClass(TridentEntity.class, getBoundingBox().expand(1), foundTrident -> true).forEach(otherTrident -> ModEntityComponents.LEECH.maybeGet(otherTrident).ifPresent(otherLeech -> {
						if (otherLeech.getStuckEntity() == living) {
							otherLeech.setStuckEntityId(-2);
							otherLeech.sync();
						}
					}));
					leechComponent.setStuckEntityId(living.getId());
					leechComponent.sync();
				}
			});
		}
	}

	@Inject(method = "applyEnchantmentsToDamage", at = @At("RETURN"))
	private void enchancement$leech(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
		if (source.getSource() instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(ModEnchantments.LEECH, living) > 0) {
			living.heal(Math.min(2, cir.getReturnValueF()));
			if (world instanceof ServerWorld serverWorld) {
				serverWorld.spawnParticles(ParticleTypes.DAMAGE_INDICATOR, getX(), getBodyY(0.5), getZ(), 6, getWidth() / 2, 0, getWidth() / 2, 0);
			}
		}
	}
}
