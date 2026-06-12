/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.util.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public record HeadDrop(ResourceKey<Item> drop, float chance) {
	public static final Codec<HeadDrop> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ResourceKey.codec(Registries.ITEM).fieldOf("drop").forGetter(HeadDrop::drop),
			Codec.FLOAT.fieldOf("chance").forGetter(HeadDrop::chance)
	).apply(instance, HeadDrop::new));

	public static final Map<EntityType<?>, HeadDrop> DROP_MAP = new HashMap<>();

	public Item dropItem() {
		return BuiltInRegistries.ITEM.getValue(drop());
	}
}
