package moriyashiine.enchancement.neoforge.common.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.BaseMapCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import moriyashiine.enchancement.common.init.EnchancementEnchantments;
import net.minecraft.core.Holder;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A complete copy of {@link com.mojang.serialization.codecs.UnboundedMapCodec} but including a check for the empty enchantment to not error when multiple copies are found.
 * NeoForge requires me to do this ridiculous hack because it doesn't allow mixins on DFU classes, so the original plan of {@link com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation} that works on Fabric does not work on NeoForge.
 */
public record EvilAssCodecHack<K, V>(Codec<K> keyCodec, Codec<V> elementCodec) implements BaseMapCodec<K, V>, Codec<Map<K, V>> {
	@Override
	public <T> DataResult<Pair<Map<K, V>, T>> decode(final DynamicOps<T> ops, final T input) {
		return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
	}

	@Override
	public <T> DataResult<T> encode(final Map<K, V> input, final DynamicOps<T> ops, final T prefix) {
		return encode(input, ops, ops.mapBuilder()).build(prefix);
	}

	@Override
	public String toString() {
		return "UnboundedMapCodec[" + keyCodec + " -> " + elementCodec + ']';
	}

	@Override
	public <T> DataResult<Map<K, V>> decode(final DynamicOps<T> ops, final MapLike<T> input) {
		final Object2ObjectMap<K, V> read = new Object2ObjectArrayMap<>();
		final Stream.Builder<Pair<T, T>> failed = Stream.builder();

		final DataResult<Unit> result = input.entries().reduce(
				DataResult.success(Unit.INSTANCE, Lifecycle.stable()),
				(r, pair) -> {
					final DataResult<K> key = keyCodec().parse(ops, pair.getFirst());
					final DataResult<V> value = elementCodec().parse(ops, pair.getSecond());

					final DataResult<Pair<K, V>> entryResult = key.apply2stable(Pair::of, value);
					final Optional<Pair<K, V>> entry = entryResult.resultOrPartial();
					if (entry.isPresent()) {
						// ignore duplicate entries if the empty enchantment was already added
						final V existingValue = read.putIfAbsent(entry.get().getFirst(), entry.get().getSecond());
						if (existingValue != null && !(entry.get().getFirst() instanceof Holder.Reference<?> reference && reference.value() == EnchancementEnchantments.EMPTY)) {
							failed.add(pair);
							return r.apply2stable((u, _) -> u, DataResult.error(() -> "Duplicate entry for key: '" + entry.get().getFirst() + "'"));
						}
					}
					if (entryResult.isError()) {
						failed.add(pair);
					}

					return r.apply2stable((u, _) -> u, entryResult);
				},
				(r1, r2) -> r1.apply2stable((u1, _) -> u1, r2)
		);

		final Map<K, V> elements = ImmutableMap.copyOf(read);
		final T errors = ops.createMap(failed.build());

		return result.map(_ -> elements).setPartial(elements).mapError(e -> e + " missed input: " + errors);
	}
}
