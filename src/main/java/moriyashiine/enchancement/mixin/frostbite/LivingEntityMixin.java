package moriyashiine.enchancement.mixin.frostbite;

import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.registry.ModComponents;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityTypeTags;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private Entity causeOfShattering = null;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!world.isClient) {
			ModComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
				if (frozenComponent.isFrozen()) {
					if (source == DamageSource.FREEZE) {
						cir.setReturnValue(false);
					} else {
						Entity entitySource = source.getSource();
						Entity attacker = source.getAttacker();
						if (attacker != null) {
							causeOfShattering = attacker;
						}
						if (entitySource != null && amount <= 1) {
							setVelocity(getVelocity().add(-(entitySource.getX() - getX()), 0, -(entitySource.getZ() - getZ())).normalize().multiply(0.5));
							cir.setReturnValue(true);
						} else {
							for (int i = 0; i < MathHelper.nextInt(random, 16, 24); i++) {
								IceShardEntity entity = new IceShardEntity(world, LivingEntity.class.cast(this));
								entity.setOwner(causeOfShattering);
								entity.teleport(getX(), getEyeY(), getZ());
								entity.setVelocity(new Vec3d(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize().multiply(0.75));
								world.spawnEntity(entity);
							}
							discard();
							cir.setReturnValue(true);
						}
					}
				}
			});
		}
	}

	@Inject(method = "pushAwayFrom", at = @At("HEAD"))
	private void enchancement$frostbite(Entity entity, CallbackInfo ci) {
		ModComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen() && causeOfShattering == null) {
				causeOfShattering = entity;
			}
		});
	}

	@Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"), cancellable = true)
	private void enchancement$frostbite(DamageSource source, CallbackInfo ci) {
		if (!world.isClient && !getType().isIn(ModEntityTypeTags.UNFREEZABLE)) {
			if (source.getSource() instanceof IceShardEntity || (source.getSource() instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(ModEnchantments.FROSTBITE, living) > 0)) {
				ModComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
					frozenComponent.freeze();
					causeOfShattering = source.getAttacker();
					ci.cancel();
				});
			}
		}
	}
}
