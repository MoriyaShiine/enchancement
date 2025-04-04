/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModLootFunctionTypes;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StoreItemEnchantmentsLootFunction extends ConditionalLootFunction {
	public static final MapCodec<StoreItemEnchantmentsLootFunction> CODEC = RecordCodecBuilder.mapCodec(
			instance -> addConditionsField(instance)
					.and(Registries.ITEM.getCodec().fieldOf("item").forGetter(StoreItemEnchantmentsLootFunction::getItem))
					.apply(instance, StoreItemEnchantmentsLootFunction::new)
	);

	private final Item item;

	public StoreItemEnchantmentsLootFunction(List<LootCondition> conditions, Item item) {
		super(conditions);
		this.item = item;
	}

	@Override
	public LootFunctionType<? extends ConditionalLootFunction> getType() {
		return ModLootFunctionTypes.STORE_ITEM_ENCHANTMENTS;
	}

	@Override
	protected ItemStack process(ItemStack stack, LootContext context) {
		ItemStack checkedStack = item.getDefaultStack();
		List<RegistryEntry<Enchantment>> enchantments = new ArrayList<>();
		Registry<Enchantment> enchantmentRegistry = context.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
		enchantmentRegistry.forEach(enchantment -> {
			RegistryEntry<Enchantment> entry = enchantmentRegistry.getEntry(enchantment);
			if (checkedStack.canBeEnchantedWith(entry, EnchantingContext.ACCEPTABLE)) {
				Optional<TagKey<Item>> tagKey = enchantment.getApplicableItems().getTagKey();
				if (!(tagKey.isPresent() && (tagKey.get() == ItemTags.MINING_ENCHANTABLE || tagKey.get() == ItemTags.MINING_LOOT_ENCHANTABLE))) {
					enchantments.add(entry);
				}
			}
		});
		if (enchantments.isEmpty()) {
			return stack;
		}
		if (stack.isOf(Items.BOOK)) {
			stack = Items.ENCHANTED_BOOK.getDefaultStack();
		}
		stack.addEnchantment(enchantments.get(context.getRandom().nextInt(enchantments.size())), 1);
		return stack;
	}

	private Item getItem() {
		return item;
	}

	public static ConditionalLootFunction.Builder<?> builder(Item item) {
		return builder(conditions -> new StoreItemEnchantmentsLootFunction(conditions, item));
	}
}
