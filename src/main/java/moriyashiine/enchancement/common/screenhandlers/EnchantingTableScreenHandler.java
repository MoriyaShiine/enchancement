/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.screenhandlers;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.registry.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.registry.ModTags;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class EnchantingTableScreenHandler extends ScreenHandler {
	public List<Enchantment> validEnchantments = null, selectedEnchantments = null;
	public int viewIndex = 0;

	private ItemStack enchantingStack = null;

	private final Inventory inventory = new SimpleInventory(2) {
		@Override
		public void markDirty() {
			super.markDirty();
			onContentChanged(this);
		}
	};
	private final ScreenHandlerContext context;

	public EnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
	}

	public EnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(ModScreenHandlerTypes.ENCHANTING_TABLE, syncId);
		this.context = context;
		addSlot(new Slot(inventory, 0, 15, 47) {
			@Override
			public boolean canInsert(ItemStack stack) {
				if (stack.getItem() == Items.BOOK) {
					return true;
				} else if (stack.isEnchantable()) {
					for (Enchantment enchantment : Registry.ENCHANTMENT) {
						if (isEnchantmentAllowed(enchantment, stack)) {
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public int getMaxItemCount() {
				return 1;
			}

			@Override
			public void onTakeItem(PlayerEntity player, ItemStack stack) {
				validEnchantments = null;
				selectedEnchantments = null;
				viewIndex = 0;
				enchantingStack = null;
				super.onTakeItem(player, stack);
			}
		});
		addSlot(new Slot(inventory, 1, 35, 47) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack.isOf(Items.LAPIS_LAZULI);
			}
		});
		int index;
		for (index = 0; index < 3; ++index) {
			for (int i = 0; i < 9; ++i) {
				addSlot(new Slot(playerInventory, i + index * 9 + 9, 8 + i * 18, 84 + index * 18));
			}
		}
		for (index = 0; index < 9; ++index) {
			addSlot(new Slot(playerInventory, index, 8 + index * 18, 142));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player) {
		return canUse(context, player, Blocks.ENCHANTING_TABLE);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index) {
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if (slot.hasStack()) {
			ItemStack stackInSlot = slot.getStack();
			stack = stackInSlot.copy();
			if (index == 0) {
				if (!insertItem(stackInSlot, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index == 1) {
				if (!insertItem(stackInSlot, 2, 38, true)) {
					return ItemStack.EMPTY;
				}
			} else if (stackInSlot.isOf(Items.LAPIS_LAZULI)) {
				if (!insertItem(stackInSlot, 1, 2, true)) {
					return ItemStack.EMPTY;
				}
			} else if (!slots.get(0).hasStack() && slots.get(0).canInsert(stackInSlot)) {
				slots.get(0).setStack(stackInSlot.split(1));
			} else {
				return ItemStack.EMPTY;
			}
			if (stackInSlot.isEmpty()) {
				slot.setStack(ItemStack.EMPTY);
			} else {
				slot.markDirty();
			}
			if (stackInSlot.getCount() == stack.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(player, stackInSlot);
		}
		return stack;
	}

	@Override
	public void close(PlayerEntity player) {
		super.close(player);
		context.run((world, pos) -> dropInventory(player, inventory));
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (id == 0) {
			if (canEnchant(player, player.isCreative())) {
				context.run((world, pos) -> {
					ItemStack stack = slots.get(0).getStack();
					if (stack.isOf(Items.BOOK)) {
						stack = new ItemStack(Items.ENCHANTED_BOOK);
						for (Enchantment enchantment : selectedEnchantments) {
							EnchantedBookItem.addEnchantment(stack, new EnchantmentLevelEntry(enchantment, enchantment.getMaxLevel()));
						}
						slots.get(0).setStack(stack);
					} else {
						for (Enchantment enchantment : selectedEnchantments) {
							stack.addEnchantment(enchantment, enchantment.getMaxLevel());
						}
					}
					player.addExperience(-55 * selectedEnchantments.size());
					player.incrementStat(Stats.ENCHANT_ITEM);
					if (player instanceof ServerPlayerEntity serverPlayer) {
						Criteria.ENCHANTED_ITEM.trigger(serverPlayer, stack, getCost());
					}
					world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, world.random.nextFloat() * 0.1F + 0.9F);
					if (!player.isCreative()) {
						slots.get(1).getStack().decrement(getCost());
					}
					inventory.markDirty();
				});
				return true;
			}
		} else if (id == 1) {
			updateViewIndex(true);
			return true;
		} else if (id == 2) {
			updateViewIndex(false);
			return true;
		} else if (id > 2 && id < 6) {
			Enchantment enchantment = getEnchantmentFromViewIndex(id - 3);
			if (selectedEnchantments.contains(enchantment)) {
				selectedEnchantments.remove(enchantment);
			} else {
				selectedEnchantments.add(enchantment);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		if (inventory == this.inventory) {
			ItemStack stack = slots.get(0).getStack();
			if (enchantingStack != stack) {
				validEnchantments = new ArrayList<>();
				selectedEnchantments = new ArrayList<>();
				viewIndex = 0;
				enchantingStack = stack;
				for (Enchantment enchantment : Registry.ENCHANTMENT) {
					if (isEnchantmentAllowed(enchantment, stack)) {
						validEnchantments.add(enchantment);
					}
				}
				super.onContentChanged(inventory);
			}
		}
	}

	public Enchantment getEnchantmentFromViewIndex(int index) {
		if (validEnchantments.size() <= 3) {
			return validEnchantments.get(index);
		}
		return validEnchantments.get((index + viewIndex) % validEnchantments.size());
	}

	public boolean canEnchant(PlayerEntity player, boolean simulate) {
		if (slots.get(0).hasStack()) {
			if (!slots.get(0).getStack().isEnchantable()) {
				return false;
			}
			if (simulate) {
				return true;
			}
			return player.experienceLevel >= getCost() && slots.get(1).getStack().getCount() >= getCost();
		}
		return false;
	}

	public int getCost() {
		return selectedEnchantments.size() * 5;
	}

	public void updateViewIndex(boolean up) {
		viewIndex = (viewIndex + (up ? -1 : 1)) % validEnchantments.size();
		if (viewIndex < 0) {
			viewIndex += validEnchantments.size();
		}
	}

	private static boolean isEnchantmentAllowed(Enchantment enchantment, ItemStack stack) {
		if (enchantment.isAvailableForRandomSelection()) {
			if (!enchantment.isTreasure() || ModConfig.allowTreasureEnchantmentsInEnchantingTable) {
				if (stack.isOf(Items.BOOK) || enchantment.isAcceptableItem(stack)) {
					return !Registry.ENCHANTMENT.entryOf(Registry.ENCHANTMENT.getKey(enchantment).orElse(null)).isIn(ModTags.Enchantments.UNSELECTABLE);
				}
			}
		}
		return false;
	}
}
