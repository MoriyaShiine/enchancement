/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class DisarmedPlayerComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Player obj;
	private final List<ItemStack> disarmedStacks = new ArrayList<>();

	public DisarmedPlayerComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		disarmedStacks.clear();
		disarmedStacks.addAll(input.read("DisarmedStacks", ItemStack.CODEC.listOf()).orElse(List.of()));
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("DisarmedStacks", ItemStack.CODEC.listOf(), disarmedStacks);
	}

	@Override
	public void tick() {
		for (int i = disarmedStacks.size() - 1; i >= 0; i--) {
			ItemStack stack = disarmedStacks.get(i);
			if (stack.isEmpty() || !obj.getCooldowns().isOnCooldown(stack)) {
				disarmedStacks.remove(i);
			}
		}
	}

	public void sync() {
		ModEntityComponents.DISARMED_PLAYER.sync(obj);
	}

	public List<ItemStack> getDisarmedStacks() {
		return disarmedStacks;
	}

	public boolean isDisabled(ItemStack stack) {
		return getDisarmedStacks().stream().anyMatch(disarmedStack -> stack.getItem() == disarmedStack.getItem());
	}
}
