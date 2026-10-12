package gay.amethyst.silly_things.items.weapons

import gay.amethyst.silly_things.items.CooldownHandler
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Arrow
import net.minecraft.world.entity.projectile.SpectralArrow
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

class SolemnLament(props: Properties) : Item(
	props.stacksTo(1)
) {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
		val stack = player.getItemInHand(usedHand)

		val arrow = if (CooldownHandler.solemnLamentAltFire.contains(stack)) {
			CooldownHandler.solemnLamentAltFire.remove(stack)
			SpectralArrow(level, player, ItemStack.EMPTY, stack)
		} else {
			CooldownHandler.solemnLamentAltFire.add(stack)
			Arrow(level, player, ItemStack.EMPTY, stack)
		}
		level.addFreshEntity(arrow)
		arrow.shoot(player.lookAngle.x, player.lookAngle.y, player.lookAngle.z, 3f, 0f)

		return InteractionResultHolder.pass(stack)
	}
}