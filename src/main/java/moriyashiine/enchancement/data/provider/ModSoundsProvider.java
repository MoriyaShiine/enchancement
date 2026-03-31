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
		exporter.add(ModSoundEvents.ENTITY_BRIMSTONE_TRAVEL, of().subtitle("subtitles.enchancement.entity.brimstone.travel")
				.sound(ofFile(id("entity/brimstone/travel"))));
		exporter.add(ModSoundEvents.ENTITY_SHARD_SHATTER, of().subtitle("subtitles.enchancement.entity.shard.shatter")
				.sound(ofEvent(SoundEvents.GLASS_BREAK)));
		exporter.add(ModSoundEvents.ENTITY_FISHING_BOBBER_GRAPPLE, of().subtitle("subtitles.enchancement.entity.fishing_bobber.grapple")
				.sound(ofEvent(SoundEvents.EXPERIENCE_ORB_PICKUP).volume(0.25F)));

		exporter.add(ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, of().subtitle("subtitles.enchancement.entity.generic.air_jump")
				.sound(ofEvent(SoundEvents.SAND_BREAK)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_BURY, of().subtitle("subtitles.enchancement.entity.generic.bury")
				.sound(ofEvent(SoundEvents.SHOVEL_FLATTEN)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_DASH, of().subtitle("subtitles.enchancement.entity.generic.dash")
				.sound(ofEvent(SoundEvents.SAND_BREAK)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_ERUPT, of().subtitle("subtitles.enchancement.entity.generic.erupt")
				.sound(ofFile(id("entity/erupt"))));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_FREEZE, of().subtitle("subtitles.enchancement.entity.generic.freeze")
				.sound(ofEvent(SoundEvents.PLAYER_HURT_FREEZE)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_IMPACT, of().subtitle("subtitles.enchancement.entity.generic.impact")
				.sound(ofFile(id("entity/impact"))));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_PING, of().subtitle("subtitles.enchancement.entity.generic.ping")
				.sound(ofEvent(SoundEvents.EXPERIENCE_ORB_PICKUP)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_SPARK, of().subtitle("subtitles.enchancement.entity.generic.spark")
				.sound(ofFile(id("entity/spark"))));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_STRAFE, of().subtitle("subtitles.enchancement.entity.generic.strafe")
				.sound(ofEvent(SoundEvents.SAND_BREAK)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_TELEPORT, of().subtitle("subtitles.enchancement.entity.generic.teleport")
				.sound(ofEvent(SoundEvents.PLAYER_TELEPORT)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_WARDENSPINE, of().subtitle("subtitles.enchancement.entity.generic.wardenspine")
				.sound(ofEvent(SoundEvents.SCULK_CLICKING).pitch(1.5F)));
		exporter.add(ModSoundEvents.ENTITY_GENERIC_ZAP, of().subtitle("subtitles.enchancement.entity.generic.zap")
				.sound(ofFile(id("entity/zap"))));

		exporter.add(ModSoundEvents.BLOCK_ORE_EXTRACT, of().subtitle("subtitles.enchancement.block.ore.extract")
				.sound(ofFile(id("block/extract"))));
		exporter.add(ModSoundEvents.BLOCK_GENERIC_SMELT, of().subtitle("subtitles.enchancement.block.generic.smelt")
				.sound(ofEvent(SoundEvents.FURNACE_FIRE_CRACKLE)));

		exporter.add(ModSoundEvents.ITEM_CROSSBOW_LOADING_BRIMSTONE, of().subtitle("subtitles.item.crossbow.charge")
				.sound(ofFile(id("item/crossbow/loading_brimstone"))));
		exporter.add(ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_1, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone1"))));
		exporter.add(ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_2, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone2"))));
		exporter.add(ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_3, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone3"))));
		exporter.add(ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_4, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone4"))));
		exporter.add(ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_5, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone5"))));
		exporter.add(ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_6, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/brimstone6"))));
		exporter.add(ModSoundEvents.ITEM_CROSSBOW_SCATTER, of().subtitle("subtitles.item.crossbow.shoot")
				.sound(ofFile(id("item/crossbow/scatter1")))
				.sound(ofFile(id("item/crossbow/scatter2")))
				.sound(ofFile(id("item/crossbow/scatter3"))));
		exporter.add(ModSoundEvents.ITEM_GENERIC_WHOOSH, of().subtitle("subtitles.enchancement.item.generic.whoosh")
				.sound(ofFile(withDefaultNamespace("mob/breeze/shoot"))));
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_sounds";
	}
}
