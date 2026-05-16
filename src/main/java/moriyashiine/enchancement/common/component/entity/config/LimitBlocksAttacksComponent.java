/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.config;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class LimitBlocksAttacksComponent implements CommonTickingComponent {
	private static final int MAX_CHARGE = 60;

	private final Player obj;
	private final Object2IntMap<ItemStack> charges = new Object2IntOpenHashMap<>();

	public LimitBlocksAttacksComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
	}

	@Override
	public void writeData(ValueOutput output) {
	}

	@Override
	public void tick() {
		ItemStack useItem = obj.getUseItem();
		if (ModConfig.rebalanceEquipment && obj.slib$isSurvival() && !useItem.isEmpty() && useItem.has(DataComponents.BLOCKS_ATTACKS)) {
			int charge = charges.put(useItem, charges.getOrDefault(useItem, 0) + 1);
			if (charge == MAX_CHARGE) {
				obj.stopUsingItem();
				obj.getCooldowns().addCooldown(useItem, MAX_CHARGE * 2);
			}
		}
		charges.replaceAll((stack, charge) -> {
			if (stack != useItem) {
				return charge - 1;
			}
			return charge;
		});
		charges.object2IntEntrySet().removeIf(charge -> charge.getIntValue() == 0);
	}

	public int getBarWidth(ItemStack stack) {
		int charge = charges.getOrDefault(stack, 0);
		return Mth.clamp(Math.round(13 - charge * 13F / MAX_CHARGE), 0, 13);
	}
}
