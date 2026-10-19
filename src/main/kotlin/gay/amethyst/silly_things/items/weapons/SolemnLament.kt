package gay.amethyst.silly_things.items.weapons

import gay.amethyst.silly_things.SillyThings
import gay.amethyst.silly_things.entities.projectiles.SolemnLamentButterfly
import gay.amethyst.silly_things.items.ModDataComponents
import gay.amethyst.silly_things.sounds.weapons.ModWeaponSounds
import gay.amethyst.silly_things.util.text.ItemDescriptions
import gay.amethyst.silly_things.util.text.Rarity
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.HumanoidArm
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ProjectileWeaponItem
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import java.util.function.Predicate
import kotlin.math.cos
import kotlin.math.sin

class SolemnLament(props: Properties) : ProjectileWeaponItem(
	props.durability(9999)
) {
	override fun getName(stack: ItemStack): Component = Component.translatable("item.silly_things.solemn_lament")
		.withColor(Rarity.SOLEMN_LAMENT.hexColour)
		.withStyle(ChatFormatting.BOLD)

	override fun appendHoverText(
		stack: ItemStack, context: TooltipContext, tooltipComponents: MutableList<Component>, tooltipFlag: TooltipFlag
	) {
		tooltipComponents.add(Component.literal("WAW").withColor(Rarity.LOB_WAW.hexColour))

		tooltipComponents.add(
			Component.translatable("item.silly_things.solemn_lament.desc_1")
				.withColor(Rarity.SOLEMN_LAMENT.lightHex)
		)
		tooltipComponents.add(
			Component.translatable("item.silly_things.solemn_lament.desc_2")
				.withColor(Rarity.SOLEMN_LAMENT.lightHex)
		)
		tooltipComponents.add(ItemDescriptions.from(ItemDescriptions.Reference.LOBCORP))

		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
	}

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
		val stack = player.getItemInHand(usedHand)
		val isDummy = stack.isDummy()

		if (stack.isDummy() || usedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(stack)
		if (player.cooldowns.isOnCooldown(this)) return InteractionResultHolder.fail(stack)

		val otherStack = player.getItemInHand(usedHand.other)

		if (!(otherStack.isEmpty || otherStack.`is`(stack.item))) {
			Minecraft.getInstance().player?.displayClientMessage(
				Component.translatable("hint.silly_things.solemn_lament.both_hands").withStyle(
					ChatFormatting.RED
				), true
			)
			return InteractionResultHolder.fail(stack)
		}

		val fireMode = stack.get(ModDataComponents.FIRE_MODE) ?: 0

		val sound = when (fireMode) {
			0 -> ModWeaponSounds.SOLEMN_LAMENT_WHITE.get()
			else -> ModWeaponSounds.SOLEMN_LAMENT_BLACK.get()
		}


		val shootHand = if (stack.get(ModDataComponents.HAND) == 0)
			InteractionHand.MAIN_HAND else InteractionHand.OFF_HAND

		if (level is ServerLevel) {
			level.playSound(
				null,
				player.blockPosition(),
				sound,
				SoundSource.PLAYERS,
				.8f,
				1f
			)

			shoot(level, player, fireMode, shootHand, stack, false, null)
			stack.swapFireMode()

			player.cooldowns.addCooldown(this, 10)
		}

		val swingHand = if (fireMode == 1) shootHand else shootHand.other
		player.swing(swingHand)

		return InteractionResultHolder.pass(stack)
	}

	fun createProjectile(
		level: Level, shooter: LivingEntity, weapon: ItemStack, fireMode: Int, isCrit: Boolean
	): Projectile = SolemnLamentButterfly(
		level, shooter, weapon, weapon.get(ModDataComponents.FIRE_MODE) ?: 0
	)

	override fun shootProjectile(
		shooter: LivingEntity,
		projectile: Projectile,
		index: Int,
		velocity: Float,
		inaccuracy: Float,
		angle: Float,
		target: LivingEntity?
	) {
		projectile.shootFromRotation(
			shooter, shooter.xRot, shooter.yRot + angle, 0f, velocity, 0f
		)
	}

	fun shoot(
		level: ServerLevel,
		shooter: LivingEntity,
		fireMode: Int,
		usedHand: InteractionHand,
		weapon: ItemStack,
		isCrit: Boolean,
		target: LivingEntity?
	) {
		val side = Side.used(shooter, usedHand).switch(fireMode == 1)

		val yaw = Math.toRadians(shooter.yRot.toDouble())
		val offset = side.offset * 0.7

		val spawnPos = shooter.eyePosition.add(
			cos(yaw) * offset, -0.7, sin(yaw) * offset
		)

		val projectile = createProjectile(level, shooter, weapon, fireMode, isCrit)
		projectile.moveTo(spawnPos)
		shootProjectile(shooter, projectile, 0, 1.2f, 1f, 0f, target)
		level.addFreshEntity(projectile)
	}

	@Deprecated("Deprecated in Java")
	override fun getAllSupportedProjectiles(): Predicate<ItemStack?> = Predicate { false }
	override fun getDefaultProjectileRange() = 7

	enum class Side {
		LEFT, RIGHT;

		val offset get() = (2 * ordinal - 1).toDouble()
		val other get() = if (this == LEFT) RIGHT else LEFT

		fun switch(condition: Boolean) = if (condition) other else this

		operator fun not() = other

		val id: Int get() = ordinal

		companion object {
			fun of(index: Int) = entries.getOrNull(index)
			fun of(bool: Boolean) = if (bool) RIGHT else LEFT
			fun used(entity: LivingEntity, usedHand: InteractionHand) =
				of((entity.mainArm == HumanoidArm.RIGHT) == (usedHand == InteractionHand.MAIN_HAND))

			fun used(entity: LivingEntity, usedHandId: Int) =
				of((entity.mainArm == HumanoidArm.RIGHT) == (usedHandId == 0))
		}
	}

	fun getDummyItem(): ItemStack {
		val stack = ItemStack(this)
		stack.set(ModDataComponents.DUMMY, true)
		return stack
	}

	val InteractionHand.other get() =
		if (this == InteractionHand.MAIN_HAND) InteractionHand.OFF_HAND else InteractionHand.MAIN_HAND
	val InteractionHand.slot get() =
		if (this == InteractionHand.MAIN_HAND) EquipmentSlot.MAINHAND else EquipmentSlot.OFFHAND
	val InteractionHand.id get() = this.ordinal

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		super.inventoryTick(stack, level, entity, slotId, isSelected)
		if (entity !is LivingEntity) return

		if (stack.get(ModDataComponents.HAND) == null) stack.set(ModDataComponents.HAND, 0)

		val mainItem = entity.mainHandItem
		val offItem = entity.offhandItem

		var otherStack: ItemStack? = null

		if (mainItem == stack) {
			otherStack = offItem

			if (mainItem.isDummy()) {
				if (offItem.matches(stack) && !offItem.isDummy()) {
					SillyThings.LOGGER.info("real dummy -> dummy real swap")
					mainItem.swapHand()
					entity.setItemInHand(InteractionHand.MAIN_HAND, offItem)
					entity.setItemInHand(InteractionHand.OFF_HAND, mainItem)
				}
				else {
					SillyThings.LOGGER.info("air dummy -> air air")
					mainItem.count = 0
				}
			} else {
				if (offItem.isEmpty) {
					SillyThings.LOGGER.info("air real -> dummy real")
					entity.setItemInHand(InteractionHand.OFF_HAND, getDummyItem())
				} else if (!offItem.matches(stack)) {
					SillyThings.LOGGER.info("item real -> item real warn")
				}
			}
		}
		else if (offItem == stack) {
			otherStack = mainItem

			if (offItem.isDummy()) {
				if (!mainItem.matches(stack) || mainItem.isDummy()) {
					SillyThings.LOGGER.info("dummy air -> air air")
					offItem.count = 0
				}
			} else {
				if (mainItem.isEmpty) {
					SillyThings.LOGGER.info("real air -> dummy real")
					offItem.swapHand()
					entity.setItemInHand(InteractionHand.MAIN_HAND, offItem)
					entity.setItemInHand(InteractionHand.OFF_HAND, getDummyItem())
				}
			}
		}

		stack.set(
			ModDataComponents.ACTIVE,
			mainItem.matches(stack) && !mainItem.isDummy() && offItem.matches(stack) && offItem.isDummy()
		)
		otherStack?.set(ModDataComponents.HAND, stack.get(ModDataComponents.HAND))
	}

	fun ItemStack.isDummy() = this.get(ModDataComponents.DUMMY) ?: false
	fun ItemStack.matches(stack: ItemStack) = this.item == stack.item
	fun ItemStack.swapFireMode() {
		val fireMode = this.get(ModDataComponents.FIRE_MODE) ?: 0
		this.set(ModDataComponents.FIRE_MODE, 1 - fireMode)
	}

	fun ItemStack.swapHand() {
		val fireMode = this.get(ModDataComponents.HAND) ?: 0
		this.set(ModDataComponents.HAND, 1 - fireMode)
	}

	override fun shouldCauseReequipAnimation(oldStack: ItemStack, newStack: ItemStack, slotChanged: Boolean): Boolean =
		false
}