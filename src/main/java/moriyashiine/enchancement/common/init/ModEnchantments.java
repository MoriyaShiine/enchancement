/*
 * All Rights Reserved (c) MoriyaShiine
 */

package moriyashiine.enchancement.common.init;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;

public class ModEnchantments {
	// helmet
	public static final Enchantment ASSIMILATION = new Enchantment(properties(ItemTags.HEAD_ARMOR_ENCHANTABLE, 2, EquipmentSlot.HEAD));
	public static final Enchantment PERCEPTION = new Enchantment(properties(ItemTags.HEAD_ARMOR_ENCHANTABLE, 2, EquipmentSlot.HEAD));
	public static final Enchantment VEIL = new Enchantment(properties(ItemTags.HEAD_ARMOR_ENCHANTABLE, 2, EquipmentSlot.HEAD));
	// chestplate
	public static final Enchantment AMPHIBIOUS = new Enchantment(properties(ItemTags.CHEST_ARMOR_ENCHANTABLE, 2, EquipmentSlot.CHEST));
	public static final Enchantment STRAFE = new Enchantment(properties(ItemTags.CHEST_ARMOR_ENCHANTABLE, 2, EquipmentSlot.CHEST));
	public static final Enchantment WARDENSPINE = new WardenspineEnchantment(properties(ItemTags.CHEST_ARMOR_ENCHANTABLE, 2, EquipmentSlot.CHEST));
	// leggings
	public static final Enchantment ADRENALINE = new Enchantment(properties(ItemTags.LEG_ARMOR_ENCHANTABLE, 2, EquipmentSlot.LEGS));
	public static final Enchantment DASH = new Enchantment(properties(ItemTags.LEG_ARMOR_ENCHANTABLE, 2, EquipmentSlot.LEGS));
	public static final Enchantment SLIDE = new Enchantment(properties(ItemTags.LEG_ARMOR_ENCHANTABLE, 2, EquipmentSlot.LEGS));
	// boots
	public static final Enchantment BOUNCY = new BouncyEnchantment(properties(ItemTags.FOOT_ARMOR_ENCHANTABLE, 2, EquipmentSlot.FEET));
	public static final Enchantment BUOY = new Enchantment(properties(ItemTags.FOOT_ARMOR_ENCHANTABLE, 2, EquipmentSlot.FEET));
	public static final Enchantment GALE = new Enchantment(properties(ItemTags.FOOT_ARMOR_ENCHANTABLE, 2, EquipmentSlot.FEET));
	// sword
	public static final Enchantment BERSERK = new Enchantment(properties(ItemTags.SWORD_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	public static final Enchantment FROSTBITE = new FrostbiteEnchantment(properties(ItemTags.SWORD_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	// bow
	public static final Enchantment CHAOS = new Enchantment(properties(ItemTags.BOW_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	public static final Enchantment DELAY = new Enchantment(properties(ItemTags.BOW_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	public static final Enchantment PHASING = new Enchantment(properties(ItemTags.BOW_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	// crossbow
	public static final Enchantment BRIMSTONE = new BrimstoneEnchantment(properties(ItemTags.CROSSBOW_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	public static final Enchantment SCATTER = new Enchantment(properties(ItemTags.CROSSBOW_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	public static final Enchantment TORCH = new Enchantment(properties(ItemTags.CROSSBOW_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	// trident
	public static final Enchantment LEECH = new LeechEnchantment(properties(ItemTags.TRIDENT_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	public static final Enchantment WARP = new NoRiptideEnchantment(properties(ItemTags.TRIDENT_ENCHANTABLE, 2, EquipmentSlot.MAINHAND));
	// pickaxe
	public static final Enchantment EXTRACTING = new NoSilkTouchEnchantment(properties(ItemTags.PICKAXES, 1, EquipmentSlot.MAINHAND));
	public static final Enchantment MOLTEN = new MoltenEnchantment(properties(ItemTags.PICKAXES, 1, EquipmentSlot.MAINHAND));
	// axe
	public static final Enchantment BEHEADING = new Enchantment(properties(ItemTags.AXES, 1, EquipmentSlot.MAINHAND));
	public static final Enchantment LUMBERJACK = new Enchantment(properties(ItemTags.AXES, 1, EquipmentSlot.MAINHAND));
	// shovel
	public static final Enchantment BURY = new Enchantment(properties(ItemTags.SHOVELS, 1, EquipmentSlot.MAINHAND));
	public static final Enchantment SCOOPING = new ScoopingEnchantment(properties(ItemTags.SHOVELS, 1, EquipmentSlot.MAINHAND));
	// fishing rod
	public static final Enchantment DISARM = new Enchantment(properties(ItemTags.FISHING_ENCHANTABLE, 1, EquipmentSlot.MAINHAND));
	public static final Enchantment GRAPPLE = new Enchantment(properties(ItemTags.FISHING_ENCHANTABLE, 1, EquipmentSlot.MAINHAND));

	private static Enchantment.Properties properties(TagKey<Item> supportedItems, int maxLevel, EquipmentSlot... slots) {
		return Enchantment.properties(supportedItems, 5, maxLevel, Enchantment.leveledCost(5, 6), Enchantment.leveledCost(11, 6), 2, slots);
	}

	public static void init() {
		// helmet
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("assimilation"), ASSIMILATION);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("perception"), PERCEPTION);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("veil"), VEIL);
		// chestplate
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("amphibious"), AMPHIBIOUS);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("strafe"), STRAFE);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("wardenspine"), WARDENSPINE);
		// leggings
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("adrenaline"), ADRENALINE);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("dash"), DASH);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("slide"), SLIDE);
		// boots
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("bouncy"), BOUNCY);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("buoy"), BUOY);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("gale"), GALE);
		// sword
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("berserk"), BERSERK);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("frostbite"), FROSTBITE);
		// bow
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("chaos"), CHAOS);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("delay"), DELAY);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("phasing"), PHASING);
		// crossbow
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("brimstone"), BRIMSTONE);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("scatter"), SCATTER);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("torch"), TORCH);
		// trident
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("leech"), LEECH);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("warp"), WARP);
		// pickaxe
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("extracting"), EXTRACTING);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("molten"), MOLTEN);
		// axe
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("beheading"), BEHEADING);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("lumberjack"), LUMBERJACK);
		// shovel
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("bury"), BURY);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("scooping"), SCOOPING);
		// fishing rod
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("disarm"), DISARM);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("grapple"), GRAPPLE);
	}
}
