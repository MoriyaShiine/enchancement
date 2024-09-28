/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.payload.SlamPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Thickness;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.event.GameEvent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class SlamComponent implements CommonTickingComponent {
	public static final int DEFAULT_SLAM_COOLDOWN = 7;

	private final PlayerEntity obj;
	private boolean isSlamming = false;
	private int slamCooldown = DEFAULT_SLAM_COOLDOWN, ticksLeftToJump = 0;

	private float strength = 0;
	private boolean hasSlam = false;

	private boolean wasPressingSlamKey = false;

	public SlamComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		isSlamming = tag.getBoolean("IsSlamming");
		slamCooldown = tag.getInt("SlamCooldown");
		ticksLeftToJump = tag.getInt("TicksLeftToJump");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("IsSlamming", isSlamming);
		tag.putInt("SlamCooldown", slamCooldown);
		tag.putInt("TicksLeftToJump", ticksLeftToJump);
	}

	@Override
	public void tick() {
		strength = EnchancementUtil.getValue(ModEnchantmentEffectComponentTypes.SLAM, obj, 0);
		hasSlam = strength > 0;
		if (hasSlam) {
			if (slamCooldown > 0) {
				slamCooldown--;
			}
			if (ticksLeftToJump > 0) {
				ticksLeftToJump--;
			}
		} else {
			isSlamming = false;
			slamCooldown = DEFAULT_SLAM_COOLDOWN;
			ticksLeftToJump = 0;
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (hasSlam && isSlamming) {
			slamTick(() -> {
				obj.getWorld().getOtherEntities(obj, new Box(obj.getBlockPos()).expand(5, 1, 5), foundEntity -> foundEntity.isAlive() && foundEntity.distanceTo(obj) < 5).forEach(entity -> {
					if (entity instanceof LivingEntity living && EnchancementUtil.shouldHurt(obj, living) && canSee(entity)) {
						living.takeKnockback(1, obj.getX() - living.getX(), obj.getZ() - living.getZ());
					}
				});
				obj.getWorld().emitGameEvent(GameEvent.STEP, obj.getPos(), GameEvent.Emitter.of(obj.getSteppingBlockState()));
				BlockState state = obj.getWorld().getBlockState(obj.getLandingPos());
				if (state.contains(Properties.THICKNESS) && state.contains(Properties.VERTICAL_DIRECTION) && state.get(Properties.THICKNESS) == Thickness.TIP && state.get(Properties.VERTICAL_DIRECTION) == Direction.UP) {
					obj.damage(obj.getDamageSources().stalagmite(), Integer.MAX_VALUE);
				}
			});
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasSlam && !obj.isSpectator() && obj == MinecraftClient.getInstance().player) {
			if (isSlamming) {
				slamTick(() -> {
					BlockPos.Mutable mutable = new BlockPos.Mutable();
					for (int i = 0; i < 360; i += 15) {
						for (int j = 1; j < 5; j++) {
							double x = obj.getX() + MathHelper.sin(i) * j / 2, z = obj.getZ() + MathHelper.cos(i) * j / 2;
							BlockState state = obj.getWorld().getBlockState(mutable.set(x, Math.round(obj.getY() - 1), z));
							if (!state.isReplaceable() && obj.getWorld().getBlockState(mutable.move(Direction.UP)).isReplaceable()) {
								obj.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), x, mutable.getY(), z, 0, 0, 0);
							}
						}
					}
				});
			}
			boolean pressingSlamKey = EnchancementClient.SLAM_KEYBINDING.isPressed();
			if (pressingSlamKey && !wasPressingSlamKey && canSlam()) {
				isSlamming = true;
				slamCooldown = DEFAULT_SLAM_COOLDOWN;
				SlamPayload.send();
			}
			wasPressingSlamKey = pressingSlamKey;
		} else {
			wasPressingSlamKey = false;
		}
	}

	public void setSlamming(boolean slamming) {
		this.isSlamming = slamming;
	}

	public boolean isSlamming() {
		return isSlamming;
	}

	public void setSlamCooldown(int slamCooldown) {
		this.slamCooldown = slamCooldown;
	}

	public boolean shouldBoostJump() {
		return ticksLeftToJump > 0;
	}

	public float getStrength() {
		return strength;
	}

	public boolean hasSlam() {
		return hasSlam;
	}

	public boolean canSlam() {
		SlideComponent slideComponent = ModEntityComponents.SLIDE.getNullable(obj);
		if (slideComponent != null && slideComponent.isSliding()) {
			return false;
		}
		return slamCooldown == 0 && !obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj);
	}

	private void slamTick(Runnable onLand) {
		obj.setVelocity(obj.getVelocity().getX() * 0.98, -3, obj.getVelocity().getZ() * 0.98);
		obj.fallDistance = 0;
		if (obj.isOnGround()) {
			isSlamming = false;
			ticksLeftToJump = 5;
			obj.playSound(ModSoundEvents.ENTITY_GENERIC_IMPACT, 1, 1);
			onLand.run();
		}
	}

	private boolean canSee(Entity entity) {
		if (entity.getWorld() == obj.getWorld()) {
			return obj.getPos().distanceTo(entity.getPos()) <= 32 && obj.getWorld().raycast(new RaycastContext(obj.getPos(), entity.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, obj)).getType() == HitResult.Type.MISS;
		}
		return false;
	}
}
