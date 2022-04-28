package moriyashiine.enchancement.mixin.vanillachanges;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {
	@Shadow
	private static void forEachEnchantment(EnchantmentHelper.Consumer consumer, ItemStack stacks) {
		throw new UnsupportedOperationException();
	}

	@Shadow
	private static void forEachEnchantment(EnchantmentHelper.Consumer consumer, Iterable<ItemStack> stacks) {
		throw new UnsupportedOperationException();
	}

	@Inject(method = "getAttackDamage", at = @At("HEAD"), cancellable = true)
	private static void enchancement$singleLevelModeAttackDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir) {
		if (Enchancement.getConfig().singleLevelMode) {
			MutableFloat mutableFloat = new MutableFloat();
			forEachEnchantment((Enchantment enchantment, int level) -> mutableFloat.add(enchantment.getAttackDamage(enchantment.getMaxLevel(), group)), stack);
			cir.setReturnValue(mutableFloat.floatValue());
		}
	}

	@Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
	private static void enchancement$singleLevelMode(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (cir.getReturnValueI() > 0 && Enchancement.getConfig().singleLevelMode) {
			cir.setReturnValue(enchantment.getMaxLevel());
		}
	}

	@Inject(method = "getLoyalty", at = @At("HEAD"), cancellable = true)
	private static void enchancement$giveAllTridentsLoyalty(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().allTridentsHaveLoyalty && stack.getItem() instanceof TridentItem) {
			cir.setReturnValue(Enchantments.LOYALTY.getMaxLevel());
		}
	}

	@Inject(method = "getLure", at = @At("HEAD"), cancellable = true)
	private static void enchancement$luckOfTheSeaWithLure(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().luckOfTheSeaHasLure) {
			int level = EnchantmentHelper.getLuckOfTheSea(stack);
			if (level > 0) {
				cir.setReturnValue(Enchantments.LURE.getMaxLevel());
			}
		}
	}

	@ModifyExpressionValue(method = "getPossibleEntries", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
	private static int enchancement$singleLevelMode(int value) {
		if (Enchancement.getConfig().singleLevelMode) {
			return 1;
		}
		return value;
	}

	@Inject(method = "getProtectionAmount", at = @At("HEAD"), cancellable = true)
	private static void enchancement$singleLevelModeProtectionAmount(Iterable<ItemStack> equipment, DamageSource source, CallbackInfoReturnable<Integer> cir) {
		if (Enchancement.getConfig().singleLevelMode) {
			MutableInt mutableInt = new MutableInt();
			forEachEnchantment((Enchantment enchantment, int level) -> mutableInt.add(enchantment.getProtectionAmount(enchantment.getMaxLevel(), source)), equipment);
			cir.setReturnValue(mutableInt.intValue());
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
	private static void enchancement$singleLevelModeTargetDamaged(LivingEntity user, Entity target, CallbackInfo ci) {
		if (Enchancement.getConfig().singleLevelMode) {
			EnchantmentHelper.Consumer consumer = (enchantment, level) -> enchantment.onTargetDamaged(user, target, enchantment.getMaxLevel());
			if (user != null) {
				forEachEnchantment(consumer, user.getItemsEquipped());
			}
			if (user instanceof PlayerEntity) {
				forEachEnchantment(consumer, user.getMainHandStack());
			}
			ci.cancel();
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(method = "onUserDamaged", at = @At("HEAD"), cancellable = true)
	private static void enchancement$singleLevelModeUserDamaged(LivingEntity user, Entity attacker, CallbackInfo ci) {
		if (Enchancement.getConfig().singleLevelMode) {
			EnchantmentHelper.Consumer consumer = (enchantment, level) -> enchantment.onUserDamaged(user, attacker, enchantment.getMaxLevel());
			if (user != null) {
				forEachEnchantment(consumer, user.getItemsEquipped());
			}
			if (attacker instanceof PlayerEntity) {
				forEachEnchantment(consumer, user.getMainHandStack());
			}
			ci.cancel();
		}
	}

	@Inject(method = "set", at = @At("HEAD"), cancellable = true)
	private static void enchancement$singleEnchantmentMode(Map<Enchantment, Integer> enchantments, ItemStack stack, CallbackInfo ci) {
		for (Enchantment enchantment : enchantments.keySet()) {
			if (Registry.ENCHANTMENT.getId(enchantment) == null) {
				enchantments.remove(enchantment);
			}
		}
		if (Enchancement.getConfig().singleEnchantmentMode && stack.hasEnchantments()) {
			ci.cancel();
		}
	}
}
