package gay.amethyst.silly_things.effects

import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.Blocks

class BleedingEffect : MobEffect(
	MobEffectCategory.HARMFUL, 0x770000
) {
	val particle = BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState())

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		livingEntity.hurt(livingEntity.damageSources().generic(), amplifier + 1f)
		val pos = livingEntity.position()
		val level = livingEntity.level()
		if (!level.isClientSide()) {
			val count = 5 + 3 * amplifier
			(level as ServerLevel).sendParticles(particle, pos.x, pos.y + 1, pos.z, count, 0.0, 0.0, 0.0, 1.0)
		}

		return true
	}

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		val interval = (15 - (amplifier - 1) * 2).coerceAtLeast(3)
		return duration % interval == 0
	}

	override fun createParticleOptions(effect: MobEffectInstance): ParticleOptions = particle
}