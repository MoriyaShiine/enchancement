/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class DisarmedPlayerComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private final List<Item> disarmedItems = new ArrayList<>();

	public DisarmedPlayerComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		NbtList disarmedItems = tag.getList("DisarmedItems", NbtElement.STRING_TYPE);
		for (int i = 0; i < disarmedItems.size(); i++) {
			this.disarmedItems.add(Registries.ITEM.get(Identifier.tryParse(disarmedItems.getString(i))));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList disarmedItems = new NbtList();
		this.disarmedItems.forEach(item -> disarmedItems.add(NbtString.of(Registries.ITEM.getId(item).toString())));
		tag.put("DisarmedItems", disarmedItems);
	}

	@Override
	public void tick() {
		for (int i = disarmedItems.size() - 1; i >= 0; i--) {
			if (!obj.getItemCooldownManager().isCoolingDown(disarmedItems.get(i))) {
				disarmedItems.remove(i);
			}
		}
	}

	public void sync() {
		ModEntityComponents.DISARMED_PLAYER.sync(obj);
	}

	public List<Item> getDisarmedItems() {
		return disarmedItems;
	}
}
