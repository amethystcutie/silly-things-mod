package gay.amethyst.silly_things.items.client

import gay.amethyst.silly_things.SillyThings
import gay.amethyst.silly_things.items.ModDataComponents
import gay.amethyst.silly_things.items.weapons.ModWeapons
import net.minecraft.client.renderer.item.ItemProperties

object ModItemProperties {
	fun addCustomItemProperties() {
		ItemProperties.register(ModWeapons.SOLEMN_LAMENT.get(), SillyThings.res("gun")) {
			stack, level, entity, seed ->
			if (stack.get(ModDataComponents.ACTIVE) == true) {
				if ((stack.get(ModDataComponents.HAND) == 1) != (stack.get(ModDataComponents.DUMMY) == true)) {
					.5f
				} else 1f
			} else 0f
		}
	}
}