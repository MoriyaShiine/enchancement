/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModLootFunctionTypes;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StoreItemEnchantmentsLootFunction extends LootItemConditionalFunction {
	public static final MapCodec<StoreItemEnchantmentsLootFunction> CODEC = RecordCodecBuilder.mapCodec(
			instance -> commonFields(instance)
					.and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(StoreItemEnchantmentsLootFunction::getItem))
					.apply(instance, StoreItemEnchantmentsLootFunction::new)
	);

	private final Item item;

	public StoreItemEnchantmentsLootFunction(List<LootItemCondition> conditions, Item item) {
		super(conditions);
		this.item = item;
	}

	@Override
	public MapCodec<StoreItemEnchantmentsLootFunction> codec() {
		return ModLootFunctionTypes.STORE_ITEM_ENCHANTMENTS;
	}

	@Override
	protected ItemStack run(ItemStack stack, LootContext context) {
		ItemStack checkedStack = item.getDefaultInstance();
		List<Holder<Enchantment>> enchantments = new ArrayList<>();
		Registry<Enchantment> enchantmentRegistry = context.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
		enchantmentRegistry.forEach(enchantment -> {
			Holder<Enchantment> entry = enchantmentRegistry.wrapAsHolder(enchantment);
			if (checkedStack.canBeEnchantedWith(entry, EnchantingContext.ACCEPTABLE)) {
				Optional<TagKey<Item>> tagKey = enchantment.getSupportedItems().unwrapKey();
				if (!(tagKey.isPresent() && (tagKey.get() == ItemTags.MINING_ENCHANTABLE || tagKey.get() == ItemTags.MINING_LOOT_ENCHANTABLE))) {
					enchantments.add(entry);
				}
			}
		});
		if (enchantments.isEmpty()) {
			return stack;
		}
		if (stack.is(Items.BOOK)) {
			stack = Items.ENCHANTED_BOOK.getDefaultInstance();
		}
		stack.enchant(enchantments.get(context.getRandom().nextInt(enchantments.size())), 1);
		return stack;
	}

	private Item getItem() {
		return item;
	}

	public static LootItemConditionalFunction.Builder<?> builder(Item item) {
		return simpleBuilder(conditions -> new StoreItemEnchantmentsLootFunction(conditions, item));
	}
}
