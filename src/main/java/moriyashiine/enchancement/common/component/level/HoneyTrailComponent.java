/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.particle.HoneyBubbleParticleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HoneyTrailComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Level obj;
	private final List<HoneySpot> honeySpots = new ArrayList<>();

	private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

	public HoneyTrailComponent(Level obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		honeySpots.clear();
		honeySpots.addAll(input.read("HoneySpots", HoneySpot.CODEC.listOf()).orElse(List.of()));
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("HoneySpots", HoneySpot.CODEC.listOf(), honeySpots);
	}

	@Override
	public void tick() {
		for (int i = honeySpots.size() - 1; i >= 0; i--) {
			HoneySpot spot = honeySpots.get(i);
			if (++spot.age >= spot.maxAge || isInFluid(spot.blockPos)) {
				honeySpots.remove(i);
			}
		}
	}

	@Override
	public void clientTick() {
		tick();
		Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
		if (cameraEntity != null) {
			for (HoneySpot spot : honeySpots) {
				if (spot.pos.distanceTo(cameraEntity.position()) < 128) {
					for (int j = 0; j < 3; j++) {
						obj.addParticle(new HoneyBubbleParticleOption(spot.ownerId),
								Mth.nextDouble(obj.getRandom(), spot.getBox().minX, spot.getBox().maxX),
								Mth.nextDouble(obj.getRandom(), spot.getBox().minY, spot.getBox().maxY),
								Mth.nextDouble(obj.getRandom(), spot.getBox().minZ, spot.getBox().maxZ),
								0, 0, 0);
					}
				}
			}
		}
	}

	public List<HoneySpot> getHoneySpots() {
		return honeySpots;
	}

	public void addHoneySpot(LivingEntity owner, int maxAge) {
		Vec3 adjustedPos = getAdjustedPos(owner);
		for (HoneySpot honeySpot : honeySpots) {
			if (owner.getUUID().equals(honeySpot.getOwnerId()) && honeySpot.box.deflate(0.7).contains(adjustedPos)) {
				honeySpot.age = 0;
				return;
			}
		}
		BlockPos blockPos = BlockPos.containing(adjustedPos);
		if (!isInFluid(blockPos)) {
			honeySpots.add(new HoneySpot(owner.getUUID(), adjustedPos, blockPos, 0, maxAge));
		}
	}

	private boolean isInFluid(BlockPos pos) {
		for (int i = 0; i <= 1; i++) {
			if (!obj.getFluidState(mutablePos.set(pos.getX(), pos.getY() + i, pos.getZ())).isEmpty()) {
				return true;
			}
		}
		return false;
	}

	private static Vec3 getAdjustedPos(LivingEntity owner) {
		return owner.level().clip(new ClipContext(owner.position(), owner.position().subtract(0, 2, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, owner)).getLocation();
	}

	public static class HoneySpot {
		public static final Codec<HoneySpot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						UUIDUtil.AUTHLIB_CODEC.fieldOf("owner_id").forGetter(HoneySpot::getOwnerId),
						Vec3.CODEC.fieldOf("pos").forGetter(HoneySpot::getPos),
						BlockPos.CODEC.fieldOf("block_pos").forGetter(HoneySpot::getBlockPos),
						Codec.INT.fieldOf("age").forGetter(HoneySpot::getAge),
						Codec.INT.fieldOf("max_age").forGetter(HoneySpot::getMaxAge))
				.apply(instance, HoneySpot::new));

		private final UUID ownerId;
		private final Vec3 pos;
		private final BlockPos blockPos;
		private final AABB box;
		private int age;
		private final int maxAge;

		public HoneySpot(UUID ownerId, Vec3 pos, BlockPos blockPos, int age, int maxAge) {
			this.ownerId = ownerId;
			this.pos = pos;
			this.blockPos = blockPos;
			this.box = new AABB(pos.add(-0.5, 0, -0.5), pos.add(0.5, 0.2, 0.5));
			this.age = age;
			this.maxAge = maxAge;
		}

		public UUID getOwnerId() {
			return ownerId;
		}

		private Vec3 getPos() {
			return pos;
		}

		private BlockPos getBlockPos() {
			return blockPos;
		}

		public AABB getBox() {
			return box;
		}

		private int getAge() {
			return age;
		}

		private int getMaxAge() {
			return maxAge;
		}
	}
}
