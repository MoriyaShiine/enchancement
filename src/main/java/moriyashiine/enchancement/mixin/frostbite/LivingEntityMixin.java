package moriyashiine.enchancement.mixin.frostbite;

import moriyashiine.enchancement.common.component.entity.FrozenComponent;
import moriyashiine.enchancement.common.enchantment.FrostbiteEnchantment;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "applyDamage", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;getHealth()F"))
	private void enchancement$frostbite(DamageSource source, float amount, CallbackInfo ci) {
		FrostbiteEnchantment.applyEffect(this, source, amount);
	}

	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!world.isClient) {
			ModEntityComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
				if (FrozenComponent.isSourceFreezingEntity(source)) {
					frozenComponent.setLastFreezingAttacker(source.getAttacker());
				}
				if (frozenComponent.isFrozen()) {
					if (source == DamageSource.FREEZE) {
						cir.setReturnValue(false);
					} else {
						Entity entitySource = source.getSource();
						if (entitySource != null && amount <= 1) {
							setVelocity(getVelocity().add(-(entitySource.getX() - getX()), 0, -(entitySource.getZ() - getZ())).normalize().multiply(0.5));
							cir.setReturnValue(true);
						} else {
							LivingEntity living = LivingEntity.class.cast(this);
							for (int i = 0; i < MathHelper.nextInt(random, 24, 32); i++) {
								IceShardEntity entity = new IceShardEntity(world, living);
								entity.setOwner(frozenComponent.getLastFreezingAttacker());
								entity.teleport(getX(), getEyeY(), getZ());
								entity.setVelocity(new Vec3d(random.nextGaussian(), random.nextGaussian() / 2, random.nextGaussian()).normalize().multiply(0.75));
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

	@Inject(method = "isAlive", at = @At("HEAD"), cancellable = true)
	private void enchancement$frostbite(CallbackInfoReturnable<Boolean> cir) {
		ModEntityComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
			if (frozenComponent.isFrozen()) {
				cir.setReturnValue(false);
			}
		});
	}

	@Inject(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;sendEntityStatus(Lnet/minecraft/entity/Entity;B)V"), cancellable = true)
	private void enchancement$frostbite(DamageSource source, CallbackInfo ci) {
		ModEntityComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
			if (frozenComponent.shouldFreezeOnDeath(source)) {
				frozenComponent.freeze();
				ci.cancel();
			}
		});
	}

	@Inject(method = "pushAwayFrom", at = @At("HEAD"))
	private void enchancement$frostbite(Entity entity, CallbackInfo ci) {
		if (!world.isClient && entity instanceof LivingEntity living && !living.isDead()) {
			ModEntityComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
				if (frozenComponent.isFrozen()) {
					Entity lastFreezingAttacker = frozenComponent.getLastFreezingAttacker();
					if (EnchancementUtil.shouldHurt(lastFreezingAttacker, entity) && entity.damage(new EntityDamageSource("freeze", lastFreezingAttacker == null ? this : lastFreezingAttacker), 8)) {
						damage(DamageSource.GENERIC, 2);
						entity.setFrozenTicks(800);
					}
				}
			});
		}
	}

	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFrozenTicks(I)V", ordinal = 1))
	private void enchancement$frostbite(CallbackInfo ci) {
		int frozenTicks = getFrozenTicks();
		if (frozenTicks > 0 && frozenTicks - 2 <= 0) {
			ModEntityComponents.FROZEN.maybeGet(this).ifPresent(frozenComponent -> {
				if (!frozenComponent.isFrozen()) {
					frozenComponent.setLastFreezingAttacker(null);
				}
			});
		}
	}
}
