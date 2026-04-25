/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.internal;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class GroundedCooldownComponent implements CommonTickingComponent {
	private static final int MAX_WAIT_TICKS = 5;

	private final Player obj;
	private final List<GroundedCooldown> cooldowns = new ArrayList<>();
	private int airTicks = 0, waitTicks = 0;

	public GroundedCooldownComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		cooldowns.clear();
		cooldowns.addAll(input.read("Cooldowns", GroundedCooldown.CODEC.listOf()).orElse(List.of()));
		airTicks = input.getIntOr("AirTicks", 0);
		waitTicks = input.getIntOr("WaitTicks", 0);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("Cooldowns", GroundedCooldown.CODEC.listOf(), cooldowns);
		output.putInt("AirTicks", airTicks);
		output.putInt("WaitTicks", waitTicks);
	}

	@Override
	public void tick() {
		if (!cooldowns.isEmpty()) {
			cooldowns.forEach(cooldown -> obj.getCooldowns().addCooldown(cooldown.stack(), cooldown.cooldown()));
			if (obj.onGround()) {
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
