package gay.amethyst.silly_things.entities

import gay.amethyst.silly_things.SillyThings
import gay.amethyst.silly_things.entities.projectiles.ModProjectiles
import gay.amethyst.silly_things.util.touch
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

@EventBusSubscriber(Dist.CLIENT)
object ModEntities {
	val ENTITY_TYPES: DeferredRegister<EntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SillyThings.ID)

	private val ENTITY_RENDERERS = mutableMapOf<Supplier<out EntityType<out Entity>>, (EntityRendererProvider.Context) -> EntityRenderer<Entity>>()

	init {
		ModProjectiles.touch()
	}

	fun <T : Entity> registerEntityType(
		name: String,
		typeBuilder: EntityType.Builder<T>,
		renderer: (EntityRendererProvider.Context) -> EntityRenderer<Entity>
	): Supplier<EntityType<T>> = ENTITY_TYPES.register(name) { location -> typeBuilder.build(location.path) }.also {
		ENTITY_RENDERERS[it] = renderer
	}

	@SubscribeEvent
	fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
		ENTITY_RENDERERS.forEach { (type, renderer) ->
			event.registerEntityRenderer(type.get(), renderer)
		}
	}
}