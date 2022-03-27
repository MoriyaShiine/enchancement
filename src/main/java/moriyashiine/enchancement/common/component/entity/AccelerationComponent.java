package moriyashiine.enchancement.common.component.entity;

import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.EnchancementUtil;
import moriyashiine.enchancement.common.packet.SyncMovingForwardPacket;
import moriyashiine.enchancement.common.registry.ModComponents;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

import java.util.UUID;

public class AccelerationComponent implements CommonTickingComponent {
	private static final EntityAttributeModifier STEP_HEIGHT_INCREASE = new EntityAttributeModifier(UUID.fromString("fb7d190e-ebaa-485b-8ded-a57cab9b7a5a"), "Enchantment modifier", 1, EntityAttributeModifier.Operation.ADDITION);

	private final PlayerEntity obj;
	private float speedMultiplier = 1;

	public AccelerationComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		speedMultiplier = tag.getFloat("SpeedMultiplier");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putFloat("SpeedMultiplier", speedMultiplier);
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void tick() {
		obj.airStrafingSpeed *= speedMultiplier;
		boolean hasEnchantment = EnchantmentHelper.getEquipmentLevel(ModEnchantments.ACCELERATION, obj) > 0;
		if (hasEnchantment) {
			if (!obj.horizontalCollision && obj.isSprinting() && EnchancementUtil.isGroundedOrJumping(obj) && ModComponents.MOVING_FORWARD.get(obj).isMovingForward()) {
				if (speedMultiplier < 2) {
					speedMultiplier = Math.min(2, speedMultiplier + 1 / 256F);
				}
			} else if (speedMultiplier > 1) {
				speedMultiplier = Math.max(1, speedMultiplier - 1 / 64F);
			}
		} else if (speedMultiplier != 1) {
			speedMultiplier = 1;
		}
		if (!obj.world.isClient) {
			EntityAttributeInstance attribute = obj.getAttributeInstance(StepHeightEntityAttributeMain.STEP_HEIGHT);
			if (hasEnchantment && !obj.isSneaking()) {
				if (!attribute.hasModifier(STEP_HEIGHT_INCREASE)) {
					attribute.addPersistentModifier(STEP_HEIGHT_INCREASE);
				}
			} else if (attribute.hasModifier(STEP_HEIGHT_INCREASE)) {
				attribute.removeModifier(STEP_HEIGHT_INCREASE);
			}
		} else {
			ModComponents.MOVING_FORWARD.maybeGet(obj).ifPresent(movingForwardComponent -> {
				if (obj.forwardSpeed > 0) {
					if (!movingForwardComponent.isMovingForward() && EnchantmentHelper.getEquipmentLevel(ModEnchantments.ACCELERATION, obj) > 0) {
						SyncMovingForwardPacket.send(true);
					}
				} else if (movingForwardComponent.isMovingForward()) {
					SyncMovingForwardPacket.send(false);
				}
			});
		}
	}

	public float getSpeedMultiplier() {
		return speedMultiplier;
	}
}
