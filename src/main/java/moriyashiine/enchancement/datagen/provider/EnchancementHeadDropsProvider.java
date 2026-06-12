/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.datagen.provider;

import moriyashiine.enchancement.api.datagen.HeadDropsProvider;
import moriyashiine.enchancement.common.Enchancement;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class EnchancementHeadDropsProvider extends HeadDropsProvider {
	public EnchancementHeadDropsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture);
	}

	@Override
	protected void configure(Output output) {
		output.accept(EntityTypeIds.BOGGED, BlockItemIds.SKELETON_SKULL, 0.2F);
		output.accept(EntityTypeIds.CREEPER, BlockItemIds.CREEPER_HEAD, 0.2F);
		output.accept(EntityTypeIds.DROWNED, BlockItemIds.ZOMBIE_HEAD, 0.2F);
		output.accept(EntityTypeIds.ENDER_DRAGON, BlockItemIds.DRAGON_HEAD, 1);
		output.accept(EntityTypeIds.HUSK, BlockItemIds.ZOMBIE_HEAD, 0.2F);
		output.accept(EntityTypeIds.PIGLIN, BlockItemIds.PIGLIN_HEAD, 0.2F);
		output.accept(EntityTypeIds.PIGLIN_BRUTE, BlockItemIds.PIGLIN_HEAD, 0.2F);
		output.accept(EntityTypeIds.PARCHED, BlockItemIds.SKELETON_SKULL, 0.2F);
		output.accept(EntityTypeIds.PLAYER, BlockItemIds.PLAYER_HEAD, 1);
		output.accept(EntityTypeIds.SKELETON, BlockItemIds.SKELETON_SKULL, 0.2F);
		output.accept(EntityTypeIds.STRAY, BlockItemIds.SKELETON_SKULL, 0.2F);
		output.accept(EntityTypeIds.WITHER, BlockItemIds.WITHER_SKELETON_SKULL, 1);
		output.accept(EntityTypeIds.WITHER_SKELETON, BlockItemIds.WITHER_SKELETON_SKULL, 0.2F);
		output.accept(EntityTypeIds.ZOMBIE, BlockItemIds.ZOMBIE_HEAD, 0.2F);
		output.accept(EntityTypeIds.ZOMBIE_VILLAGER, BlockItemIds.ZOMBIE_HEAD, 0.2F);
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_head_drops";
	}
}
