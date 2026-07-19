package moriyashiine.enchancement.common.event.internal;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.UUID;

public class InCombatEvent {
	public static void init() {
		ServerLivingEntityEvents.AFTER_DAMAGE.register(new Damage());
		ServerTickEvents.END_SERVER_TICK.register(new Tick());
	}

	public static final Object2IntMap<UUID> COMBAT_TICKS = new Object2IntOpenHashMap<>();

	private static class Damage implements ServerLivingEntityEvents.AfterDamage {
		@Override
		public void afterDamage(LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) {
			if (source.getEntity() instanceof LivingEntity) {
				COMBAT_TICKS.put(entity.getUUID(), 320);
			}
		}
	}

	private static class Tick implements ServerTickEvents.EndTick {
		@Override
		public void onEndTick(MinecraftServer server) {
			COMBAT_TICKS.replaceAll((_, ticks) -> ticks - 1);
			COMBAT_TICKS.object2IntEntrySet().removeIf(entry -> {
				if (entry.getIntValue() <= 0) {
					return true;
				}
				Entity entity = server.overworld().getEntityInAnyDimension(entry.getKey());
				return entity == null || !entity.isAlive();
			});
		}
	}
}
