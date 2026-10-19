package gay.amethyst.silly_things.entities.projectiles

import gay.amethyst.silly_things.entities.ModEntities
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object ModProjectiles {
	val SOLEMN_LAMENT_BUTTERFLY = ModEntities.registerEntityType(
		"solemn_lament_butterfly",
		EntityType.Builder.of(
			::SolemnLamentButterfly, MobCategory.MISC
		).sized(3f, 1.5f), ::NoopRenderer)
}