/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class GroundedCooldownComponent implements CommonTickingComponent {
	private static final int MAX_WAIT_TICKS = 5;

	private final PlayerEntity obj;
	private final List<GroundedCooldown> cooldowns = new ArrayList<>();
	private int airTicks = 0, waitTicks = 0;

	public GroundedCooldownComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ReadView readView) {
		cooldowns.clear();
		cooldowns.addAll(readView.read("Cooldowns", GroundedCooldown.CODEC.listOf()).orElse(List.of()));
		airTicks = readView.getInt("AirTicks", 0);
		waitTicks = readView.getInt("WaitTicks", 0);
	}

	@Override
	public void writeData(WriteView writeView) {
		writeView.put("Cooldowns", GroundedCooldown.CODEC.listOf(), cooldowns);
		writeView.putInt("AirTicks", airTicks);
		writeView.putInt("WaitTicks", waitTicks);
	}

	@Override
	public void tick() {
		if (!cooldowns.isEmpty()) {
			cooldowns.forEach(groundedCooldown -> obj.getItemCooldownManager().set(groundedCooldown.stack(), groundedCooldown.cooldown()));
			if (obj.isOnGround()) {
				airTicks = 0;
				waitTicks++;
				if (waitTicks >= MAX_WAIT_TICKS) {
					cooldowns.clear();
				}
			} else if (++airTicks >= MAX_WAIT_TICKS) {
				waitTicks = MAX_WAIT_TICKS;
			}
		} else {
			airTicks = waitTicks = 0;
		}
	}

	public void putOnCooldown(ItemStack stack, int cooldown) {
		cooldowns.add(new GroundedCooldown(stack, cooldown));
	}

	private record GroundedCooldown(ItemStack stack, int cooldown) {
		private static final Codec<GroundedCooldown> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				ItemStack.CODEC.fieldOf("stack").forGetter(GroundedCooldown::stack),
				Codec.INT.fieldOf("cooldown").forGetter(GroundedCooldown::cooldown)
		).apply(instance, GroundedCooldown::new));
	}
}
