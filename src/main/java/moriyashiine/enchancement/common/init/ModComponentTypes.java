/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

import java.util.UUID;

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerComponentType;

public class ModComponentTypes {
	public static final DataComponentType<Boolean> TOGGLEABLE_PASSIVE = registerComponentType("toggleable_passive", new DataComponentType.Builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
	public static final DataComponentType<UUID> BRIMSTONE_UUID = registerComponentType("brimstone_uuid", new DataComponentType.Builder<UUID>().persistent(UUIDUtil.AUTHLIB_CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));
	public static final DataComponentType<Integer> BRIMSTONE_DAMAGE = registerComponentType("brimstone_damage", new DataComponentType.Builder<Integer>().persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

	public static void init() {
	}
}
