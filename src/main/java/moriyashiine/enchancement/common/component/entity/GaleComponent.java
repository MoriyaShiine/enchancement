package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.packet.SyncJumpingPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;

public class GaleComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	private int jumpCooldown = 0, jumpsLeft = 0, ticksInAir = 0;

	private boolean hasGale = false, shouldJump = false;

	public GaleComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		jumpCooldown = tag.getInt("JumpCooldown");
		jumpsLeft = tag.getInt("JumpsLeft");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("JumpCooldown", jumpCooldown);
		tag.putInt("JumpsLeft", jumpsLeft);
		tag.putInt("TicksInAir", ticksInAir);
	}

	@Override
	public void tick() {
		boolean onGround = obj.isOnGround();
		hasGale = EnchancementUtil.hasEnchantment(ModEnchantments.GALE, obj);
		shouldJump = !onGround && hasGale && jumpCooldown == 0 && jumpsLeft > 0 && ticksInAir >= 10 && ModEntityComponents.JUMPING.get(obj).isJumping() && EnchancementUtil.isGroundedOrJumping(obj);
		if (hasGale) {
			if (jumpCooldown > 0) {
				jumpCooldown--;
			}
			if (onGround) {
				ticksInAir = 0;
				jumpsLeft = 2;
			} else {
				ticksInAir++;
			}
			if (shouldJump) {
				obj.playSound(ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, 1, 1);
				obj.jump();
				obj.setVelocity(obj.getVelocity().getX(), obj.getVelocity().getY() * 1.5, obj.getVelocity().getZ());
				jumpCooldown = 10;
				jumpsLeft--;
			}
		} else {
			jumpCooldown = 0;
			jumpsLeft = 0;
			ticksInAir = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		ModEntityComponents.JUMPING.maybeGet(obj).ifPresent(jumpingComponent -> {
			if (((LivingEntityAccessor) obj).enchancement$jumping()) {
				if (!jumpingComponent.isJumping() && hasGale) {
					SyncJumpingPacket.send(true);
				}
			} else if (jumpingComponent.isJumping()) {
				SyncJumpingPacket.send(false);
			}
		});
		if (shouldJump) {
			if (MinecraftClient.getInstance().gameRenderer.getCamera().isThirdPerson() || obj != MinecraftClient.getInstance().cameraEntity) {
				for (int i = 0; i < 8; i++) {
					obj.world.addParticle(ParticleTypes.CLOUD, obj.getParticleX(1), obj.getY(), obj.getParticleZ(1), 0, 0, 0);
				}
			}
		}
	}

	public boolean hasGale() {
		return hasGale;
	}
}
