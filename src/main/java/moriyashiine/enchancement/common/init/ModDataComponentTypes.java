/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.init;

import com.mojang.serialization.Codec;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.component.DataComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Uuids;
import net.minecraft.util.dynamic.Codecs;

import java.util.UUID;

public class ModDataComponentTypes {
	public static final DataComponentType<Boolean> TOGGLEABLE_PASSIVE = new DataComponentType.Builder<Boolean>().codec(Codec.BOOL).packetCodec(PacketCodecs.BOOL).build();
	public static final DataComponentType<UUID> BRIMSTONE_UUID = new DataComponentType.Builder<UUID>().codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC).build();
	public static final DataComponentType<Integer> BRIMSTONE_DAMAGE = new DataComponentType.Builder<Integer>().codec(Codecs.POSITIVE_INT).packetCodec(PacketCodecs.VAR_INT).build();

	public static void init() {
		Registry.register(Registries.DATA_COMPONENT_TYPE, Enchancement.id("toggleable_passive"), TOGGLEABLE_PASSIVE);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Enchancement.id("brimstone_uuid"), BRIMSTONE_UUID);
		Registry.register(Registries.DATA_COMPONENT_TYPE, Enchancement.id("brimstone_damage"), BRIMSTONE_DAMAGE);
	}
}
