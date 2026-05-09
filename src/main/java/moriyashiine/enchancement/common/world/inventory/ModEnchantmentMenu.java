/*
 * Copyright (c) MoriyaShiine. All Rights Reserved.
 */

package moriyashiine.enchancement.common.world.inventory;

import moriyashiine.enchancement.client.payload.SyncBookshelvesPayload;
import moriyashiine.enchancement.client.payload.SyncEnchantingTableCostPayload;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModMenuTypes;
import moriyashiine.enchancement.common.tag.ModItemTags;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.config.OverhaulMode;
import moriyashiine.enchancement.common.util.enchantment.EnchantingMaterial;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;

import java.util.*;

public class ModEnchantmentMenu extends AbstractContainerMenu {
	public static final int PAGE_SIZE = 4;

	public final List<Holder<Enchantment>> validEnchantments = new ArrayList<>(), selectedEnchantments = new ArrayList<>();
	public final Set<Holder<Enchantment>> chiseledEnchantments = new HashSet<>();
	public int viewIndex = 0;

	private ItemStack enchantingStack = ItemStack.EMPTY;
	private EnchantingMaterial enchantingMaterial = EnchantingMaterial.EMPTY;
	private int bookshelfCount = 0, cost = 0;

	private final Container enchantSlots = new SimpleContainer(3) {
		@Override
		public void setChanged() {
			super.setChanged();
			slotsChanged(this);
		}
	};
	private final ContainerLevelAccess access;
	private final Level level;

	public ModEnchantmentMenu(int syncId, Inventory inventory) {
		this(syncId, inventory, ContainerLevelAccess.NULL, inventory.player.level());
	}

