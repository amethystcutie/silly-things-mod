package gay.amethyst.silly_things.entities.projectiles

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.ThrownTrident
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class SolemnLamentButterfly(entityType: EntityType<out SolemnLamentButterfly>, level: Level) : AbstractArrow(entityType, level) {
	override fun getDefaultPickupItem(): ItemStack = ItemStack.EMPTY
}