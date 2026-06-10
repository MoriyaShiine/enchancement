/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class LimitBlocksAttacksComponent implements AutoSyncedComponent, CommonTickingComponent {
	private static final int SECONDS_TO_RECHARGE = 10, MAX_CHARGE = 20 * SECONDS_TO_RECHARGE;

	private final Player obj;
	private final Object2IntMap<Item> charges = new Object2IntOpenHashMap<>();

	public LimitBlocksAttacksComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		charges.clear();
		input.read("Charges", Charge.CODEC.listOf()).ifPresent(c -> c.forEach(charge -> charges.put(charge.item().value(), charge.charge())));
	}

	@Override
	public void writeData(ValueOutput output) {
		List<Charge> charges = new ArrayList<>();
		this.charges.forEach((item, charge) -> charges.add(new Charge(item.builtInRegistryHolder(), charge)));
		output.store("Charges", Charge.CODEC.listOf(), charges);
	}

	@Override
	public void tick() {
		charges.replaceAll((item, charge) -> {
			if (!obj.getUseItem().is(item)) {
				return charge - 1;
			}
			return charge;
		});
		charges.object2IntEntrySet().removeIf(charge -> charge.getIntValue() <= 0);
	}

	@Override
	public void serverTick() {
		tick();
		ItemStack useItem = obj.getUseItem();
		if (charges.getOrDefault(useItem.getItem(), 0) == MAX_CHARGE) {
			ItemStack itemBlockingWith = obj.getItemBlockingWith();
			BlocksAttacks blocksAttacks = itemBlockingWith != null ? itemBlockingWith.get(DataComponents.BLOCKS_ATTACKS) : null;
			if (blocksAttacks != null) {
				blocksAttacks.disable((ServerLevel) obj.level(), obj, SECONDS_TO_RECHARGE, itemBlockingWith);
			}
			obj.invulnerableTime = 0;
		}
	}

	public void sync() {
		ModEntityComponents.LIMIT_BLOCKS_ATTACKS.sync(obj);
	}

	public int getBarWidth(ItemStack stack) {
		int charge = charges.getOrDefault(stack.getItem(), 0);
		return Mth.clamp(Math.round(13 - charge * 13F / MAX_CHARGE), 0, 13);
	}

	public void damage(ItemStack stack, float damage, boolean player) {
		addCharge(stack, Mth.floor(damage * SECONDS_TO_RECHARGE));
		if (!player) {
			obj.invulnerableTime = 20;
			obj.hurtTime = obj.hurtDuration = 10;
		}
	}

	private void addCharge(ItemStack stack, int charge) {
		charges.put(stack.getItem(), Math.min(MAX_CHARGE, charges.getOrDefault(stack.getItem(), 0) + charge));
	}

	private record Charge(Holder<Item> item, int charge) {
		private static final Codec<Charge> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Item.CODEC.fieldOf("drops").forGetter(Charge::item),
						Codec.INT.fieldOf("charge").forGetter(Charge::charge))
				.apply(instance, Charge::new));
	}
}
