/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.component.entity.enchantmenteffectcomponenttype;

import moriyashiine.enchancement.client.EnchancementClient;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.payload.ChargeJumpPayload;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.world.item.effects.ChargeJumpEffect;
import moriyashiine.strawberrylib.api.module.SLibClientUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;

public class ChargeJumpComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final Player obj;
	private double charge = 0, toAdd = 0;
	private int addTicks = 0;
	private boolean pressingChargeJump = false;

	private int maximumCharge = 0, renderTicks = 0;

	public ChargeJumpComponent(Player obj) {
		this.obj = obj;
	}

	@Override
	public void readData(ValueInput input) {
		charge = input.getDoubleOr("Charge", 0);
		toAdd = input.getDoubleOr("ToAdd", 0);
		addTicks = input.getIntOr("AddTicks", 0);
		pressingChargeJump = input.getBooleanOr("PressingChargeJump", false);
	}

	@Override
	public void writeData(ValueOutput output) {
		output.putDouble("Charge", charge);
		output.putDouble("ToAdd", toAdd);
		output.putInt("AddTicks", addTicks);
		output.putBoolean("PressingChargeJump", pressingChargeJump);
	}

	@Override
	public void tick() {
		maximumCharge = ChargeJumpEffect.getMaximumCharge(obj);
		if (hasChargeJump()) {
			if (addTicks > 0) {
				addCharge(toAdd / 4);
				if (--addTicks == 0) {
					toAdd = 0;
				}
			}
			if (renderTicks > 0) {
				renderTicks--;
			}
			if (obj.onGround()) {
				if (pressingChargeJump) {
					renderTicks = Math.max(renderTicks, 5);
				}
				double add = EnchancementUtil.getSyncedDeltaMovement(obj).multiply(1, 0, 1).length();
				if (obj.isShiftKeyDown()) {
					add += ChargeJumpEffect.getActiveChargeRate(obj);
				}
				if (add > 0) {
					addCharge(add);
				}
			}
		} else {
			charge = toAdd = addTicks = renderTicks = 0;
		}
	}

	@Override
	public void clientTick() {
		tick();
		if (SLibClientUtils.isHost(obj)) {
			boolean pressing = hasChargeJump() && EnchancementClient.CHARGE_JUMP_KEYMAPPING.isDown();
			if (pressingChargeJump != pressing) {
				pressingChargeJump = pressing;
				ChargeJumpPayload.send(pressingChargeJump);
			}
		}
	}

	public void sync() {
		ModEntityComponents.CHARGE_JUMP.sync(obj);
	}

	public void addCharge(double added) {
		if (charge < maximumCharge) {
			renderTicks = 20;
		}
		charge = Math.min(maximumCharge, charge + added);
	}

	public void addChargeDelayed(double added) {
		toAdd = added;
		addTicks = 4;
	}

	public double getChargeProgress() {
		return Mth.lerp(charge / maximumCharge, 0, 1);
	}

	public double getBoost() {
		double boost = getChargeProgress() * ChargeJumpEffect.getJumpStrength(obj);
		charge = renderTicks = 0;
		return boost;
	}

	public boolean shouldRender() {
		return renderTicks > 0 && !obj.isPassenger();
	}

	public boolean hasChargeJump() {
		return maximumCharge > 0;
	}

	public boolean isPressingChargeJump() {
		return pressingChargeJump;
	}

	public void setPressingChargeJump(boolean pressingChargeJump) {
		this.pressingChargeJump = pressingChargeJump;
	}
}
