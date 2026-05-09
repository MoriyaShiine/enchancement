/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import net.minecraft.sounds.SoundEvent;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerSoundEvent;

public class ModSoundEvents {
	public static final SoundEvent BRIMSTONE_TRAVEL = registerSoundEvent("entity.brimstone.travel");
	public static final SoundEvent SHARD_SHATTER = registerSoundEvent("entity.shard.shatter");
	public static final SoundEvent FISHING_BOBBER_GRAPPLE = registerSoundEvent("entity.fishing_bobber.grapple");

	public static final SoundEvent GENERIC_AIR_JUMP = registerSoundEvent("entity.generic.air_jump");
	public static final SoundEvent GENERIC_BOOST_DEFAULT = registerSoundEvent("entity.generic.boost.default");
	public static final SoundEvent GENERIC_BOOST_WATER = registerSoundEvent("entity.generic.boost.water");
	public static final SoundEvent GENERIC_BOOST_LAVA = registerSoundEvent("entity.generic.boost.lava");
	public static final SoundEvent GENERIC_BOOST_POWDER_SNOW = registerSoundEvent("entity.generic.boost.powder_snow");
	public static final SoundEvent GENERIC_BURY = registerSoundEvent("entity.generic.bury");
	public static final SoundEvent GENERIC_DASH = registerSoundEvent("entity.generic.dash");
	public static final SoundEvent GENERIC_ERUPT = registerSoundEvent("entity.generic.erupt");
	public static final SoundEvent GENERIC_FREEZE = registerSoundEvent("entity.generic.freeze");
	public static final SoundEvent GENERIC_GUST = registerSoundEvent("entity.generic.gust");
	public static final SoundEvent GENERIC_IMPACT = registerSoundEvent("entity.generic.impact");
	public static final SoundEvent GENERIC_SPARK = registerSoundEvent("entity.generic.spark");
	public static final SoundEvent GENERIC_STRAFE = registerSoundEvent("entity.generic.strafe");
	public static final SoundEvent GENERIC_TELEPORT = registerSoundEvent("entity.generic.teleport");
	public static final SoundEvent GENERIC_WARDENSPINE = registerSoundEvent("entity.generic.wardenspine");
	public static final SoundEvent GENERIC_ZAP = registerSoundEvent("entity.generic.zap");

	public static final SoundEvent ORE_EXTRACT = registerSoundEvent("block.ore.extract");
	public static final SoundEvent GENERIC_SMELT = registerSoundEvent("block.generic.smelt");

	public static final SoundEvent CROSSBOW_LOADING_BRIMSTONE = registerSoundEvent("item.crossbow.loading_brimstone");
	public static final SoundEvent CROSSBOW_BRIMSTONE_1 = registerSoundEvent("item.crossbow.brimstone.1");
	public static final SoundEvent CROSSBOW_BRIMSTONE_2 = registerSoundEvent("item.crossbow.brimstone.2");
	public static final SoundEvent CROSSBOW_BRIMSTONE_3 = registerSoundEvent("item.crossbow.brimstone.3");
	public static final SoundEvent CROSSBOW_BRIMSTONE_4 = registerSoundEvent("item.crossbow.brimstone.4");
	public static final SoundEvent CROSSBOW_BRIMSTONE_5 = registerSoundEvent("item.crossbow.brimstone.5");
	public static final SoundEvent CROSSBOW_BRIMSTONE_6 = registerSoundEvent("item.crossbow.brimstone.6");
	public static final SoundEvent CROSSBOW_SCATTER = registerSoundEvent("item.crossbow.scatter");
	public static final SoundEvent BOW_READY = registerSoundEvent("item.bow.ready");
	public static final SoundEvent MACE_READY = registerSoundEvent("item.mace.ready");
	public static final SoundEvent TRIDENT_READY = registerSoundEvent("item.trident.ready");
	public static final SoundEvent GENERIC_WHOOSH = registerSoundEvent("item.generic.whoosh");

	public static void init() {
	}
}
