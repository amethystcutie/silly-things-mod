package gay.amethyst.silly_things.entities

import gay.amethyst.silly_things.SillyThings
import gay.amethyst.silly_things.entities.projectiles.ModProjectiles
import gay.amethyst.silly_things.util.touch
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.neoforge.registries.DeferredRegister

object ModEntities {
	val ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SillyThings.ID)

	init {
		ModProjectiles.touch()
	}
}