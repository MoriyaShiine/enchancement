/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import com.mojang.serialization.Codec;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Uuids;
import net.minecraft.util.dynamic.Codecs;

import java.util.UUID;

public class ModComponentTypes {
	public static final ComponentType<Boolean> TOGGLEABLE_PASSIVE = new ComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN).build();
	public static final ComponentType<UUID> BRIMSTONE_UUID = new ComponentType.Builder<UUID>().codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC).build();
	public static final ComponentType<Integer> BRIMSTONE_DAMAGE = new ComponentType.Builder<Integer>().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT).build();

	public static void init() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, Enchancement.id("toggleable_passive"), TOGGLEABLE_PASSIVE);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Enchancement.id("brimstone_uuid"), BRIMSTONE_UUID);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Enchancement.id("brimstone_damage"), BRIMSTONE_DAMAGE);
	}
}
