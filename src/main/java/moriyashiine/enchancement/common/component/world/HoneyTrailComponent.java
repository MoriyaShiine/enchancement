/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.world;

import moriyashiine.enchancement.common.particle.HoneyBubbleParticleEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
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
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList honeySpots = tag.getList("HoneySpots", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < honeySpots.size(); i++) {
			this.honeySpots.add(HoneySpot.deserialize(honeySpots.getCompound(i)));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList honeySpots = new NbtList();
		honeySpots.addAll(this.honeySpots.stream().map(HoneySpot::serialize).toList());
		tag.put("HoneySpots", honeySpots);
	}

	@Override
	public void tick() {
		for (int i = honeySpots.size() - 1; i >= 0; i--) {
			HoneySpot spot = honeySpots.get(i);
			if (++spot.age >= HoneySpot.MAX_AGE || isInFluid(spot.blockPos)) {
				honeySpots.remove(i);
			}
		}
	}

	@Override
	public void clientTick() {
		tick();
		for (HoneySpot spot : honeySpots) {
			if (spot.pos.distanceTo(MinecraftClient.getInstance().getCameraEntity().getPos()) < 128) {
				for (int j = 0; j < 3; j++) {
					obj.addParticle(new HoneyBubbleParticleEffect(spot.ownerId),
							MathHelper.nextDouble(obj.random, spot.getBox().minX, spot.getBox().maxX),
							MathHelper.nextDouble(obj.random, spot.getBox().minY, spot.getBox().maxY),
							MathHelper.nextDouble(obj.random, spot.getBox().minZ, spot.getBox().maxZ),
							0, 0, 0);
				}
			}
		}
	}

	public List<HoneySpot> getHoneySpots() {
		return honeySpots;
	}

	public void addHoneySpot(LivingEntity owner) {
		Vec3d adjustedPos = getAdjustedPos(owner);
		for (HoneySpot honeySpot : honeySpots) {
			if (owner.getUuid().equals(honeySpot.getOwnerId()) && honeySpot.box.contract(0.7).contains(adjustedPos)) {
				honeySpot.age = 0;
				return;
			}
		}
		BlockPos blockPos = BlockPos.ofFloored(adjustedPos);
		if (!isInFluid(blockPos)) {
			honeySpots.add(new HoneySpot(owner.getUuid(), adjustedPos, blockPos, 0));
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
		private static final int MAX_AGE = 60;

		private final UUID ownerId;
		private final Vec3d pos;
		private final BlockPos blockPos;
		private final Box box;
		private int age;

		public HoneySpot(UUID ownerId, Vec3d pos, BlockPos blockPos, int age) {
			this.ownerId = ownerId;
			this.pos = pos;
			this.blockPos = blockPos;
			this.box = new Box(pos.add(-0.5, 0, -0.5), pos.add(0.5, 0.2, 0.5));
			this.age = age;
		}

		public UUID getOwnerId() {
			return ownerId;
		}

		public Box getBox() {
			return box;
		}

		public NbtCompound serialize() {
			NbtCompound compound = new NbtCompound();
			compound.putUuid("OwnerId", ownerId);
			compound.putDouble("PosX", pos.getX());
			compound.putDouble("PosY", pos.getY());
			compound.putDouble("PosZ", pos.getZ());
			compound.putLong("BlockPos", blockPos.asLong());
			compound.putInt("Age", age);
			return compound;
		}

		public static HoneySpot deserialize(NbtCompound compound) {
			return new HoneySpot(compound.getUuid("OwnerId"), new Vec3d(compound.getDouble("PosX"), compound.getDouble("PosY"), compound.getDouble("PosZ")), BlockPos.fromLong(compound.getLong("BlockPos")), compound.getInt("Age"));
		}
	}
}
