package gay.amethyst.silly_things.items

import gay.amethyst.silly_things.items.weapons.Justitia
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent

@EventBusSubscriber
object CooldownHandler {
	val wontAttackOnCooldown = mutableListOf<Item>()

	fun dontAttackOnCooldown(item: ItemLike) {
		if (!wontAttackOnCooldown.contains(item)) {
			wontAttackOnCooldown.add(item.asItem())
		}
	}

	@SubscribeEvent
	fun onAttack(event: AttackEntityEvent) {
		val player = event.entity
		val item = player.mainHandItem.item
		if (wontAttackOnCooldown.contains(item) && player.cooldowns.isOnCooldown(item)) event.isCanceled = true
	}

	val getsTaggedOnCrit = mutableListOf<Item>()
	val critTagged = mutableListOf<ItemStack>()

	fun tagOnCrit(item: ItemLike) {
		if (!getsTaggedOnCrit.contains(item)) {
			getsTaggedOnCrit.add(item.asItem())
		}
	}

	fun hasCritTag(stack: ItemStack) = critTagged.contains(stack)

	fun consumeCritTag(stack: ItemStack): Boolean {
		if (hasCritTag(stack)) {
			critTagged.remove(stack)
			return true
		} else {
			return false
		}
	}

	@SubscribeEvent
	fun onCrit(event: CriticalHitEvent) {
		if (event.isVanillaCritical && event.entity.mainHandItem.item in getsTaggedOnCrit) {
			critTagged.add(event.entity.mainHandItem)
		}
	}
}