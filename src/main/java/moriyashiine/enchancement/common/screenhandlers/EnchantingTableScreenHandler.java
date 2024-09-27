/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.screenhandlers;

import moriyashiine.enchancement.client.payload.SyncEnchantingTableBookshelfCountPayload;
import moriyashiine.enchancement.client.payload.SyncEnchantingTableCostPayload;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;

public class EnchantingTableScreenHandler extends ScreenHandler {
	public static final Map<RegistryEntry<Item>, Ingredient> ENCHANTING_MATERIAL_MAP = new HashMap<>();
	public static final int PAGE_SIZE = 4;

	public final List<RegistryEntry<Enchantment>> validEnchantments = new ArrayList<>(), selectedEnchantments = new ArrayList<>();
	public int viewIndex = 0;

	private ItemStack enchantingStack = ItemStack.EMPTY;
	private Ingredient repairIngredient = Ingredient.EMPTY;
	private int bookshelfCount = 0, cost = 0;

	private final Inventory inventory = new SimpleInventory(3) {
		@Override
		public void markDirty() {
			super.markDirty();
			onContentChanged(this);
		}
	};
	private final ScreenHandlerContext context;
	private final World world;

	public EnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, ScreenHandlerContext.EMPTY, playerInventory.player.getWorld());
	}

	public EnchantingTableScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, World world) {
		super(ModScreenHandlerTypes.ENCHANTING_TABLE, syncId);
		this.context = context;
		this.world = world;
		bookshelfCount = calculateBookshelfCount();
		if (playerInventory.player instanceof ServerPlayerEntity player) {
			SyncEnchantingTableBookshelfCountPayload.send(player, bookshelfCount);
		}
		addSlot(new Slot(inventory, 0, 15, 31) {
			@Override
			public boolean canInsert(ItemStack stack) {
				if (stack.isEnchantable()) {
					for (RegistryEntry<Enchantment> enchantment : getAllEnchantments()) {
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
				validEnchantments.clear();
				selectedEnchantments.clear();
				viewIndex = 0;
				enchantingStack = ItemStack.EMPTY;
				repairIngredient = Ingredient.EMPTY;
				cost = 0;
				player.getInventory().offerOrDrop(slots.get(2).getStack());
				super.onTakeItem(player, stack);
			}
		});
		addSlot(new Slot(inventory, 1, 35, 31) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return stack.isOf(Items.LAPIS_LAZULI);
			}
		});
		addSlot(new Slot(inventory, 2, 25, 51) {
			@Override
			public boolean canInsert(ItemStack stack) {
				return getRepairIngredient(slots.getFirst().getStack()).test(stack);
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
			if (index == 0 || index == 1 || index == 2) {
				if (!insertItem(stackInSlot, 3, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (repairIngredient.test(stackInSlot)) {
				if (!insertItem(stackInSlot, 2, 3, false)) {
					return ItemStack.EMPTY;
				}
			} else if (stackInSlot.isOf(Items.LAPIS_LAZULI)) {
				if (!insertItem(stackInSlot, 1, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!slots.getFirst().hasStack() && slots.getFirst().canInsert(stackInSlot)) {
				slots.getFirst().setStack(stackInSlot.split(1));
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
					ItemStack stack = slots.getFirst().getStack();
					for (RegistryEntry<Enchantment> enchantment : selectedEnchantments) {
						stack.addEnchantment(enchantment, EnchancementUtil.alterLevel(stack, enchantment));
					}
					if (!player.isCreative() && cost > 0) {
						player.applyEnchantmentCosts(stack, cost);
					}
					player.incrementStat(Stats.ENCHANT_ITEM);
					Criteria.ENCHANTED_ITEM.trigger((ServerPlayerEntity) player, stack, cost);
					world.playSound(null, pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1, world.random.nextFloat() * 0.1F + 0.9F);
					if (!player.isCreative() && cost > 0) {
						slots.get(1).getStack().decrement(cost);
						if (!getRepairIngredient(slots.get(0).getStack()).isEmpty()) {
							slots.get(2).getStack().decrement(cost);
						}
					}
					inventory.markDirty();
					onContentChanged(inventory);
				});
				return true;
			}
		} else if (id == 1) {
			updateViewIndex(true);
			return true;
		} else if (id == 2) {
			updateViewIndex(false);
			return true;
		} else if (id > 2 && id < 8) {
			RegistryEntry<Enchantment> enchantment = getEnchantmentFromViewIndex(id - PAGE_SIZE);
			if (selectedEnchantments.contains(enchantment)) {
				selectedEnchantments.remove(enchantment);
			} else {
				selectedEnchantments.add(enchantment);
			}
			cost = getCost(slots.getFirst().getStack());
			if (player instanceof ServerPlayerEntity serverPlayer) {
				SyncEnchantingTableCostPayload.send(serverPlayer, cost);
			}
			return true;
		}
		return false;
	}

	@Override
	public void onContentChanged(Inventory inventory) {
		if (inventory == this.inventory) {
			ItemStack stack = slots.getFirst().getStack();
			if (enchantingStack != stack) {
				validEnchantments.clear();
				selectedEnchantments.clear();
				viewIndex = 0;
				enchantingStack = stack;
				repairIngredient = getRepairIngredient(stack);
				cost = 0;
				for (RegistryEntry<Enchantment> enchantment : getAllEnchantments()) {
					if (isEnchantmentAllowed(enchantment, stack) && !EnchancementUtil.isDefaultEnchantment(stack, enchantment)) {
						validEnchantments.add(enchantment);
					}
				}
				validEnchantments.sort(Comparator.comparing(e -> e.getKey().get().getValue().getPath()));
				super.onContentChanged(inventory);
			}
		}
	}

	public RegistryEntry<Enchantment> getEnchantmentFromViewIndex(int index) {
		if (validEnchantments.size() <= PAGE_SIZE) {
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
			if (player.experienceLevel >= cost && slots.get(1).getStack().getCount() >= cost) {
				if (!getRepairIngredient(slots.get(0).getStack()).isEmpty()) {
					return slots.get(2).getStack().getCount() >= cost;
				}
				return true;
			}
		}
		return false;
	}

	public Ingredient getRepairIngredient() {
		return repairIngredient;
	}

	private Ingredient getRepairIngredient(ItemStack stack) {
		Item item = stack.getItem();
		Ingredient ingredient = ENCHANTING_MATERIAL_MAP.getOrDefault(Registries.ITEM.getEntry(item), Ingredient.EMPTY);
		if (ingredient.isEmpty()) {
			if (item instanceof ArmorItem armorItem) {
				Ingredient repairIngredient = armorItem.getMaterial().value().repairIngredient().get();
				if (!repairIngredient.isEmpty()) {
					ingredient = repairIngredient;
				}
			} else if (item instanceof ToolItem toolItem) {
				Ingredient repairIngredient = toolItem.getMaterial().getRepairIngredient();
				if (!repairIngredient.isEmpty()) {
					ingredient = repairIngredient;
				}
			}
		}
		return ingredient;
	}

	private int calculateBookshelfCount() {
		float[] bookshelfCountArray = {0};
		context.run((world, pos) -> {
			for (BlockPos offset : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
				if (canAccessPowerProvider(world, pos, offset)) {
					bookshelfCountArray[0]++;
				} else if (world.getBlockEntity(pos.add(offset)) instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity && world.getBlockState(pos.add(offset.getX() / 2, offset.getY(), offset.getZ() / 2)).isIn(BlockTags.ENCHANTMENT_POWER_TRANSMITTER)) {
					bookshelfCountArray[0] += chiseledBookshelfBlockEntity.getFilledSlotCount() / 3F;
				}
			}
		});
		return Math.min(15, (int) bookshelfCountArray[0]);
	}

	public int getCost() {
		return cost;
	}

	private int getCost(ItemStack stack) {
		int enchantability = 13;
		if (stack.getItem() instanceof ArmorItem armorItem) {
			enchantability = armorItem.getEnchantability();
		} else if (stack.getItem() instanceof ToolItem toolItem) {
			enchantability = toolItem.getEnchantability();
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

	// client
	public void setCost(int cost) {
		this.cost = cost;
	}

	private List<RegistryEntry.Reference<Enchantment>> getAllEnchantments() {
		return world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).streamEntries().toList();
	}

	// clone of vanilla method because for some reason quilt always returns true
	private static boolean canAccessPowerProvider(World world, BlockPos tablePos, BlockPos providerOffset) {
		return world.getBlockState(tablePos.add(providerOffset)).isIn(BlockTags.ENCHANTMENT_POWER_PROVIDER) && world.getBlockState(tablePos.add(providerOffset.getX() / 2, providerOffset.getY(), providerOffset.getZ() / 2)).isIn(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
	}

	private static boolean isEnchantmentAllowed(RegistryEntry<Enchantment> enchantment, ItemStack stack) {
		if (enchantment.isIn(ModEnchantmentTags.NEVER_SELECTABLE)) {
			return false;
		}
		if (stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)) {
			if (enchantment.isIn(ModEnchantmentTags.ALWAYS_SELECTABLE)) {
				return true;
			}
			return ModConfig.allowTreasureEnchantmentsInEnchantingTable || !enchantment.isIn(EnchantmentTags.TREASURE);
		}
		return false;
	}
}
