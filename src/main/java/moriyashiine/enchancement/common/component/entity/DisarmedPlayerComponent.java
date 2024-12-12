/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.component.entity;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
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
		NbtList disarmedItems = tag.getList("DisarmedStacks", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < disarmedItems.size(); i++) {
			ItemStack.fromNbt(registryLookup, disarmedItems.getCompound(i)).ifPresent(this.disarmedStacks::add);
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList disarmedItems = new NbtList();
		this.disarmedStacks.forEach(stack -> {
			if (!stack.isEmpty()) {
				disarmedItems.add(stack.toNbt(registryLookup));
			}
		});
		tag.put("DisarmedStacks", disarmedItems);
	}

	@Override
	public void tick() {
		for (int i = disarmedStacks.size() - 1; i >= 0; i--) {
			if (!obj.getItemCooldownManager().isCoolingDown(disarmedStacks.get(i))) {
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
