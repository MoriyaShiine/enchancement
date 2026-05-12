/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.payload;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.level.WideMiningComponent;
import moriyashiine.enchancement.common.event.enchantmenteffectcomponenttype.WideMiningEvent;
import moriyashiine.enchancement.common.init.ModLevelComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record UpdateWideMiningEntryPayload(WideMiningComponent.Entry entry, boolean add) implements CustomPacketPayload {
	public static final Type<UpdateWideMiningEntryPayload> TYPE = new Type<>(Enchancement.id("update_wide_mining_entry"));
	public static final StreamCodec<FriendlyByteBuf, UpdateWideMiningEntryPayload> CODEC = StreamCodec.composite(
			WideMiningComponent.Entry.STREAM_CODEC, UpdateWideMiningEntryPayload::entry,
			ByteBufCodecs.BOOL, UpdateWideMiningEntryPayload::add,
			UpdateWideMiningEntryPayload::new);

	@Override
	public Type<UpdateWideMiningEntryPayload> type() {
		return TYPE;
	}

	public static void send(WideMiningComponent.Entry entry, boolean add) {
		ClientPlayNetworking.send(new UpdateWideMiningEntryPayload(entry, add));
	}

	public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<UpdateWideMiningEntryPayload> {
		@Override
		public void receive(UpdateWideMiningEntryPayload payload, ServerPlayNetworking.Context context) {
			WideMiningComponent wideMiningComponent = ModLevelComponents.WIDE_MINING.get(context.player().level());
			if (payload.add()) {
				if (WideMiningEvent.isValid(payload.entry().blocks(), context.player().getMainHandItem())
						&& WideMiningEvent.canActivate(context.player(), context.player().getMainHandItem(), context.player().level().getBlockState(payload.entry().origin()))) {
					wideMiningComponent.addEntry(payload.entry());
				}
			} else {
				wideMiningComponent.removeEntry(payload.entry().player());
			}
			wideMiningComponent.sync();
		}
	}
}
