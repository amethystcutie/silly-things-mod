package gay.amethyst.silly_things.items

import gay.amethyst.silly_things.SillyThings
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier
import gay.amethyst.silly_things.items.weapons.ModWeapons
import gay.amethyst.silly_things.util.touch
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

@EventBusSubscriber
object ModItems {
	// Registries

	val ITEMS: DeferredRegister.Items = DeferredRegister.createItems(SillyThings.ID)
	val tabItems = mutableMapOf<Supplier<out Item>, ResourceKey<CreativeModeTab>>()

	// Helper functions

	fun registerItem(id: String, init: (Item.Properties) -> Item, props: Item.Properties): Supplier<Item> =
		ITEMS.registerItem(id, init, props)
	fun registerItem(id: String, props: Item.Properties) =
		registerItem(id, ::Item, props)
	fun registerItem(id: String, init: (Item.Properties) -> Item) =
		registerItem(id, init, Item.Properties())
	fun registerItem(id: String) = registerItem(id, Item.Properties())

	fun <T : Item> Supplier<T>.addToTab(tab: ResourceKey<CreativeModeTab>) = this.also {
		tabItems[this] = tab
	}

	init {
		ModWeapons.touch()
	}

	@SubscribeEvent
	fun buildContents(event: BuildCreativeModeTabContentsEvent) {
		for ((supplier, tab) in tabItems) {
			SillyThings.LOGGER.info(event.tabKey)
			SillyThings.LOGGER.info(tab)

			if (event.tabKey == tab) {
				event.accept(supplier.get())
			}
		}
	}
}