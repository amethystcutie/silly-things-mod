package gay.amethyst.silly_things

import gay.amethyst.silly_things.effects.ModEffects
import gay.amethyst.silly_things.items.ModItems
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
		ModEffects.MOB_EFFECTS.register(MOD_BUS)
	}

	@SubscribeEvent
	private fun onClientSetup(event: FMLClientSetupEvent) {

	}

	@SubscribeEvent
	private fun onServerSetup(event: FMLDedicatedServerSetupEvent) {

	}

	@SubscribeEvent
	fun onCommonSetup(event: FMLCommonSetupEvent) {

	}
}
