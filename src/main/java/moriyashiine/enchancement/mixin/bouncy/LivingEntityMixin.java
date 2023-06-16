/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.mixin.bouncy;

import moriyashiine.enchancement.common.component.entity.BouncyComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique
	private static final BlockStateParticleEffect SLIME_PARTICLE = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.getDefaultState());

	@Unique
	private Vec3d prevVelocity;

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void enchancement$bouncy(CallbackInfo ci) {
		if (!getWorld().isClient) {
			prevVelocity = getVelocity();
		}
	}

	@SuppressWarnings("unchecked")
	@ModifyArg(method = "fall", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
	private <T extends ParticleEffect> T enchancement$bouncy(T value) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.BOUNCY, this)) {
			return (T) SLIME_PARTICLE;
		}
		return value;
	}

	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void enchancement$bouncy(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (prevVelocity != null && !damageSource.isOf(DamageTypes.STALAGMITE) && fallDistance > getSafeFallDistance() && EnchancementUtil.hasEnchantment(ModEnchantments.BOUNCY, this)) {
			if (!getWorld().isClient) {
				getWorld().playSoundFromEntity(null, this, SoundEvents.BLOCK_SLIME_BLOCK_FALL, getSoundCategory(), 1, 1);
				if (!bypassesLandingEffects()) {
					setVelocity(getVelocity().getX(), -prevVelocity.getY() * 0.99, getVelocity().getZ());
					velocityModified = true;
				}
			}
			cir.setReturnValue(false);
		}
	}

	@ModifyVariable(method = "jump", at = @At("STORE"))
	private double enchancement$bouncy(double value) {
		BouncyComponent bouncyComponent = ModEntityComponents.BOUNCY.getNullable(this);
		if (bouncyComponent != null && bouncyComponent.hasBouncy()) {
			float boostProgress = bouncyComponent.getBoostProgress();
			if (boostProgress > 0) {
				if (!getWorld().isClient) {
					getWorld().playSoundFromEntity(null, this, SoundEvents.BLOCK_SLIME_BLOCK_FALL, getSoundCategory(), 1, 1);
					((ServerWorld) getWorld()).spawnParticles(SLIME_PARTICLE, getX(), getY(), getZ(), 32, 0.0, 0.0, 0.0, 0.15F);
				}
				return value + (boostProgress * 2);
			}
		}
		return value;
	}
}
