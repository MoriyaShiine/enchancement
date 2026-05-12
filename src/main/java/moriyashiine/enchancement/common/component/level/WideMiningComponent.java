/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.level;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moriyashiine.enchancement.common.init.ModLevelComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WideMiningComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Level obj;
	private final List<Entry> entries = new ArrayList<>();

	public WideMiningComponent(Level obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		entries.clear();
		entries.addAll(input.read("Entries", Entry.CODEC.listOf()).orElse(List.of()));
	}

	@Override
	public void writeData(ValueOutput output) {
		output.store("Entries", Entry.CODEC.listOf(), entries);
	}

	@Override
	public void tick() {
		entries.removeIf(entry -> obj.getPlayerByUUID(entry.player()) == null);
	}

	public void sync() {
		ModLevelComponents.WIDE_MINING.sync(obj);
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public @Nullable Entry getEntry(Player player) {
		for (Entry entry : entries) {
			if (entry.player().equals(player.getUUID())) {
				return entry;
			}
		}
		return null;
	}

	public boolean addEntry(Entry entry) {
		removeEntry(entry.player());
		return entries.add(entry);
	}

	public boolean removeEntry(UUID player) {
		return entries.removeIf(entry -> entry.player().equals(player));
	}

	public record Entry(UUID player, BlockPos origin, List<BlockPos> blocks, float destroySpeed) {
		public static final Codec<Entry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						UUIDUtil.CODEC.fieldOf("player").forGetter(Entry::player),
						BlockPos.CODEC.fieldOf("origin").forGetter(Entry::origin),
						BlockPos.CODEC.listOf().fieldOf("blocks").forGetter(Entry::blocks),
						Codec.FLOAT.fieldOf("destroy_speed").forGetter(Entry::destroySpeed))
				.apply(instance, Entry::new));

		public static final StreamCodec<FriendlyByteBuf, Entry> STREAM_CODEC = new StreamCodec<>() {
			@Override
			public Entry decode(FriendlyByteBuf input) {
				UUID player = input.readUUID();
				BlockPos origin = input.readBlockPos();
				List<BlockPos> blocks = new ArrayList<>();
				for (long pos : input.readLongArray()) {
					blocks.add(BlockPos.of(pos));
				}
				float destroySpeed = input.readFloat();
				return new Entry(player, origin, blocks, destroySpeed);
			}

			@Override
			public void encode(FriendlyByteBuf output, Entry value) {
				output.writeUUID(value.player());
				output.writeBlockPos(value.origin());
				long[] blocks = new long[value.blocks().size()];
				for (int i = 0; i < blocks.length; i++) {
					blocks[i] = value.blocks().get(i).asLong();
				}
				output.writeLongArray(blocks);
				output.writeFloat(value.destroySpeed());
			}
		};
	}
}
