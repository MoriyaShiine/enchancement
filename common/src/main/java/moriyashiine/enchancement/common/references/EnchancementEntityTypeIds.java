package moriyashiine.enchancement.common.references;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.key;

public class EnchancementEntityTypeIds {
	public static final ResourceKey<EntityType<?>> FROZEN_PLAYER = key(Registries.ENTITY_TYPE, "frozen_player");
	public static final ResourceKey<EntityType<?>> AMETHYST_SHARD = key(Registries.ENTITY_TYPE, "amethyst_shard");
	public static final ResourceKey<EntityType<?>> BRIMSTONE = key(Registries.ENTITY_TYPE, "brimstone");
	public static final ResourceKey<EntityType<?>> ICE_SHARD = key(Registries.ENTITY_TYPE, "ice_shard");
	public static final ResourceKey<EntityType<?>> TORCH = key(Registries.ENTITY_TYPE, "torch");
}
