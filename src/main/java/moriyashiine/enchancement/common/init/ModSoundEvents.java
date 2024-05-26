/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class ModSoundEvents {
	public static final SoundEvent ENTITY_SHARD_SHATTER = SoundEvent.of(Enchancement.id("entity.shard.shatter"));
	public static final SoundEvent ENTITY_FISHING_BOBBER_GRAPPLE = SoundEvent.of(Enchancement.id("entity.fishing_bobber.grapple"));
	public static final SoundEvent ENTITY_GENERIC_STRAFE = SoundEvent.of(Enchancement.id("entity.generic.strafe"));
	public static final SoundEvent ENTITY_GENERIC_WARDENSPINE = SoundEvent.of(Enchancement.id("entity.generic.wardenspine"));
	public static final SoundEvent ENTITY_GENERIC_DASH = SoundEvent.of(Enchancement.id("entity.generic.dash"));
	public static final SoundEvent ENTITY_GENERIC_IMPACT = SoundEvent.of(Enchancement.id("entity.generic.impact"));
	public static final SoundEvent ENTITY_GENERIC_AIR_JUMP = SoundEvent.of(Enchancement.id("entity.generic.air_jump"));
	public static final SoundEvent ENTITY_GENERIC_FREEZE = SoundEvent.of(Enchancement.id("entity.generic.freeze"));
	public static final SoundEvent ENTITY_GENERIC_TELEPORT = SoundEvent.of(Enchancement.id("entity.generic.teleport"));
	public static final SoundEvent ENTITY_GENERIC_BURY = SoundEvent.of(Enchancement.id("entity.generic.bury"));

	public static final SoundEvent BLOCK_ORE_EXTRACT = SoundEvent.of(Enchancement.id("block.ore.extract"));
	public static final SoundEvent BLOCK_GENERIC_SMELT = SoundEvent.of(Enchancement.id("block.generic.smelt"));

	public static final SoundEvent ITEM_CROSSBOW_LOADING_BRIMSTONE = SoundEvent.of(Enchancement.id("item.crossbow.loading_brimstone"));
	public static final SoundEvent ITEM_CROSSBOW_BRIMSTONE_1 = SoundEvent.of(Enchancement.id("item.crossbow.brimstone.1"));
	public static final SoundEvent ITEM_CROSSBOW_BRIMSTONE_2 = SoundEvent.of(Enchancement.id("item.crossbow.brimstone.2"));
	public static final SoundEvent ITEM_CROSSBOW_BRIMSTONE_3 = SoundEvent.of(Enchancement.id("item.crossbow.brimstone.3"));
	public static final SoundEvent ITEM_CROSSBOW_BRIMSTONE_4 = SoundEvent.of(Enchancement.id("item.crossbow.brimstone.4"));
	public static final SoundEvent ITEM_CROSSBOW_BRIMSTONE_5 = SoundEvent.of(Enchancement.id("item.crossbow.brimstone.5"));
	public static final SoundEvent ITEM_CROSSBOW_BRIMSTONE_6 = SoundEvent.of(Enchancement.id("item.crossbow.brimstone.6"));
	public static final SoundEvent ITEM_CROSSBOW_SCATTER = SoundEvent.of(Enchancement.id("item.crossbow.scatter"));

	public static void init() {
		Registry.register(Registries.SOUND_EVENT, ENTITY_SHARD_SHATTER.getId(), ENTITY_SHARD_SHATTER);
		Registry.register(Registries.SOUND_EVENT, ENTITY_FISHING_BOBBER_GRAPPLE.getId(), ENTITY_FISHING_BOBBER_GRAPPLE);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_STRAFE.getId(), ENTITY_GENERIC_STRAFE);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_WARDENSPINE.getId(), ENTITY_GENERIC_WARDENSPINE);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_DASH.getId(), ENTITY_GENERIC_DASH);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_IMPACT.getId(), ENTITY_GENERIC_IMPACT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_AIR_JUMP.getId(), ENTITY_GENERIC_AIR_JUMP);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_FREEZE.getId(), ENTITY_GENERIC_FREEZE);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_TELEPORT.getId(), ENTITY_GENERIC_TELEPORT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_BURY.getId(), ENTITY_GENERIC_BURY);
		Registry.register(Registries.SOUND_EVENT, BLOCK_ORE_EXTRACT.getId(), BLOCK_ORE_EXTRACT);
		Registry.register(Registries.SOUND_EVENT, BLOCK_GENERIC_SMELT.getId(), BLOCK_GENERIC_SMELT);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_LOADING_BRIMSTONE.getId(), ITEM_CROSSBOW_LOADING_BRIMSTONE);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_BRIMSTONE_1.getId(), ITEM_CROSSBOW_BRIMSTONE_1);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_BRIMSTONE_2.getId(), ITEM_CROSSBOW_BRIMSTONE_2);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_BRIMSTONE_3.getId(), ITEM_CROSSBOW_BRIMSTONE_3);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_BRIMSTONE_4.getId(), ITEM_CROSSBOW_BRIMSTONE_4);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_BRIMSTONE_5.getId(), ITEM_CROSSBOW_BRIMSTONE_5);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_BRIMSTONE_6.getId(), ITEM_CROSSBOW_BRIMSTONE_6);
		Registry.register(Registries.SOUND_EVENT, ITEM_CROSSBOW_SCATTER.getId(), ITEM_CROSSBOW_SCATTER);
	}
}
