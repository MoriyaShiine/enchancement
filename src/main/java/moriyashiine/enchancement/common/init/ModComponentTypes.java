/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.init;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;
import net.minecraft.util.dynamic.Codecs;

import java.util.UUID;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerComponentType;

public class ModComponentTypes {
	public static final ComponentType<Boolean> TOGGLEABLE_PASSIVE = registerComponentType("toggleable_passive", new ComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOLEAN));
	public static final ComponentType<UUID> BRIMSTONE_UUID = registerComponentType("brimstone_uuid", new ComponentType.Builder<UUID>().codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC));
	public static final ComponentType<Integer> BRIMSTONE_DAMAGE = registerComponentType("brimstone_damage", new ComponentType.Builder<Integer>().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT));

	public static void init() {
	}
}
