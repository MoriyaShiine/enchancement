package moriyashiine.enchancement.common.component.entity;

import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import moriyashiine.enchancement.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class AssimilationComponent implements ServerTickingComponent {
	private final PlayerEntity obj;

	public AssimilationComponent(PlayerEntity obj) {
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
		if (obj.age % 20 == 0 && obj.getHungerManager().isNotFull() && EnchantmentHelper.getEquipmentLevel(ModEnchantments.ASSIMILATION, obj) > 0) {
			ItemStack food = ItemStack.EMPTY;
			if (obj.getOffHandStack().isFood()) {
				if (needsFood(obj.getOffHandStack().getItem().getFoodComponent())) {
					food = obj.getOffHandStack();
				}
			} else {
				food = getMostNeededFood();
			}
			if (!food.isEmpty()) {
				obj.eatFood(obj.world, food);
			}
		}
	}

	@SuppressWarnings("ConstantConditions")
	private ItemStack getMostNeededFood() {
		ItemStack food = ItemStack.EMPTY;
		for (int i = 0; i < obj.getInventory().main.size(); i++) {
			ItemStack stack = obj.getInventory().main.get(i);
			if (stack.isFood()) {
				FoodComponent component = stack.getItem().getFoodComponent();
				if (needsFood(component)) {
					if (food.isEmpty() || food.getItem().getFoodComponent().getHunger() < component.getHunger()) {
						food = stack;
					}
				}
			}
		}
		return food;
	}

	private boolean needsFood(FoodComponent component) {
		return component != null && (obj.getHungerManager().getFoodLevel() < 6 || obj.getHungerManager().getFoodLevel() <= 20 - component.getHunger());
	}
}
