/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.particle.HoneyBubbleParticleEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Uuids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HoneyTrailComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final World obj;
	private final List<HoneySpot> honeySpots = new ArrayList<>();

	private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();

	public HoneyTrailComponent(World obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		honeySpots.clear();
		honeySpots.addAll(readView.read("HoneySpots", HoneySpot.CODEC.listOf()).orElse(List.of()));
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.put("HoneySpots", HoneySpot.CODEC.listOf(), honeySpots);
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
		Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
		if (cameraEntity != null) {
			for (HoneySpot spot : honeySpots) {
				if (spot.pos.distanceTo(cameraEntity.getPos()) < 128) {
					for (int j = 0; j < 3; j++) {
						obj.addParticleClient(new HoneyBubbleParticleEffect(spot.ownerId),
								MathHelper.nextDouble(obj.random, spot.getBox().minX, spot.getBox().maxX),
								MathHelper.nextDouble(obj.random, spot.getBox().minY, spot.getBox().maxY),
								MathHelper.nextDouble(obj.random, spot.getBox().minZ, spot.getBox().maxZ),
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
		Vec3d adjustedPos = getAdjustedPos(owner);
		for (HoneySpot honeySpot : honeySpots) {
			if (owner.getUuid().equals(honeySpot.getOwnerId()) && honeySpot.box.contract(0.7).contains(adjustedPos)) {
				honeySpot.age = 0;
				return;
			}
		}
		BlockPos blockPos = BlockPos.ofFloored(adjustedPos);
		if (!isInFluid(blockPos)) {
			honeySpots.add(new HoneySpot(owner.getUuid(), adjustedPos, blockPos, 0, maxAge));
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

	private static Vec3d getAdjustedPos(LivingEntity owner) {
		return owner.getWorld().raycast(new RaycastContext(owner.getPos(), owner.getPos().subtract(0, 2, 0), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, owner)).getPos();
	}

	public static class HoneySpot {
		public static final Codec<HoneySpot> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Uuids.CODEC.fieldOf("owner_id").forGetter(HoneySpot::getOwnerId),
						Vec3d.CODEC.fieldOf("pos").forGetter(HoneySpot::getPos),
						BlockPos.CODEC.fieldOf("block_pos").forGetter(HoneySpot::getBlockPos),
						Codec.INT.fieldOf("age").forGetter(HoneySpot::getAge),
						Codec.INT.fieldOf("max_age").forGetter(HoneySpot::getMaxAge))
				.apply(instance, HoneySpot::new));

		private final UUID ownerId;
		private final Vec3d pos;
		private final BlockPos blockPos;
		private final Box box;
		private int age;
		private final int maxAge;

		public HoneySpot(UUID ownerId, Vec3d pos, BlockPos blockPos, int age, int maxAge) {
			this.ownerId = ownerId;
			this.pos = pos;
			this.blockPos = blockPos;
			this.box = new Box(pos.add(-0.5, 0, -0.5), pos.add(0.5, 0.2, 0.5));
			this.age = age;
			this.maxAge = maxAge;
		}

		public UUID getOwnerId() {
			return ownerId;
		}

		private Vec3d getPos() {
			return pos;
		}

		private BlockPos getBlockPos() {
			return blockPos;
		}

		public Box getBox() {
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
