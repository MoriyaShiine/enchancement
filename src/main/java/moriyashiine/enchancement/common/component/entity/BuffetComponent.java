package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import moriyashiine.enchancement.mixin.buffet.LivingEntityAccessor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.UseAction;
import org.jetbrains.annotations.NotNull;

public class BuffetComponent implements ServerTickingComponent {
	private final PlayerEntity obj;

	public BuffetComponent(PlayerEntity obj) {
		this.obj = obj;
	}

	@Override
	public void readFromNbt(@NotNull NbtCompound tag) {
	}

	@Override
	public void writeToNbt(@NotNull NbtCompound tag) {
	}

	@Override
	public void serverTick() {
		if (obj.getActiveItem().isFood() || obj.getActiveItem().getUseAction() == UseAction.DRINK) {
			if (obj.getItemUseTime() == obj.getActiveItem().getMaxUseTime() / 2 && EnchantmentHelper.getEquipmentLevel(ModEnchantments.BUFFET, obj) > 0) {
				((LivingEntityAccessor) obj).enchancement$consumeItem();
			}
		}
	}
}
