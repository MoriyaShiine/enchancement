/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModDamageTypes;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

public class LeechComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final TridentEntity obj;
	private boolean hasLeech = false;
	private LivingEntity stuckEntity = null;
	private int stuckEntityId = -1;
	private int ticksLeeching = 0;
	private float renderTicks = 0, stabTicks = 0;

	public LeechComponent(TridentEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		hasLeech = tag.getBoolean("HasLeech");
		stuckEntityId = tag.getInt("StuckEntityId");
		ticksLeeching = tag.getInt("TicksLeeching");
		renderTicks = tag.getFloat("RenderTicks");
		stabTicks = tag.getFloat("StabTicks");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("HasLeech", hasLeech);
		tag.putInt("StuckEntityId", stuckEntityId);
		tag.putInt("TicksLeeching", ticksLeeching);
		tag.putFloat("RenderTicks", renderTicks);
		tag.putFloat("StabTicks", stabTicks);
	}

	@Override
	public void tick() {
		if (stuckEntityId == -2) {
			stuckEntity = null;
			stuckEntityId = -1;
		} else if (stuckEntityId != -1 && stuckEntity == null && obj.getWorld().getEntityById(stuckEntityId) instanceof LivingEntity living) {
			stuckEntity = living;
		} else {
			if (stuckEntity != null && stuckEntity.isAlive()) {
				obj.setVelocity(Vec3d.ZERO);
				if (++ticksLeeching > 120) {
					stuckEntityId = -2;
				}
				renderTicks += 1 / 20F;
				stabTicks = Math.max(0, stabTicks - stabTicks / 20F);
			} else {
				stuckEntityId = -2;
				ticksLeeching = 0;
				renderTicks = 0;
				stabTicks = 0;
			}
		}
	}

	@Override
	public void serverTick() {
		tick();
		if (stuckEntity != null && stuckEntity.isAlive()) {
			if (obj.getOwner() instanceof LivingEntity living && living.isAlive()) {
				obj.teleport(stuckEntity.getX(), stuckEntity.getEyeY(), stuckEntity.getZ());
				if (ticksLeeching % 20 == 0 && stuckEntity.damage(ModDamageTypes.create(obj.getWorld(), ModDamageTypes.LIFE_DRAIN, obj, living), 1)) {
					living.heal(1);
					stuckEntity.timeUntilRegen = 0;
					stabTicks = 1;
					sync();
				}
			} else {
				stuckEntityId = -2;
				sync();
			}
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (stuckEntity != null && stuckEntity.isAlive() && stabTicks == 19 / 20F) {
			if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || stuckEntity != MinecraftClient.getInstance().cameraEntity) {
				for (int i = 0; i < 6; i++) {
					obj.getWorld().addParticle(ParticleTypes.DAMAGE_INDICATOR, stuckEntity.getParticleX(0.5), stuckEntity.getBodyY(0.5), stuckEntity.getParticleZ(0.5), 0, 0, 0);
				}
			}
		}
	}

	public void sync() {
		ModEntityComponents.LEECH.sync(obj);
	}

	public boolean hasLeech() {
		return hasLeech;
	}

	public void setHasLeech(boolean hasLeech) {
		this.hasLeech = hasLeech;
	}

	public LivingEntity getStuckEntity() {
		return stuckEntity;
	}

	public void setStuckEntityId(int stuckEntityId) {
		this.stuckEntityId = stuckEntityId;
	}

	public float getRenderTicks() {
		return renderTicks;
	}

	public float getStabTicks() {
		return stabTicks;
	}

	public static void maybeSet(LivingEntity user, ItemStack stack, PersistentProjectileEntity trident) {
		if (EnchancementUtil.hasEnchantment(ModEnchantments.LEECH, stack) || (user instanceof DrownedEntity && EnchancementUtil.hasEnchantment(ModEnchantments.LEECH, user))) {
			ModEntityComponents.LEECH.maybeGet(trident).ifPresent(leechComponent -> {
				leechComponent.setHasLeech(true);
				leechComponent.sync();
			});
		}
	}
}