	public ModEnchantmentMenu(int syncId, Inventory inventory, ContainerLevelAccess access, Level level) {
		super(ModMenuTypes.ENCHANTING_TABLE, syncId);
		this.access = access;
		this.level = level;
		if (inventory.player instanceof ServerPlayer player) {
			collectBookshelves(player);
		}
		addSlot(new Slot(enchantSlots, 0, 15, 31) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				if (isEnchantable(stack)) {
					for (Holder<Enchantment> enchantment : getAllEnchantments()) {
						if (isEnchantmentAllowed(enchantment, stack)) {
							return true;
						}
					}
				}
				return false;
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}

			@Override
			public void onTake(Player player, ItemStack stack) {
				validEnchantments.clear();
				selectedEnchantments.clear();
				viewIndex = 0;
				enchantingStack = ItemStack.EMPTY;
				enchantingMaterial = EnchantingMaterial.EMPTY;
				cost = 0;
				player.getInventory().placeItemBackInInventory(slots.get(2).getItem().copyAndClear());
				super.onTake(player, stack);
			}
		});
		addSlot(new Slot(enchantSlots, 1, 35, 31) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return stack.is(Items.LAPIS_LAZULI);
			}
		});
		addSlot(new Slot(enchantSlots, 2, 25, 51) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return getEnchantingMaterial(slots.getFirst().getItem()).test(stack);
			}
		});
		addStandardInventorySlots(inventory, 8, 84);
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(access, player, Blocks.ENCHANTING_TABLE);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {
		ItemStack clicked = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);
		if (slot.hasItem()) {
			ItemStack slotStack = slot.getItem();
			clicked = slotStack.copy();
			if (slotIndex == 0 || slotIndex == 1 || slotIndex == 2) {
				if (!moveItemStackTo(slotStack, 3, 38, false)) {
					return ItemStack.EMPTY;
				}
			} else if (getEnchantingMaterial().test(slotStack)) {
				if (!moveItemStackTo(slotStack, 2, 3, false)) {
					return ItemStack.EMPTY;
				}
			} else if (slotStack.is(Items.LAPIS_LAZULI)) {
				if (!moveItemStackTo(slotStack, 1, 2, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!slots.getFirst().hasItem() && slots.getFirst().mayPlace(slotStack)) {
				slots.getFirst().setByPlayer(slotStack.split(1));
			} else {
				return ItemStack.EMPTY;
			}
			if (slotStack.isEmpty()) {
				slot.setByPlayer(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}
			if (slotStack.getCount() == clicked.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, slotStack);
		}
		return clicked;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		access.execute((_, _) -> clearContainer(player, enchantSlots));
	}

	@Override
	public boolean clickMenuButton(Player player, int buttonId) {
		if (buttonId == 0) {
			if (canEnchant(player, player.isCreative())) {
				access.execute((level, pos) -> {
					ItemStack stack = slots.getFirst().getItem();
					for (Holder<Enchantment> enchantment : selectedEnchantments) {
						stack.enchant(enchantment, EnchancementUtil.alterLevel(stack, enchantment));
					}
					if (!player.isCreative() && cost > 0) {
						player.onEnchantmentPerformed(stack, cost);
					}
					player.awardStat(Stats.ENCHANT_ITEM);
					CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayer) player, stack, cost);
					level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1, level.getRandom().nextFloat() * 0.1F + 0.9F);
					if (!player.isCreative() && cost > 0) {
						slots.get(1).getItem().shrink(cost);
						if (!getEnchantingMaterial(slots.get(0).getItem()).isEmpty()) {
							slots.get(2).getItem().shrink(cost);
						}
					}
					enchantSlots.setChanged();
					slotsChanged(enchantSlots);
				});
				return true;
			}
		} else if (buttonId == 1) {
			updateViewIndex(true);
			return true;
		} else if (buttonId == 2) {
			updateViewIndex(false);
			return true;
		} else if (buttonId > 2 && buttonId < 8) {
			Holder<Enchantment> enchantment = getEnchantmentFromViewIndex(buttonId - PAGE_SIZE);
			if (selectedEnchantments.contains(enchantment)) {
				selectedEnchantments.remove(enchantment);
			} else {
				selectedEnchantments.add(enchantment);
			}
			cost = getCost(slots.getFirst().getItem());
			if (player instanceof ServerPlayer serverPlayer) {
				SyncEnchantingTableCostPayload.send(serverPlayer, cost);
			}
			return true;
		}
		return false;
	}

	@Override
	public void slotsChanged(Container container) {
		if (container == enchantSlots) {
			ItemStack stack = slots.getFirst().getItem();
			if (enchantingStack != stack) {
				validEnchantments.clear();
				selectedEnchantments.clear();
				viewIndex = 0;
				enchantingStack = stack;
				enchantingMaterial = getEnchantingMaterial(stack);
				cost = 0;
				for (Holder<Enchantment> enchantment : getAllEnchantments()) {
					if (isEnchantmentAllowed(enchantment, stack) && !EnchancementUtil.isDefaultEnchantment(stack, enchantment)) {
						validEnchantments.add(enchantment);
					}
				}
				validEnchantments.sort(Comparator.comparing(Holder::getRegisteredName));
				super.slotsChanged(container);
			}
		}
	}

	public Holder<Enchantment> getEnchantmentFromViewIndex(int index) {
		if (validEnchantments.size() <= PAGE_SIZE) {
			return validEnchantments.get(index);
		}
		return validEnchantments.get((index + viewIndex) % validEnchantments.size());
	}

	public boolean canEnchant(Player player, boolean simulate) {
		if (slots.get(0).hasItem()) {
			if (!isEnchantable(slots.get(0).getItem())) {
				return false;
			}
			if (simulate) {
				return true;
			}
			if (player.experienceLevel >= cost && slots.get(1).getItem().getCount() >= cost) {
				if (!getEnchantingMaterial(slots.get(0).getItem()).isEmpty()) {
					return slots.get(2).getItem().getCount() >= cost;
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
		EnchantingMaterial material = EnchantingMaterial.MATERIAL_MAP.getOrDefault(stack.getItem(), EnchantingMaterial.EMPTY);
		if (material.isEmpty()) {
			Set<ItemLike> items = new HashSet<>();
			if (stack.has(DataComponents.REPAIRABLE)) {
				stack.get(DataComponents.REPAIRABLE).items().forEach(item -> items.add(item.value() == Items.NETHERITE_INGOT ? Items.DIAMOND : item.value()));
			}
			if (items.isEmpty()) {
				material = new EnchantingMaterial(Ingredient.of(level.registryAccess().lookupOrThrow(Registries.ITEM).getOrThrow(ModItemTags.DEFAULT_ENCHANTING_MATERIAL)));
			} else {
				material = new EnchantingMaterial(Ingredient.of(items.toArray(new ItemLike[0])));
			}
		}
		return material;
	}

	private void collectBookshelves(ServerPlayer player) {
		access.execute((level, pos) -> {
			chiseledEnchantments.clear();
			bookshelfCount = 0;
			for (BlockPos offset : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
				if (EnchantingTableBlock.isValidBookShelf(level, pos, offset)) {
					if (level.getBlockEntity(pos.offset(offset)) instanceof ChiseledBookShelfBlockEntity chiseledBookshelfBlockEntity) {
						bookshelfCount += chiseledBookshelfBlockEntity.count() / 3;
						if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && !player.isCreative()) {
							for (ItemStack stack : chiseledBookshelfBlockEntity) {
								chiseledEnchantments.addAll(EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet());
							}
						}
					} else {
						bookshelfCount++;
					}
				}
			}
			bookshelfCount = Math.min(15, bookshelfCount);
			if (ModConfig.overhaulEnchanting == OverhaulMode.CHISELED && player.isCreative()) {
				Registry<Enchantment> enchantmentRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
				enchantmentRegistry.forEach(enchantment -> chiseledEnchantments.add(enchantmentRegistry.wrapAsHolder(enchantment)));
			}
			SyncBookshelvesPayload.send(player, chiseledEnchantments, bookshelfCount);
		});
	}

	public int getCost() {
		return cost;
	}

	private int getCost(ItemStack stack) {
		double cost = 60F / (Math.max(1, EnchancementUtil.getEnchantmentValue(stack) + bookshelfCount));
		if (bookshelfCount == 15) {
			cost = Mth.floor(cost);
		} else {
			cost = Mth.ceil(cost);
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

	private List<Holder.Reference<Enchantment>> getAllEnchantments() {
		return level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).listElements().toList();
	}

	private boolean isEnchantmentAllowed(Holder<Enchantment> enchantment, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		if (stack.canBeEnchantedWith(enchantment, EnchantingContext.ACCEPTABLE)) {
			if (ModConfig.overhaulEnchanting != OverhaulMode.CHISELED || chiseledEnchantments.contains(enchantment)) {
				if (ModConfig.overhaulEnchanting.allowsTreasure() && enchantment.is(EnchantmentTags.TREASURE)) {
					return true;
				}
				return enchantment.is(EnchantmentTags.IN_ENCHANTING_TABLE);
			}
		}
		return false;
	}

	public static boolean isEnchantable(ItemStack stack) {
		ItemEnchantments enchantments = stack.get(DataComponents.ENCHANTMENTS);
		return enchantments != null && enchantments.isEmpty();
	}
}
