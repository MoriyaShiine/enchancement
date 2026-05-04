/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.api.datagen.HeadDropsProvider;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModHeadDropsProvider extends HeadDropsProvider {
	public ModHeadDropsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture);
	}

	@Override
	protected void configure(Output output) {
		output.accept(EntityType.BOGGED, Items.SKELETON_SKULL, 0.2F);
		output.accept(EntityType.CREEPER, Items.CREEPER_HEAD, 0.2F);
		output.accept(EntityType.DROWNED, Items.ZOMBIE_HEAD, 0.2F);
		output.accept(EntityType.ENDER_DRAGON, Items.DRAGON_HEAD, 1);
		output.accept(EntityType.HUSK, Items.ZOMBIE_HEAD, 0.2F);
		output.accept(EntityType.PIGLIN, Items.PIGLIN_HEAD, 0.2F);
		output.accept(EntityType.PIGLIN_BRUTE, Items.PIGLIN_HEAD, 0.2F);
		output.accept(EntityType.PARCHED, Items.SKELETON_SKULL, 0.2F);
		output.accept(EntityType.PLAYER, Items.PLAYER_HEAD, 1);
		output.accept(EntityType.SKELETON, Items.SKELETON_SKULL, 0.2F);
		output.accept(EntityType.STRAY, Items.SKELETON_SKULL, 0.2F);
		output.accept(EntityType.WITHER, Items.WITHER_SKELETON_SKULL, 1);
		output.accept(EntityType.WITHER_SKELETON, Items.WITHER_SKELETON_SKULL, 0.2F);
		output.accept(EntityType.ZOMBIE, Items.ZOMBIE_HEAD, 0.2F);
		output.accept(EntityType.ZOMBIE_VILLAGER, Items.ZOMBIE_HEAD, 0.2F);
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_head_drops";
	}
}
