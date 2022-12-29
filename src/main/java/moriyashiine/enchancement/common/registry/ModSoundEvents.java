/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;

public class ModSoundEvents {
	public static final SoundEvent ENTITY_ICE_SHARD_SHATTER = SoundEvent.of(Enchancement.id("entity.ice_shard.shatter"));
	public static final SoundEvent ENTITY_BRIMSTONE_FIRE = SoundEvent.of(Enchancement.id("entity.brimstone.fire"));
	public static final SoundEvent ENTITY_GENERIC_STRAFE = SoundEvent.of(Enchancement.id("entity.generic.strafe"));
	public static final SoundEvent ENTITY_GENERIC_DASH = SoundEvent.of(Enchancement.id("entity.generic.dash"));
	public static final SoundEvent ENTITY_GENERIC_IMPACT = SoundEvent.of(Enchancement.id("entity.generic.impact"));
	public static final SoundEvent ENTITY_GENERIC_AIR_JUMP = SoundEvent.of(Enchancement.id("entity.generic.air_jump"));
	public static final SoundEvent ENTITY_GENERIC_FREEZE = SoundEvent.of(Enchancement.id("entity.generic.freeze"));
	public static final SoundEvent ENTITY_GENERIC_TELEPORT = SoundEvent.of(Enchancement.id("entity.generic.teleport"));
	public static final SoundEvent ENTITY_GENERIC_BURY = SoundEvent.of(Enchancement.id("entity.generic.bury"));

	public static final SoundEvent BLOCK_ORE_EXTRACT = SoundEvent.of(Enchancement.id("block.ore.extract"));
	public static final SoundEvent BLOCK_GENERIC_SMELT = SoundEvent.of(Enchancement.id("block.generic.smelt"));

	public static void init() {
		Registry.register(Registries.SOUND_EVENT, ENTITY_ICE_SHARD_SHATTER.getId(), ENTITY_ICE_SHARD_SHATTER);
		Registry.register(Registries.SOUND_EVENT, ENTITY_BRIMSTONE_FIRE.getId(), ENTITY_BRIMSTONE_FIRE);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_STRAFE.getId(), ENTITY_GENERIC_STRAFE);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_DASH.getId(), ENTITY_GENERIC_DASH);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_IMPACT.getId(), ENTITY_GENERIC_IMPACT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_AIR_JUMP.getId(), ENTITY_GENERIC_AIR_JUMP);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_FREEZE.getId(), ENTITY_GENERIC_FREEZE);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_TELEPORT.getId(), ENTITY_GENERIC_TELEPORT);
		Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_BURY.getId(), ENTITY_GENERIC_BURY);
		Registry.register(Registries.SOUND_EVENT, BLOCK_ORE_EXTRACT.getId(), BLOCK_ORE_EXTRACT);
		Registry.register(Registries.SOUND_EVENT, BLOCK_GENERIC_SMELT.getId(), BLOCK_GENERIC_SMELT);
	}
}
