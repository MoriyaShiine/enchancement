/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.packet.GalePacket;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;

public class GaleComponent implements AutoSyncedComponent, CommonTickingComponent {
	public static final int DEFAULT_GALE_COOLDOWN = 10;

	private final PlayerEntity obj;
	private boolean shouldRefreshGale = false;
	private int galeCooldown = DEFAULT_GALE_COOLDOWN, lastGaleCooldown = DEFAULT_GALE_COOLDOWN, jumpCooldown = 10, jumpsLeft = 0, ticksInAir = 0;

	private int galeLevel = 0;
	private boolean hasGale = false;

	public GaleComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldRefreshGale = tag.getBoolean("ShouldRefreshGale");
		galeCooldown = tag.getInt("GaleCooldown");
		lastGaleCooldown = tag.getInt("LastGaleCooldown");
		jumpCooldown = tag.getInt("JumpCooldown");
		jumpsLeft = tag.getInt("JumpsLeft");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("ShouldRefreshGale", shouldRefreshGale);
		tag.putInt("GaleCooldown", galeCooldown);
		tag.putInt("LastGaleCooldown", lastGaleCooldown);
		tag.putInt("JumpCooldown", jumpCooldown);
		tag.putInt("JumpsLeft", jumpsLeft);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		galeLevel = EnchantmentHelper.getEquipmentLevel(ModEnchantments.GALE, obj);
		hasGale = galeLevel > 0;
		if (hasGale) {
			if (!shouldRefreshGale) {
				if (obj.isOnGround()) {
					shouldRefreshGale = true;
				}
			} else if (galeCooldown > 0) {
				galeCooldown--;
				if (galeCooldown == 0 && jumpsLeft < galeLevel) {
					jumpsLeft++;
					setGaleCooldown(DEFAULT_GALE_COOLDOWN);
				}
			}
			if (jumpCooldown > 0) {
				jumpCooldown--;
			}
			if (obj.isOnGround()) {
				ticksInAir = 0;
			} else {
				ticksInAir++;
			}
		} else {
			shouldRefreshGale = false;
			galeCooldown = DEFAULT_GALE_COOLDOWN;
			jumpCooldown = 0;
			jumpsLeft = 0;
			ticksInAir = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (hasGale && ((LivingEntityAccessor) obj).enchancement$jumping() && canUse()) {
			use();
			addGaleParticles(obj);
			GalePacket.send();
		}
	}

	public void sync() {
		ModEntityComponents.GALE.sync(obj);
	}

	public void setGaleCooldown(int galeCooldown) {
		this.galeCooldown = galeCooldown;
		lastGaleCooldown = galeCooldown;
	}

	public int getGaleCooldown() {
		return galeCooldown;
	}

	public int getLastGaleCooldown() {
		return lastGaleCooldown;
	}

	public int getJumpsLeft() {
		return jumpsLeft;
	}

	public void setJumpsLeft(int jumpsLeft) {
		this.jumpsLeft = jumpsLeft;
	}

	public int getGaleLevel() {
		return galeLevel;
	}

	public boolean hasGale() {
		return hasGale;
	}

	public boolean canUse() {
		return jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= (obj.getWorld().isClient ? 10 : 9) && !obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj);
	}

	public void use() {
		obj.jump();
		obj.setVelocity(obj.getVelocity().getX(), obj.getVelocity().getY() * 1.5, obj.getVelocity().getZ());
		obj.playSound(ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, 1, 1);
		setGaleCooldown(DEFAULT_GALE_COOLDOWN);
		shouldRefreshGale = false;
		jumpCooldown = 10;
		jumpsLeft--;
	}

	public static void addGaleParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
