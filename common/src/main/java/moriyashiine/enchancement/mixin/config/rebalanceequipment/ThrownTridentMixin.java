package moriyashiine.enchancement.mixin.config.rebalanceequipment;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.EnchancementConfig;
import moriyashiine.enchancement.common.component.entity.config.OwnedTridentComponent;
import moriyashiine.enchancement.common.init.EnchancementEntityComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public abstract class ThrownTridentMixin extends AbstractArrow {
	@Shadow
	public abstract boolean isAcceptibleReturnOwner();

	protected ThrownTridentMixin(EntityType<? extends AbstractArrow> type, Level level) {
		super(type, level);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V", at = @At("TAIL"))
	private void enchancement$rebalanceEquipment(Level level, LivingEntity owner, ItemStack tridentItem, CallbackInfo ci) {
		if (EnchancementConfig.rebalanceEquipment && owner instanceof Player player) {
			OwnedTridentComponent ownedTrident = EnchancementEntityComponents.OWNED_TRIDENT.get(this);
			ownedTrident.markPlayerOwned(player.getUsedItemHand() == InteractionHand.OFF_HAND ? Inventory.SLOT_OFFHAND : player.getInventory().getSelectedSlot());
			ownedTrident.sync();
		}
	}

	@ModifyVariable(method = "tick", at = @At("STORE"), name = "loyalty")
	private int enchancement$rebalanceEquipment(int loyalty) {
		if (EnchancementConfig.rebalanceEquipment && loyalty > 0 && !isAcceptibleReturnOwner()) {
			if (getDeltaMovement().length() > 0) {
				setDeltaMovement(getDeltaMovement().scale(0.9));
			} else {
				setDeltaMovement(Vec3.ZERO);
			}
			return 0;
		}
		return loyalty;
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/arrow/ThrownTrident;isNoPhysics()Z"))
	private boolean enchancement$rebalanceEquipment(boolean value) {
		if (EnchancementConfig.rebalanceEquipment && getY() <= level().getMinY()) {
			return true;
		}
		return value;
	}

	@ModifyVariable(method = "tickDespawn", at = @At("STORE"), name = "loyalty")
	private int enchancement$rebalanceEquipmentPreventDespawn(int loyalty) {
		return EnchancementConfig.rebalanceEquipment && EnchancementEntityComponents.OWNED_TRIDENT.get(this).isOwnedByPlayer() ? 1 : loyalty;
	}
}
