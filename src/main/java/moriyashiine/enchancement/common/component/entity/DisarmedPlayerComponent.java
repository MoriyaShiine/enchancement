/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class DisarmedPlayerComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private final List<ItemStack> disarmedStacks = new ArrayList<>();

	public DisarmedPlayerComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		disarmedStacks.clear();
		disarmedStacks.addAll(tag.get("DisarmedStacks", ItemStack.CODEC.listOf(), registryLookup.getOps(NbtOps.INSTANCE)).orElse(List.of()));
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.put("DisarmedStacks", ItemStack.CODEC.listOf(), registryLookup.getOps(NbtOps.INSTANCE), List.copyOf(disarmedStacks));
	}

	@Override
	public void tick() {
		for (int i = disarmedStacks.size() - 1; i >= 0; i--) {
			ItemStack stack = disarmedStacks.get(i);
			if (stack.isEmpty() || !obj.getItemCooldownManager().isCoolingDown(stack)) {
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
