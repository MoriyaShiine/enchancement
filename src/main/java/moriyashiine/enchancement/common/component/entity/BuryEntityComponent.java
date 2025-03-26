/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class BuryEntityComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private BlockPos buryPos = null;

	public BuryEntityComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		buryPos = tag.get("BuryPos", BlockPos.CODEC).orElse(null);
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		if (buryPos != null) {
			tag.put("BuryPos", BlockPos.CODEC, buryPos);
		}
	}

	@Override
	public void serverTick() {
		if (buryPos != null) {
			if (obj.getX() != buryPos.getX() + 0.5 || obj.getY() != buryPos.getY() + 0.5 || obj.getZ() != buryPos.getZ() + 0.5) {
				obj.requestTeleport(buryPos.getX() + 0.5, buryPos.getY() + 0.5, buryPos.getZ() + 0.5);
			}
			if (obj.getVelocity() != Vec3d.ZERO) {
				obj.setVelocity(Vec3d.ZERO);
				obj.velocityModified = true;
			}
		}
	}

	public void sync() {
		ModEntityComponents.BURY_ENTITY.sync(obj);
	}

	public BlockPos getBuryPos() {
		return buryPos;
	}

	public void setBuryPos(BlockPos buryPos) {
		this.buryPos = buryPos;
	}

	public void unbury() {
		setBuryPos(null);
		sync();
	}
}
