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

import static moriyashiine.strawberrylib.api.module.SLibRegistries.registerDataComponentType;

public class EnchancementDataComponents {
	public static final DataComponentType<Boolean> TOGGLEABLE_PASSIVE = registerDataComponentType("toggleable_passive", new DataComponentType.Builder<Boolean>().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
	public static final DataComponentType<UUID> BRIMSTONE_UUID = registerDataComponentType("brimstone_uuid", new DataComponentType.Builder<UUID>().persistent(UUIDUtil.AUTHLIB_CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC));
	public static final DataComponentType<Integer> BRIMSTONE_DAMAGE = registerDataComponentType("brimstone_damage", new DataComponentType.Builder<Integer>().persistent(ExtraCodecs.POSITIVE_INT).networkSynchronized(ByteBufCodecs.VAR_INT));

	public static void init() {
	}
}
