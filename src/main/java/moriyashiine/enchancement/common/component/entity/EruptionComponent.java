/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.payload.UseEruptionPayload;
import moriyashiine.enchancement.common.enchantment.effect.EruptionEffect;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
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

	private boolean playedSound = false;

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
			if (!playedSound && obj.getItemUseTime() == chargeTime) {
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_PING, 1, 1);
				playedSound = true;
			}
		} else {
			playedSound = false;
		}
	}

	public void setUsing(boolean using) {
		this.using = using;
	}

	public boolean isUsing() {
		return using;
	}

	public void useCommon() {
		obj.addVelocity(0, EruptionEffect.getJumpStrength(obj.getRandom(), obj.getMainHandStack()), 0);
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_ERUPT, 1, MathHelper.nextFloat(obj.getRandom(), 0.8F, 1.2F));
	}

	public void useClient() {
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		double y = Math.round(obj.getY() - 1);
		for (int i = 0; i < 360; i += 15) {
			for (int j = 1; j < 4; j++) {
				double x = obj.getX() + MathHelper.sin(i) * j / 2, z = obj.getZ() + MathHelper.cos(i) * j / 2;
				BlockState state = obj.getWorld().getBlockState(mutable.set(x, y, z));
				if (!state.isReplaceable() && obj.getWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
					BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
					for (int k = 0; k < 2; k++) {
						obj.getWorld().addParticle(particle, x, mutable.getY() + 0.5, z, 0, 0.5, 0);
						obj.getWorld().addParticle(ParticleTypes.LAVA, x, mutable.getY() + 0.5, z, 0, 2, 0);
					}
				}
			}
		}
	}

	public void useServer() {
		PlayerLookup.tracking(obj).forEach(foundPlayer -> UseEruptionPayload.send(foundPlayer, obj.getId()));
		float base = (float) obj.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		int fireDuration = EruptionEffect.getFireDuration(obj.getRandom(), obj.getMainHandStack());
		getNearby(obj).forEach(entity -> {
			DamageSource source = obj instanceof PlayerEntity player ? entity.getDamageSources().playerAttack(player) : entity.getDamageSources().mobAttack(obj);
			float damage = EnchantmentHelper.getDamage((ServerWorld) obj.getWorld(), obj.getMainHandStack(), entity, source, base)
					+ obj.getMainHandStack().getItem().getBonusAttackDamage(entity, base, source);
			entity.setOnFireFor(fireDuration);
			if (entity.damage(source, damage)) {
				entity.takeKnockback(1, obj.getX() - entity.getX(), obj.getZ() - entity.getZ());
			}
		});
	}

	private static List<LivingEntity> getNearby(LivingEntity living) {
		return living.getWorld().getEntitiesByClass(LivingEntity.class, new Box(living.getBlockPos()).expand(2, 2, 2), foundEntity ->
				foundEntity.isAlive() && foundEntity.distanceTo(living) < 10 && EnchancementUtil.shouldHurt(living, foundEntity) && EnchancementUtil.canSee(living, foundEntity, 2));
	}
}
