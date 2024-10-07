/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.enchantment.effect.EruptionEffect;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.List;

public class EruptionComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final LivingEntity obj;
	private boolean using = false;

	private int ticksUsing = 0;

	public EruptionComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		using = tag.getBoolean("Using");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("Using", using);
	}

	@Override
	public void tick() {
		int chargeTime = EruptionEffect.getChargeTime(obj.getRandom(), obj.getMainHandStack());
		if (chargeTime > 0 && obj.isUsingItem()) {
			if (ticksUsing == MathHelper.floor(chargeTime * EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.MULTIPLY_CHARGE_TIME, obj.getRandom(), obj.getActiveItem(), 1)) + 3) {
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_PING, 1, 1);
			}
			ticksUsing++;
		} else {
			ticksUsing = 0;
		}
	}

	public void setUsing(boolean using) {
		this.using = using;
	}

	public boolean isUsing() {
		return using;
	}

	public static void useServer(LivingEntity living) {
		living.addVelocity(0, EruptionEffect.getJumpStrength(living.getRandom(), living.getMainHandStack()), 0);
		living.velocityModified = true;
		float base = (float) living.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		int fireDuration = EruptionEffect.getFireDuration(living.getRandom(), living.getMainHandStack());
		getNearby(living).forEach(entity -> {
			DamageSource source = living instanceof PlayerEntity player ? entity.getDamageSources().playerAttack(player) : entity.getDamageSources().mobAttack(living);
			float damage = EnchantmentHelper.getDamage((ServerWorld) living.getWorld(), living.getMainHandStack(), entity, source, base)
					+ living.getMainHandStack().getItem().getBonusAttackDamage(entity, base, source);
			entity.setOnFireFor(fireDuration);
			if (entity.damage(source, damage)) {
				entity.takeKnockback(1, living.getX() - entity.getX(), living.getZ() - entity.getZ());
			}
		});
	}

	public static void useClient(LivingEntity living) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 4; j++) {
				double x = living.getX() + MathHelper.sin(i) * j / 2, z = living.getZ() + MathHelper.cos(i) * j / 2;
				BlockState state = living.getWorld().getBlockState(mutable.set(x, Math.round(living.getY() - 1), z));
				if (!state.isReplaceable() && living.getWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
					BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
					for (int k = 0; k < 2; k++) {
						living.getWorld().addParticle(particle, x, mutable.getY() + 0.5, z, 0, 0.5, 0);
						living.getWorld().addParticle(ParticleTypes.LAVA, x, mutable.getY() + 0.5, z, 0, 2, 0);
					}
				}
			}
		}
	}

	private static List<LivingEntity> getNearby(LivingEntity living) {
		return living.getWorld().getEntitiesByClass(LivingEntity.class, new Box(living.getBlockPos()).expand(2, 2, 2), foundEntity ->
				foundEntity.isAlive() && foundEntity.distanceTo(living) < 10 && EnchancementUtil.shouldHurt(living, foundEntity) && EnchancementUtil.canSee(living, foundEntity, 2));
	}
}
