/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("deprecation")
public record EnchantingMaterial(Ingredient ingredient) {
	public static final Codec<EnchantingMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Ingredient.CODEC.fieldOf("ingredient").forGetter(EnchantingMaterial::ingredient)
	).apply(instance, EnchantingMaterial::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, EnchantingMaterial> STREAM_CODEC = Ingredient.CONTENTS_STREAM_CODEC.map(EnchantingMaterial::new, EnchantingMaterial::ingredient);

	public static final Map<Item, EnchantingMaterial> MATERIAL_MAP = new ConcurrentHashMap<>();

	public static final EnchantingMaterial EMPTY = new EnchantingMaterial(null);

	public Holder<Item> get(int index) {
		return ingredient().items().toList().get(index);
	}

	public int size() {
		if (ingredient() == null) {
			return 0;
		}
		return ingredient().items().toList().size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean test(ItemStack stack) {
		if (ingredient() == null) {
			return false;
		}
		return ingredient().test(stack);
	}
}
