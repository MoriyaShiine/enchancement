/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.data.provider;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;

import java.util.concurrent.CompletableFuture;

import static moriyashiine.enchancement.common.Enchancement.id;
import static net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder.RegistrationBuilder.ofEvent;
import static net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder.RegistrationBuilder.ofFile;
import static net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder.of;
import static net.minecraft.resources.Identifier.withDefaultNamespace;

public class ModSoundsProvider extends FabricSoundsProvider {
	public ModSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, SoundExporter exporter) {
		exporter.add(ModSoundEvents.BRIMSTONE_TRAVEL, of().subtitle("subtitles.enchancement.entity.brimstone.travel")
				.sound(ofFile(id("entity/brimstone/travel"))));
		exporter.add(ModSoundEvents.SHARD_SHATTER, of().subtitle("subtitles.enchancement.entity.shard.shatter")
				.sound(ofEvent(SoundEvents.GLASS_BREAK)));
		exporter.add(ModSoundEvents.FISHING_BOBBER_GRAPPLE, of().subtitle("subtitles.enchancement.entity.fishing_bobber.grapple")
				.sound(ofFile(id("entity/fishing_bobber/grapple1")).volume(0.5F))
				.sound(ofFile(id("entity/fishing_bobber/grapple2")).volume(0.5F))
				.sound(ofFile(id("entity/fishing_bobber/grapple3")).volume(0.5F)));

		exporter.add(ModSoundEvents.GENERIC_AIR_JUMP, of().subtitle("subtitles.enchancement.entity.generic.air_jump")
				.sound(ofEvent(SoundEvents.SAND_BREAK)));
		exporter.add(ModSoundEvents.GENERIC_BOOST_DEFAULT, of().subtitle("subtitles.enchancement.entity.generic.boost")
				.sound(ofEvent(SoundEvents.POINTED_DRIPSTONE_DRIP_WATER)));
		exporter.add(ModSoundEvents.GENERIC_BOOST_WATER, of().subtitle("subtitles.enchancement.entity.generic.boost")
				.sound(ofEvent(SoundEvents.BUBBLE_POP)));
		exporter.add(ModSoundEvents.GENERIC_BOOST_LAVA, of().subtitle("subtitles.enchancement.entity.generic.boost")
				.sound(ofEvent(SoundEvents.LAVA_POP)));
		exporter.add(ModSoundEvents.GENERIC_BOOST_POWDER_SNOW, of().subtitle("subtitles.enchancement.entity.generic.boost")
				.sound(ofEvent(SoundEvents.POWDER_SNOW_BREAK)));
		exporter.add(ModSoundEvents.GENERIC_BURY, of().subtitle("subtitles.enchancement.entity.generic.bury")
				.sound(ofEvent(SoundEvents.SHOVEL_FLATTEN)));
		exporter.add(ModSoundEvents.GENERIC_DASH, of().subtitle("subtitles.enchancement.entity.generic.dash")
				.sound(ofEvent(SoundEvents.SAND_BREAK)));
		exporter.add(ModSoundEvents.GENERIC_ERUPT, of().subtitle("subtitles.enchancement.entity.generic.erupt")
				.sound(ofFile(id("entity/erupt"))));
		exporter.add(ModSoundEvents.GENERIC_FREEZE, of().subtitle("subtitles.enchancement.entity.generic.freeze")
				.sound(ofEvent(SoundEvents.PLAYER_HURT_FREEZE)));
		exporter.add(ModSoundEvents.GENERIC_IMPACT, of().subtitle("subtitles.enchancement.entity.generic.impact")
				.sound(ofFile(id("entity/impact"))));
		exporter.add(ModSoundEvents.GENERIC_SPARK, of().subtitle("subtitles.enchancement.entity.generic.spark")
				.sound(ofFile(id("entity/spark"))));
		exporter.add(ModSoundEvents.GENERIC_STRAFE, of().subtitle("subtitles.enchancement.entity.generic.strafe")
				.sound(ofEvent(SoundEvents.SAND_BREAK)));
		exporter.add(ModSoundEvents.GENERIC_TELEPORT, of().subtitle("subtitles.enchancement.entity.generic.teleport")
				.sound(ofEvent(SoundEvents.PLAYER_TELEPORT)));
		exporter.add(ModSoundEvents.GENERIC_WARDENSPINE, of().subtitle("subtitles.enchancement.entity.generic.wardenspine")
				.sound(ofEvent(SoundEvents.SCULK_CLICKING).pitch(1.5F)));
		exporter.add(ModSoundEvents.GENERIC_ZAP, of().subtitle("subtitles.enchancement.entity.generic.zap")
				.sound(ofFile(id("entity/zap"))));

		exporter.add(ModSoundEvents.ORE_EXTRACT, of().subtitle("subtitles.enchancement.block.ore.extract")
				.sound(ofFile(id("block/extract"))));
		exporter.add(ModSoundEvents.GENERIC_SMELT, of().subtitle("subtitles.enchancement.block.generic.smelt")
				.sound(ofEvent(SoundEvents.FURNACE_FIRE_CRACKLE)));

		exporter.add(ModSoundEvents.CROSSBOW_LOADING_BRIMSTONE, of().subtitle("subtitles.item.crossbow.charge")
				.sound(ofFile(id("item/crossbow/loading_brimstone"))));
		exporter.add(ModSoundEvents.CROSSBOW_BRIMSTONE_1, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone1"))));
		exporter.add(ModSoundEvents.CROSSBOW_BRIMSTONE_2, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone2"))));
		exporter.add(ModSoundEvents.CROSSBOW_BRIMSTONE_3, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone3"))));
		exporter.add(ModSoundEvents.CROSSBOW_BRIMSTONE_4, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone4"))));
		exporter.add(ModSoundEvents.CROSSBOW_BRIMSTONE_5, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone5"))));
		exporter.add(ModSoundEvents.CROSSBOW_BRIMSTONE_6, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone6"))));
		exporter.add(ModSoundEvents.CROSSBOW_SCATTER, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/scatter1")))
				.sound(ofFile(id("item/crossbow/scatter2")))
				.sound(ofFile(id("item/crossbow/scatter3"))));
		exporter.add(ModSoundEvents.BOW_READY, of().subtitle("subtitles.enchancement.item.bow.ready")
				.sound(ofEvent(SoundEvents.EXPERIENCE_ORB_PICKUP).volume(0.5F)));
		exporter.add(ModSoundEvents.MACE_READY, of().subtitle("subtitles.enchancement.item.mace.ready")
				.sound(ofEvent(SoundEvents.EXPERIENCE_ORB_PICKUP).volume(0.5F)));
		exporter.add(ModSoundEvents.TRIDENT_READY, of().subtitle("subtitles.enchancement.item.trident.ready")
				.sound(ofEvent(SoundEvents.EXPERIENCE_ORB_PICKUP).volume(0.5F)));
		exporter.add(ModSoundEvents.GENERIC_WHOOSH, of().subtitle("subtitles.enchancement.item.generic.whoosh")
				.sound(ofFile(withDefaultNamespace("mob/breeze/shoot"))));
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_sounds";
	}
}
