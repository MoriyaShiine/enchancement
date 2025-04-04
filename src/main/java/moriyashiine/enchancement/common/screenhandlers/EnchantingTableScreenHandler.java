/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */
package moriyashiine.enchancement.common.screenhandlers;

import moriyashiine.enchancement.client.payload.SyncBookshelvesPayload;
import moriyashiine.enchancement.client.payload.SyncEnchantingTableCostPayload;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModScreenHandlerTypes;
import moriyashiine.enchancement.common.tag.ModEnchantmentTags;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Blocks;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
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
	public static final Map<Item, EnchantingMaterial> ENCHANTING_MATERIAL_MAP = new HashMap<>();
	public static final int PAGE_SIZE = 4;

	public final List<RegistryEntry<Enchantment>> validEnchantments = new ArrayList<>(), selectedEnchantments = new ArrayList<>();
	public final Set<RegistryEntry<Enchantment>> chiseledEnchantments = new HashSet<>();
	public int viewIndex = 0;

	private ItemStack enchantingStack = ItemStack.EMPTY;
	private EnchantingMaterial enchantingMaterial = EnchantingMaterial.EMPTY;
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
		if (playerInventory.player instanceof ServerPlayerEntity player) {
			collectBookshelves(player);
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
				enchantingMaterial = EnchantingMaterial.EMPTY;
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
				return getEnchantingMaterial(slots.getFirst().getStack()).test(stack);
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
			} else if (getEnchantingMaterial().test(stackInSlot)) {
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
						if (!getEnchantingMaterial(slots.get(0).getStack()).isEmpty()) {
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
				enchantingMaterial = getEnchantingMaterial(stack);
				cost = 0;
				for (RegistryEntry<Enchantment> enchantment : getAllEnchantments()) {
					if (isEnchantmentAllowed(enchantment, stack) && !EnchancementUtil.isDefaultEnchantment(stack, enchantment)) {
						validEnchantments.add(enchantment);
					}
				}
				validEnchantments.sort(Comparator.comparing(e -> e.getKey().orElse(ModEnchantments.EMPTY_KEY).getValue().getPath()));
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
				if (!getEnchantingMaterial(slots.get(0).getStack()).isEmpty()) {
					return slots.get(2).getStack().getCount() >= cost;
				}
				return true;
			}
		}
		return false;
	}

	public EnchantingMaterial getEnchantingMaterial() {
		return enchantingMaterial;
	}

	private EnchantingMaterial getEnchantingMaterial(ItemStack stack) {
		EnchantingMaterial material = ENCHANTING_MATERIAL_MAP.getOrDefault(stack.getItem(), EnchantingMaterial.EMPTY);
		if (material.isEmpty()) {
			Set<ItemConvertible> items = new HashSet<>();
			if (stack.contains(DataComponentTypes.REPAIRABLE)) {
				stack.get(DataComponentTypes.REPAIRABLE).items().forEach(item -> items.add(item.value() == Items.NETHERITE_INGOT ? Items.DIAMOND : item.value()));
			}
			if (items.isEmpty()) {
				material = new EnchantingMaterial(Ingredient.fromTag(world.getRegistryManager().getOrThrow(RegistryKeys.ITEM).getOrThrow(ModItemTags.DEFAULT_ENCHANTING_MATERIAL)));
			} else {
				material = new EnchantingMaterial(Ingredient.ofItems(items.toArray(new ItemConvertible[0])));
			}
		}
		return material;
	}

	private void collectBookshelves(ServerPlayerEntity player) {
		context.run((world, pos) -> {
			chiseledEnchantments.clear();
			bookshelfCount = 0;
			for (BlockPos offset : EnchantingTableBlock.POWER_PROVIDER_OFFSETS) {
				if (EnchantingTableBlock.canAccessPowerProvider(world, pos, offset)) {
					if (world.getBlockEntity(pos.add(offset)) instanceof ChiseledBookshelfBlockEntity chiseledBookshelfBlockEntity) {
						bookshelfCount += chiseledBookshelfBlockEntity.getFilledSlotCount() / 3;
						if (ModConfig.overhaulEnchantingTable.chiseledMode()) {
							for (ItemStack stack : chiseledBookshelfBlockEntity) {
								chiseledEnchantments.addAll(EnchantmentHelper.getEnchantments(stack).getEnchantments());
							}
						}
					} else {
						bookshelfCount++;
					}
				}
			}
			bookshelfCount = Math.min(15, bookshelfCount);
			SyncBookshelvesPayload.send(player, chiseledEnchantments, bookshelfCount);
		});
	}

	public int getCost() {
		return cost;
	}

	private int getCost(ItemStack stack) {
		double cost = 60F / (Math.max(1, EnchancementUtil.getEnchantmentValue(stack) + bookshelfCount));
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
		return world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).streamEntries().toList();
	}

	private boolean isEnchantmentAllowed(RegistryEntry<Enchantment> enchantment, ItemStack stack) {
		if (stack.isEmpty() || enchantment.isIn(ModEnchantmentTags.UNSELECTABLE)) {
			return false;
		}
		if (stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)) {
			if (enchantment.isIn(ModEnchantmentTags.ALWAYS_SELECTABLE)) {
				return true;
			}
			if (!ModConfig.overhaulEnchantingTable.chiseledMode() || chiseledEnchantments.contains(enchantment)) {
				if (ModConfig.overhaulEnchantingTable.allowsTreasure() && enchantment.isIn(EnchantmentTags.TREASURE)) {
					return true;
				}
				return enchantment.isIn(EnchantmentTags.IN_ENCHANTING_TABLE);
			}
		}
		return false;
	}

	public static class EnchantingMaterial {
		public static final PacketCodec<RegistryByteBuf, EnchantingMaterial> PACKET_CODEC = Ingredient.PACKET_CODEC.xmap(EnchantingMaterial::new, material -> material.ingredient);

		public static final EnchantingMaterial EMPTY = new EnchantingMaterial(null);

		private final Ingredient ingredient;

		public EnchantingMaterial(Ingredient ingredient) {
			this.ingredient = ingredient;
		}

		@SuppressWarnings("deprecation")
		public RegistryEntry<Item> get(int index) {
			return ingredient.getMatchingItems().toList().get(index);
		}

		@SuppressWarnings("deprecation")
		public int size() {
			if (ingredient == null) {
				return 0;
			}
			return ingredient.getMatchingItems().toList().size();
		}

		public boolean isEmpty() {
			return size() == 0;
		}

		public boolean test(ItemStack stack) {
			if (ingredient == null) {
				return false;
			}
			return ingredient.test(stack);
		}
	}
}
