/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.data.provider;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.reloadlistener.HeadDropsReloadListener;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModHeadDropsProvider extends FabricCodecDataProvider<ModHeadDropsProvider.DatagenHeadDropEntry> {
	public ModHeadDropsProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(packOutput, registriesFuture, PackOutput.Target.DATA_PACK, HeadDropsReloadListener.DIRECTORY, DatagenHeadDropEntry.CODEC);
	}

	@Override
	protected final void configure(BiConsumer<Identifier, DatagenHeadDropEntry> provider, HolderLookup.Provider registries) {
		addHeadDrops(((typeId, drop, chance) -> provider.accept(typeId, new DatagenHeadDropEntry(drop, chance))));
	}

	protected void addHeadDrops(Output output) {
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

	protected Identifier id(String id) {
		return Identifier.parse(id);
	}

	@Override
	public String getName() {
		return Enchancement.MOD_ID + "_head_drops";
	}

	@FunctionalInterface
	public interface Output {
		void accept(Identifier typeId, Identifier dropId, float chance);

		default void accept(EntityType<?> type, Identifier dropId, float chance) {
			accept(BuiltInRegistries.ENTITY_TYPE.getKey(type), dropId, chance);
		}

		default void accept(Identifier typeId, Item drop, float chance) {
			accept(typeId, BuiltInRegistries.ITEM.getKey(drop), chance);
		}

		default void accept(EntityType<?> type, Item drop, float chance) {
			accept(type, BuiltInRegistries.ITEM.getKey(drop), chance);
		}
	}

	protected record DatagenHeadDropEntry(Identifier drop, float chance) {
		public static final Codec<DatagenHeadDropEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Identifier.CODEC.fieldOf("drop").forGetter(DatagenHeadDropEntry::drop),
				Codec.FLOAT.fieldOf("chance").forGetter(DatagenHeadDropEntry::chance)
		).apply(instance, DatagenHeadDropEntry::new));
	}
}
