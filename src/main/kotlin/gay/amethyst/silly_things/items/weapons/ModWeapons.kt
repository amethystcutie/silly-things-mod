package gay.amethyst.silly_things.items.weapons

import gay.amethyst.silly_things.items.ModItems
import gay.amethyst.silly_things.items.ModItems.addToTab
import net.minecraft.world.item.CreativeModeTabs

object ModWeapons {
	val JUSTITIA = ModItems.registerItem("justitia", ::Justitia).addToTab(CreativeModeTabs.COMBAT)
	val MIMICRY = ModItems.registerItem("mimicry", ::Mimicry).addToTab(CreativeModeTabs.COMBAT)

	val SOLEMN_LAMENT = ModItems.registerItem("solemn_lament", ::SolemnLament).addToTab(CreativeModeTabs.COMBAT)
}