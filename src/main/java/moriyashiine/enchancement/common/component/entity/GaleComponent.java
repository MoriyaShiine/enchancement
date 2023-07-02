/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.GalePacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;

public class GaleComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean hasGaleDashed = true;
	private int jumpCooldown = 10, jumpsLeft = 0, ticksInAir = 0;

	private boolean hasGale = false;

	public GaleComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		hasGaleDashed = tag.getBoolean("HasGaleDashed");
		jumpCooldown = tag.getInt("JumpCooldown");
		jumpsLeft = tag.getInt("JumpsLeft");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("HasGaleDashed", hasGaleDashed);
		tag.putInt("JumpCooldown", jumpCooldown);
		tag.putInt("JumpsLeft", jumpsLeft);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		hasGale = EnchancementUtil.hasEnchantment(ModEnchantments.GALE, obj);
		if (hasGale) {
			if (jumpCooldown > 0) {
				jumpCooldown--;
			}
			if (obj.isOnGround()) {
				hasGaleDashed = false;
				ticksInAir = 0;
				jumpsLeft = 2;
			} else {
				ticksInAir++;
			}
		} else {
			hasGaleDashed = true;
			jumpCooldown = 0;
			jumpsLeft = 0;
			ticksInAir = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (!obj.isOnGround() && hasGale && jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= 10 && EnchancementUtil.isGroundedOrAirborne(obj) && ((LivingEntityAccessor) obj).enchancement$jumping()) {
			handle(obj, this);
			addGaleParticles(obj);
			GalePacket.send();
		}
	}

	public int getJumpsLeft() {
		return jumpsLeft;
	}

	public boolean hasGale() {
		return hasGale;
	}

	public static void handle(PlayerEntity player, GaleComponent galeComponent) {
		ModEntityComponents.DASH.maybeGet(player).ifPresent(dashComponent -> {
			player.jump();
			if (galeComponent.hasGaleDashed) {
				player.setVelocity(0, player.getVelocity().getY() * 1.5, 0);
			} else {
				player.setVelocity(player.getVelocity().getX(), player.getVelocity().getY() * 1.5, player.getVelocity().getZ());
			}
			player.playSound(ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, 1, 1);
			galeComponent.jumpCooldown = 10;
			galeComponent.jumpsLeft--;
			if (dashComponent.hasDash() && dashComponent.shouldWavedash()) {
				player.setVelocity(player.getVelocity().multiply(1.5));
				galeComponent.hasGaleDashed = true;
				dashComponent.setShouldRefreshDash(false);
				dashComponent.setDashCooldown(DashComponent.DEFAULT_DASH_COOLDOWN * 3);
				ModEntityComponents.STRAFE.get(player).setTicksInAir(0);
			}
		});
	}

	public static void addGaleParticles(Entity entity) {
		if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || entity != MinecraftClient.getInstance().cameraEntity) {
			for (int i = 0; i < 8; i++) {
				entity.getWorld().addParticle(ParticleTypes.CLOUD, entity.getParticleX(1), entity.getY(), entity.getParticleZ(1), 0, 0, 0);
			}
		}
	}
}
