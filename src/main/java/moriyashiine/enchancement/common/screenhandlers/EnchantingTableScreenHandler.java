/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.screenhandlers;

import moriyashiine.enchancement.client.packet.SyncEnchantingTableCostPacket;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.registry.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.registry.ModTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class EnchantingTableScreenHandler extends ScreenHandler {
	public List<Enchantment> validEnchantments = null, selectedEnchantments = null;
	public int viewIndex = 0;

	private ItemStack enchantingStack = null;
	private int cost = 0;

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
					for (Enchantment enchantment : Registries.ENCHANTMENT) {
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
				cost = 0;
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
	public ItemStack quickMove(PlayerEntity player, int index) {
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
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		context.run((world, pos) -> dropInventory(player, inventory));
	}

	@Override
	public boolean onButtonClick(PlayerEntity player, int id) {
		if (id == 0) {
			if (canEnchant(player, player.isCreative())) {
				context.run((world, pos) -> {
					ItemStack stack = slots.get(0).getStack();
					boolean stackChanged = false;
					if (stack.isOf(Items.BOOK)) {
						stack = new ItemStack(Items.ENCHANTED_BOOK);
						for (Enchantment enchantment : selectedEnchantments) {
							EnchantedBookItem.addEnchantment(stack, new EnchantmentLevelEntry(enchantment, enchantment.getMaxLevel()));
						}
						stackChanged = true;
					} else {
						for (Enchantment enchantment : selectedEnchantments) {
							stack.addEnchantment(enchantment, enchantment.getMaxLevel());
						}
					}
					if (!player.isCreative() && cost > 0) {
						player.applyEnchantmentCosts(stack, cost);
					}
					player.incrementStat(Stats.ENCHANT_ITEM);
					Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, stack, cost);
					world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, world.random.nextFloat() * 0.1F + 0.9F);
					if (!player.isCreative() && cost > 0) {
						slots.get(1).getStack().decrement(cost);
					}
					if (stackChanged) {
						slots.get(0).setStack(stack);
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
			cost = getCost(slots.get(0).getStack());
			if (player instanceof ServerPlayerEntity serverPlayer) {
				SyncEnchantingTableCostPacket.send(serverPlayer, cost);
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
				cost = 0;
				for (Enchantment enchantment : Registries.ENCHANTMENT) {
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
			return player.experienceLevel >= cost && slots.get(1).getStack().getCount() >= cost;
		}
		return false;
	}

	public int getCost() {
		return cost;
	}

	private int getCost(ItemStack stack) {
		float[] bookshelfCountArray = {0};
		int enchantability = 0;
		context.run((world, pos) -> {
			for (BlockPos offset : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
				if (EnchantingTableBlock.canAccessPowerProvider(world, pos, offset)) {
					bookshelfCountArray[0]++;
				} else if (world.getBlockEntity(pos.add(offset)) instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity && world.getBlockState(pos.add(offset.getX() / 2, offset.getY(), offset.getZ() / 2)).isIn(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)) {
					bookshelfCountArray[0] += chiseledBookshelfBlockEntity.getOpenSlotCount() / 3F;
				}
			}
		});
		int bookshelfCount = Math.min(15, (int) bookshelfCountArray[0]);
		if (stack.getItem() instanceof ArmorItem armorItem) {
			enchantability = armorItem.getEnchantability();
		} else if (stack.getItem() instanceof ToolItem toolItem) {
			enchantability = toolItem.getEnchantability();
		} else if (stack.isOf(Items.BOOK)) {
			enchantability = 30;
		}
		double cost = 60F / (Math.max(1, enchantability + bookshelfCount));
		if (bookshelfCount == 15) {
			cost = MathHelper.floor(cost);
		} else {
			cost = MathHelper.ceil(cost);
		}
		return (int) (cost * selectedEnchantments.size());
	}

	public void updateViewIndex(boolean up) {
		viewIndex = (viewIndex + (up ? -1 : 1)) % validEnchantments.size();
		if (viewIndex < 0) {
			viewIndex += validEnchantments.size();
		}
	}

	@Environment(EnvType.CLIENT)
	public void setCost(int cost) {
		this.cost = cost;
	}

	private static boolean isEnchantmentAllowed(Enchantment enchantment, ItemStack stack) {
		if (enchantment.isAvailableForRandomSelection()) {
			if (!enchantment.isTreasure() || ModConfig.allowTreasureEnchantmentsInEnchantingTable) {
				if (stack.isOf(Items.BOOK) || enchantment.isAcceptableItem(stack)) {
					return !Registries.ENCHANTMENT.entryOf(Registries.ENCHANTMENT.getKey(enchantment).orElse(null)).isIn(ModTags.Enchantments.UNSELECTABLE);
				}
			}
		}
		return false;
	}
}
