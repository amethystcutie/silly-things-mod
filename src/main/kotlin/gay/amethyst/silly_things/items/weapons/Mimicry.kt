package gay.amethyst.silly_things.items.weapons

import gay.amethyst.silly_things.effects.ModEffects
import gay.amethyst.silly_things.sounds.ModSounds
import gay.amethyst.silly_things.sounds.weapons.ModWeaponSounds
import gay.amethyst.silly_things.util.text.ItemDescriptions
import gay.amethyst.silly_things.util.text.Rarity
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.HitResult
import kotlin.math.sqrt

class Mimicry(properties: Properties) : Item(
	properties.durability(22222).stacksTo(1).fireResistant()
) {
	override fun getName(stack: ItemStack): Component =
		Component.translatable("item.silly_things.mimicry").withColor(Rarity.MIMICRY.hexColour).withStyle(ChatFormatting.BOLD)

	override fun getDefaultAttributeModifiers(stack: ItemStack): ItemAttributeModifiers =
		ItemAttributeModifiers.builder().add(
			Attributes.ATTACK_DAMAGE,
			AttributeModifier(
				BASE_ATTACK_DAMAGE_ID, 20.0,
				AttributeModifier.Operation.ADD_VALUE
			),
			EquipmentSlotGroup.MAINHAND
		).add(
			Attributes.ATTACK_SPEED,
			AttributeModifier(
				BASE_ATTACK_SPEED_ID, -0.85,
				AttributeModifier.Operation.ADD_MULTIPLIED_BASE
			),
			EquipmentSlotGroup.MAINHAND
		).build()

	override fun getAttackDamageBonus(target: Entity, damage: Float, damageSource: DamageSource): Float {
		val attacker = damageSource.entity
		if (attacker !is Player) return 0f
		return if (attacker.health / attacker.maxHealth < 0.5) 12f else 0f
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		tooltipComponents.add(Component.literal("ALEPH").withColor(Rarity.LOB_ALEPH.hexColour))

		tooltipComponents.add(Component.translatable("item.silly_things.mimicry.desc_1").withColor(Rarity.MIMICRY.lightHex))
		tooltipComponents.add(Component.translatable("item.silly_things.mimicry.desc_2").withColor(Rarity.MIMICRY.lightHex))
		tooltipComponents.add(Component.translatable("item.silly_things.mimicry.desc_3").withColor(Rarity.MIMICRY.lightHex))
		tooltipComponents.add(ItemDescriptions.from(ItemDescriptions.Reference.LOBCORP))

		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag)
	}

	override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
		if (attacker !is Player) return false
		if (attacker.getAttackStrengthScale(0f) < 1f) return false

		val level = attacker.level()

		level.playSound(null, target.blockPosition(), ModWeaponSounds.MIMICRY_HIT.get(), SoundSource.PLAYERS, 1.8f, 0.1f)

		target.addEffect(MobEffectInstance(ModEffects.BLEEDING, 3 * 20, 2))
		attacker.heal(0.2f * (target.maxHealth - target.health))
		return true
	}

	override fun onEntitySwing(stack: ItemStack, entity: LivingEntity, hand: InteractionHand): Boolean {
		val level = entity.level()
		if (entity !is Player) return false

		if (getPlayerPOVHitResult(level, entity, ClipContext.Fluid.NONE).type == HitResult.Type.BLOCK) return false
		if (entity.getAttackStrengthScale(0f) < 1f) return false

		if (level is ServerLevel) level.playSound(null, entity.blockPosition(), SoundEvents.WIND_CHARGE_THROW,
												  SoundSource.PLAYERS, 1.8f, 0.4f)

		return super.onEntitySwing(stack, entity, hand)
	}

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
		val stack = player.getItemInHand(usedHand)
		val hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY)

		if (hit.type == HitResult.Type.BLOCK) return InteractionResultHolder.pass(stack)

		val look = player.lookAngle
		val strength = 1.8
		player.push(look.x * strength, 0.1, look.z * strength)
		level.playSound(null, player.blockPosition(), SoundEvents.WIND_CHARGE_THROW, SoundSource.PLAYERS, 1.8f, 0.3f)
		player.cooldowns.addCooldown(stack.item, 10)
		player.invulnerableTime = 18

		return InteractionResultHolder.pass(stack)
	}

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		super.inventoryTick(stack, level, entity, slotId, isSelected)

		if (entity !is Player) return

		if (!level.isClientSide and entity.cooldowns.isOnCooldown(stack.item)) {
			val nearby = level.getEntities(entity, entity.boundingBox.inflate(1.5)) {
				it is LivingEntity && it != entity
			}

			val strength = 0.2

			nearby.forEach { e ->
				val dx = e.x - entity.x
				val dz = e.z - entity.z
				val len = sqrt(dx * dx + dz * dz)
				if (len > 0) {
					e.push(dx / len * strength, strength, dz / len * strength)

					if (e.type.category == MobCategory.MONSTER || e.type == EntityType.WANDERING_TRADER) {
						e.hurt(e.damageSources().playerAttack(entity), 3f)
						(e as LivingEntity).addEffect(MobEffectInstance(ModEffects.BLEEDING, 2 * 20, 0))
					}
				}
			}

			(level as ServerLevel).sendParticles(
				ItemParticleOption(ParticleTypes.ITEM, ItemStack(Items.RED_STAINED_GLASS_PANE)),
				entity.x, entity.y, entity.z,
				20, 0.05, 1.05, 0.05, 0.0
			)
		}
	}

	override fun canAttackBlock(state: BlockState, level: Level, pos: BlockPos, player: Player): Boolean =
		!player.isCreative
}