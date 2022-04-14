package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.client.packet.AddGaleParticlesPacket;
import moriyashiine.enchancement.common.packet.SyncJumpingPacket;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.registry.ModEntityComponents;
import moriyashiine.enchancement.common.registry.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.mixin.util.LivingEntityAccessor;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;

public class GaleComponent implements CommonTickingComponent {
	private final PlayerEntity obj;
	private int jumpCooldown = 0, ticksInAir = 0, timesJumped = 0;

	public GaleComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		jumpCooldown = tag.getInt("JumpCooldown");
		timesJumped = tag.getInt("TimesJumped");
		ticksInAir = tag.getInt("TicksInAir");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("JumpCooldown", jumpCooldown);
		tag.putInt("TicksInAir", ticksInAir);
		tag.putInt("TimesJumped", timesJumped);
	}

	@Override
	public void tick() {
		boolean hasGale = EnchantmentHelper.getEquipmentLevel(ModEnchantments.GALE, obj) > 0;
		if (obj.world.isClient) {
			ModEntityComponents.JUMPING.maybeGet(obj).ifPresent(jumpingComponent -> {
				if (((LivingEntityAccessor) obj).enchancement$jumping()) {
					if (!jumpingComponent.isJumping() && hasGale) {
						SyncJumpingPacket.send(true);
					}
				} else if (jumpingComponent.isJumping()) {
					SyncJumpingPacket.send(false);
				}
			});
		} else {
			boolean onGround = obj.isOnGround();
			if (!onGround) {
				ticksInAir++;
			} else {
				ticksInAir = 0;
			}
			if (jumpCooldown == 0) {
				if (!onGround) {
					if (hasGale && ticksInAir >= 10 && timesJumped < 2 && ModEntityComponents.JUMPING.get(obj).isJumping() && EnchancementUtil.isGroundedOrJumping(obj)) {
						jumpCooldown = 10;
						timesJumped++;
						PlayerLookup.tracking(obj).forEach(foundPlayer -> AddGaleParticlesPacket.send(foundPlayer, obj));
						AddGaleParticlesPacket.send((ServerPlayerEntity) obj, obj);
						obj.world.playSoundFromEntity(null, obj, ModSoundEvents.ENTITY_GENERIC_AIR_JUMP, SoundCategory.PLAYERS, 1, 1);
						obj.jump();
						obj.setVelocity(obj.getVelocity().getX(), obj.getVelocity().getY() * 1.75, obj.getVelocity().getZ());
						obj.velocityModified = true;
					}
				} else {
					timesJumped = 0;
				}
			} else if (jumpCooldown > 0) {
				jumpCooldown--;
			}
		}
	}
}
