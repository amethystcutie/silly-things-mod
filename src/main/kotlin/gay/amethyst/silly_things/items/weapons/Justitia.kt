package gay.amethyst.silly_things.items.weapons

import gay.amethyst.silly_things.items.CooldownHandler
import gay.amethyst.silly_things.util.text.ItemDescriptions
import gay.amethyst.silly_things.util.Delay
import gay.amethyst.silly_things.util.floatRange
import gay.amethyst.silly_things.util.text.Rarity
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3f

class Justitia(properties: Properties) : Item(
	properties.durability(15000).stacksTo(1).fireResistant()
) {
	init {
		CooldownHandler.dontAttackOnCooldown(this)
		CooldownHandler.tagOnCrit(this)
	}

	override fun getName(stack: ItemStack): Component =
		Component.translatable("item.silly_things.justitia").withColor(Rarity.LOB_PALE.hexColour).withStyle(ChatFormatting.BOLD)

	override fun getDefaultAttributeModifiers(stack: ItemStack): ItemAttributeModifiers = SwordItem.createAttributes(
		Tiers.WOOD, 0, -3f
	)

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		tooltipComponents.add(Component.literal("ALEPH").withColor(Rarity.LOB_ALEPH.hexColour))

		tooltipComponents.add(Component.translatable("item.silly_things.justitia.desc_1").withColor(Rarity.LOB_PALE.hexColour))
		tooltipComponents.add(Component.translatable("item.silly_things.justitia.desc_2").withColor(Rarity.LOB_PALE.hexColour))
		tooltipComponents.add(ItemDescriptions.from(ItemDescriptions.Reference.LOBCORP))

		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
	}

	override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
		if (attacker !is Player) return false

		if (attacker.getAttackStrengthScale(0f) < 1f) {
			attacker.hurt(
				attacker.damageSources().playerAttack(attacker),
				3f * (1f - attacker.getAttackStrengthScale(0f))
			)
			return false
		}

		val level = attacker.level()

		val source = target.damageSources().playerAttack(attacker)

		attacker.cooldowns.addCooldown(this, 30)

		level.playSound(null, target.blockPosition(), SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 0.4f, 0.5f)

		repeat(5) { i ->
			Delay.schedule(i * 2) {
				val specialChance = if (CooldownHandler.hasCritTag(stack)) 0.4 else 0.2

				if (level.random.nextDouble() < specialChance) {
					target.hurt(source, level.random.floatRange(20f, 40f))

					level.playSound(null, target.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.PLAYERS)

					(level as? ServerLevel)?.sendParticles(
						DustParticleOptions(Rarity.LOB_PALE.colour.toVec3f(), 2f),
						target.x,
						target.y,
						target.z,
						8,
						0.4,
						0.4,
						0.4,
						0.5
					)
				} else {
					target.hurt(source, level.random.floatRange(2f, 4f))
				}

				CooldownHandler.consumeCritTag(stack)
			}
		}

		return true
	}

	override fun canAttackBlock(state: BlockState, level: Level, pos: BlockPos, player: Player): Boolean =
		!player.isCreative
}