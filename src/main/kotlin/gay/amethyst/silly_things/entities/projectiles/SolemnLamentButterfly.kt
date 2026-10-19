package gay.amethyst.silly_things.entities.projectiles

import foundry.veil.api.client.render.VeilRenderSystem
import gay.amethyst.silly_things.SillyThings
import gay.amethyst.silly_things.util.Delay
import gay.amethyst.silly_things.util.colour.RGB
import gay.amethyst.silly_things.util.floatRange
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult

class SolemnLamentButterfly(
	entityType: EntityType<out AbstractHurtingProjectile>,
	level: Level,
	val owner: LivingEntity? = null,
	val firedFromWeapon: ItemStack? = null,
	mode: Int = 0,
) : AbstractHurtingProjectile(entityType, level) {
	init {
		this.entityData.set(DATA_MODE, mode)
	}

	companion object {
		val DATA_MODE: EntityDataAccessor<Int> = SynchedEntityData.defineId(
			SolemnLamentButterfly::class.java, EntityDataSerializers.INT
		)
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		builder.define(DATA_MODE, 0)
	}

	override fun onHitBlock(result: BlockHitResult) {
		if (!this.level().isClientSide) {
			discard()
		}
	}

	constructor(
		level: Level, owner: LivingEntity?, firedFromWeapon: ItemStack?, mode: Int
	) : this(ModProjectiles.SOLEMN_LAMENT_BUTTERFLY.get(), level, owner, firedFromWeapon, mode)

	override fun shouldBurn() = false
	override fun getTrailParticle(): ParticleOptions? = null
//	if (entityData.get(DATA_MODE) == 0) {
//		DustParticleOptions(RGB(0xededeb).toVec3f(), 4f)
//	} else {
//		DustParticleOptions(RGB(0x431970).toVec3f(), 4f)
//	}

	override fun onHitEntity(result: EntityHitResult) {
		result.entity.hurt(result.entity.damageSources().generic(), result.entity.level().random.floatRange(2f, 6f))
	}

	override fun onAddedToLevel() {
		if (!this.level().isClientSide) {
			val colour = if (entityData.get(DATA_MODE) == 0) "white" else "black"

			try {
				val manager = VeilRenderSystem.renderer().particleManager
				val emitter = manager.createEmitter(SillyThings.res("butterfly_$colour"))
				emitter?.attachedEntity = this
				emitter?.let { manager.addParticleSystem(it) }
			} catch (e: Exception) {
				SillyThings.LOGGER.warn(e)
			}
		}
		Delay.schedule(12) {
			discard()
		}
	}
}