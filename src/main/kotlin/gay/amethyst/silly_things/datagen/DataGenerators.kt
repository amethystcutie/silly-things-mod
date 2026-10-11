package gay.amethyst.silly_things.datagen

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber
object DataGenerators {
	@SubscribeEvent
	fun gatherData(event: GatherDataEvent) {
		val generator = event.generator
		val packOutput = generator.packOutput
		val existingFileHelper = event.existingFileHelper
		val lookupProvider = event.lookupProvider

		generator.addProvider(event.includeClient(), ModItemModelProvider(packOutput, existingFileHelper))
	}
}