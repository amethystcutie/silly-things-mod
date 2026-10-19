package gay.amethyst.silly_things

import gay.amethyst.silly_things.effects.ModEffects
import gay.amethyst.silly_things.entities.ModEntities
import gay.amethyst.silly_things.items.ModDataComponents
import gay.amethyst.silly_things.items.ModItems
import gay.amethyst.silly_things.items.client.ModItemProperties
import gay.amethyst.silly_things.player.animation.ModPlayerAnimations
import gay.amethyst.silly_things.sounds.ModSounds
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@Mod(SillyThings.ID)
@EventBusSubscriber
object SillyThings {
	const val ID = "silly_things"
	val LOGGER: Logger = LogManager.getLogger(ID)

	init {
		ModItems.ITEMS.register(MOD_BUS)
		ModDataComponents.DATA_COMPONENT_TYPES.register(MOD_BUS)
		ModSounds.SOUND_EVENTS.register(MOD_BUS)
		ModEffects.MOB_EFFECTS.register(MOD_BUS)
		ModEntities.ENTITY_TYPES.register(MOD_BUS)
	}

	@SubscribeEvent
	private fun onClientSetup(event: FMLClientSetupEvent) {
		event.enqueueWork { ModPlayerAnimations.registerAnimationLayers() }
		event.enqueueWork { ModItemProperties.addCustomItemProperties() }
	}

	@SubscribeEvent
	private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {

	}

	@SubscribeEvent
	fun onCommonSetup(event: FMLCommonSetupEvent) {

	}
	
	fun res(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(ID, path)
}
