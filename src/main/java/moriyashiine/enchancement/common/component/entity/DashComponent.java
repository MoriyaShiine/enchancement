package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.EnchancementUtil;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;

public class DashComponent implements CommonTickingComponent {
	private final PlayerEntity obj;
	private boolean shouldRefreshDash = false;
	private int dashCooldown = 0, wavedashTimer = 0;

	private boolean wasSneaking = false;

	public DashComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		shouldRefreshDash = tag.getBoolean("ShouldRefreshDash");
		dashCooldown = tag.getInt("DashCooldown");
		wavedashTimer = tag.getInt("WavedashTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putBoolean("ShouldRefreshDash", shouldRefreshDash);
		tag.putInt("DashCooldown", dashCooldown);
		tag.putInt("WavedashTimer", wavedashTimer);
	}

	@Override
	public void tick() {
		if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.DASH, obj) > 0) {
			boolean onGround = obj.isOnGround();
			boolean sneaking = obj.isSneaking();
			if (!shouldRefreshDash) {
				if (onGround) {
					shouldRefreshDash = true;
				}
			} else if (dashCooldown > 0) {
				dashCooldown--;
			}
			if (wavedashTimer > 0) {
				wavedashTimer--;
			}
			if (!onGround && dashCooldown == 0 && sneaking && !wasSneaking && EnchancementUtil.isGroundedOrJumping(obj)) {
				shouldRefreshDash = false;
				dashCooldown = 20;
				wavedashTimer = 3;
				obj.setVelocity(obj.getRotationVector().normalize());
				obj.fallDistance = 0;
				if (!obj.world.isClient) {
					obj.world.playSoundFromEntity(null, obj, ModSoundEvents.ENTITY_GENERIC_DASH, obj.getSoundCategory(), 1, 1);
				} else {
					MinecraftClient client = MinecraftClient.getInstance();
					if (client.gameRenderer.getCamera().isThirdPerson() || obj != client.cameraEntity) {
						for (int i = 0; i < 8; i++) {
							obj.world.addParticle(ParticleTypes.CLOUD, obj.getParticleX(1), obj.getRandomBodyY(), obj.getParticleZ(1), 0, 0, 0);
						}
					}
				}
			}
			wasSneaking = sneaking;
		} else {
			shouldRefreshDash = false;
			dashCooldown = 0;
			wavedashTimer = 0;
			wasSneaking = false;
		}
	}

	public boolean shouldWavedash() {
		return wavedashTimer > 0;
	}

	public int getDashCooldown() {
		return dashCooldown;
	}

	public void setDashCooldown(int dashCooldown) {
		this.dashCooldown = dashCooldown;
	}
}
