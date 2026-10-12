package gay.amethyst.silly_things.entities.projectiles

import gay.amethyst.silly_things.entities.ModEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

object ModProjectiles {
	val SOLEMN_LAMENT_BUTTERFLY = ModEntities.ENTITY_TYPES.register(
		"solemn_lament_butterfly"
	) { location ->
		EntityType.Builder.of(
			::SolemnLamentButterfly, MobCategory.MISC
		).build(location.toString())
	}
}