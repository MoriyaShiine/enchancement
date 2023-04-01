/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

public class BouncyComponent implements AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity obj;
	public int bounceStrength = 0;

	private boolean hasBouncy = false;

	public BouncyComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		bounceStrength = tag.getInt("BounceStrength");
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		tag.putInt("BounceStrength", bounceStrength);
	}

	@Override
	public void tick() {
		hasBouncy = EnchancementUtil.hasEnchantment(ModEnchantments.BOUNCY, obj);
		if (hasBouncy) {
			if (obj.isOnGround() && obj.isSneaking()) {
				if (bounceStrength < 60) {
					bounceStrength++;
				}
			} else {
				bounceStrength = 0;
			}
		} else {
			bounceStrength = 0;
		}
	}

	public float getBoostProgress() {
		return MathHelper.lerp((bounceStrength - 2) / 58F, 0, 1);
	}

	public boolean hasBouncy() {
		return hasBouncy;
	}
}
