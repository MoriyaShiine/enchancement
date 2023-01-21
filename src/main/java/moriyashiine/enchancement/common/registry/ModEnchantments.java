/*
 * All Rights Reserved (c) 2022 MoriyaShiine
 */

package moriyashiine.enchancement.common.registry;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModEnchantments {
	//helmet
	public static final Enchantment ASSIMILATION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	public static final Enchantment PERCEPTION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	public static final Enchantment VEIL = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_HEAD, EquipmentSlot.HEAD);
	//chestplate
	public static final Enchantment AMPHIBIOUS = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST);
	public static final Enchantment STRAFE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST);
	public static final Enchantment WARDENSPINE = new WardenspineEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST);
	//leggings
	public static final Enchantment DASH = new NoImpactEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);
	public static final Enchantment IMPACT = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);
	public static final Enchantment SLIDE = new NoImpactEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);
	//boots
	public static final Enchantment ACCELERATION = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
	public static final Enchantment BOUNCY = new BouncyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
	public static final Enchantment GALE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
	//sword
	public static final Enchantment BERSERK = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
	public static final Enchantment FROSTBITE = new FrostbiteEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
	//bow
	public static final Enchantment CHAOS = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment DELAY = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment PHASING = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.BOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	//crossbow
	public static final Enchantment BRIMSTONE = new BrimstoneEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.CROSSBOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment SCATTER = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.CROSSBOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment TORCH = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.CROSSBOW, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	//trident
	public static final Enchantment LEECH = new LeechEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.TRIDENT, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment WARP = new NoRiptideEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.TRIDENT, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	//pickaxe
	public static final Enchantment EXTRACTING = new NoSilkTouchEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	public static final Enchantment MOLTEN = new NoSilkTouchEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	//axe
	public static final Enchantment BEHEADING = new AxeEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	public static final Enchantment LUMBERJACK = new AxeEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	//shovel
	public static final Enchantment BURY = new ShovelEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	public static final Enchantment SCOOPING = new ShovelEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
	//fishing rod
	public static final Enchantment DISARM = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
	public static final Enchantment GRAPPLE = new EmptyEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);

	public static void init() {
		//helmet
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("assimilation"), ASSIMILATION);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("perception"), PERCEPTION);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("veil"), VEIL);
		//chestplate
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("amphibious"), AMPHIBIOUS);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("strafe"), STRAFE);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("wardenspine"), WARDENSPINE);
		//leggings
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("dash"), DASH);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("impact"), IMPACT);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("slide"), SLIDE);
		//boots
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("acceleration"), ACCELERATION);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("bouncy"), BOUNCY);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("gale"), GALE);
		//sword
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("berserk"), BERSERK);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("frostbite"), FROSTBITE);
		//bow
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("chaos"), CHAOS);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("delay"), DELAY);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("phasing"), PHASING);
		//crossbow
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("brimstone"), BRIMSTONE);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("scatter"), SCATTER);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("torch"), TORCH);
		//trident
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("leech"), LEECH);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("warp"), WARP);
		//pickaxe
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("extracting"), EXTRACTING);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("molten"), MOLTEN);
		//axe
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("beheading"), BEHEADING);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("lumberjack"), LUMBERJACK);
		//shovel
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("bury"), BURY);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("scooping"), SCOOPING);
		//fishing rod
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("disarm"), DISARM);
		Registry.register(Registries.ENCHANTMENT, Enchancement.id("grapple"), GRAPPLE);
	}
}
