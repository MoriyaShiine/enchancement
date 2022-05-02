/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModSoundEvents {
	public static final SoundEvent ENTITY_ICE_SHARD_SHATTER = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.ice_shard.shatter"));
	public static final SoundEvent ENTITY_BRIMSTONE_FIRE = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.brimstone.fire"));
	public static final SoundEvent ENTITY_GENERIC_STRAFE = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.generic.strafe"));
	public static final SoundEvent ENTITY_GENERIC_DASH = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.generic.dash"));
	public static final SoundEvent ENTITY_GENERIC_IMPACT = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.generic.impact"));
	public static final SoundEvent ENTITY_GENERIC_AIR_JUMP = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.generic.air_jump"));
	public static final SoundEvent ENTITY_GENERIC_FREEZE = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.generic.freeze"));
	public static final SoundEvent ENTITY_GENERIC_TELEPORT = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.generic.teleport"));
	public static final SoundEvent ENTITY_GENERIC_BURY = new SoundEvent(new Identifier(Enchancement.MOD_ID, "entity.generic.bury"));

	public static final SoundEvent BLOCK_ORE_EXTRACT = new SoundEvent(new Identifier(Enchancement.MOD_ID, "block.ore.extract"));
	public static final SoundEvent BLOCK_GENERIC_SMELT = new SoundEvent(new Identifier(Enchancement.MOD_ID, "block.generic.smelt"));

	public static void init() {
		Registry.register(Registry.SOUND_EVENT, ENTITY_ICE_SHARD_SHATTER.getId(), ENTITY_ICE_SHARD_SHATTER);
		Registry.register(Registry.SOUND_EVENT, ENTITY_BRIMSTONE_FIRE.getId(), ENTITY_BRIMSTONE_FIRE);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_STRAFE.getId(), ENTITY_GENERIC_STRAFE);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_DASH.getId(), ENTITY_GENERIC_DASH);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_IMPACT.getId(), ENTITY_GENERIC_IMPACT);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_AIR_JUMP.getId(), ENTITY_GENERIC_AIR_JUMP);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_FREEZE.getId(), ENTITY_GENERIC_FREEZE);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_TELEPORT.getId(), ENTITY_GENERIC_TELEPORT);
		Registry.register(Registry.SOUND_EVENT, ENTITY_GENERIC_BURY.getId(), ENTITY_GENERIC_BURY);
		Registry.register(Registry.SOUND_EVENT, BLOCK_ORE_EXTRACT.getId(), BLOCK_ORE_EXTRACT);
		Registry.register(Registry.SOUND_EVENT, BLOCK_GENERIC_SMELT.getId(), BLOCK_GENERIC_SMELT);
	}
}
