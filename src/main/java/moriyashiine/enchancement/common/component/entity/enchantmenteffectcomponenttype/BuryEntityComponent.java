/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.world.item.effects.entity.BuryEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class BuryEntityComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final LivingEntity obj;
	private BlockPos buryPos = null;

	public BuryEntityComponent(LivingEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		buryPos = input.read("BuryPos", BlockPos.CODEC).orElse(null);
	}

	@Override
	public void writeData(ValueOutput output) {
		if (buryPos != null) {
			output.store("BuryPos", BlockPos.CODEC, buryPos);
		}
	}

	@Override
	public void serverTick() {
		if (buryPos != null) {
			if (obj.getX() != buryPos.getX() + 0.5 || obj.getY() != buryPos.getY() + 0.5 || obj.getZ() != buryPos.getZ() + 0.5) {
				obj.teleportTo(buryPos.getX() + 0.5, buryPos.getY() + 0.5, buryPos.getZ() + 0.5);
			}
			if (obj.getDeltaMovement() != Vec3.ZERO) {
				obj.setDeltaMovement(Vec3.ZERO);
				obj.hurtMarked = true;
			}
			if (BuryEffect.cannotBeBuried(obj)) {
				unbury();
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
