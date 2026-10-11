package gay.amethyst.silly_things.datagen

import gay.amethyst.silly_things.SillyThings
import gay.amethyst.silly_things.items.weapons.ModWeapons
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModItemModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) : ItemModelProvider(
	output, SillyThings.ID, existingFileHelper
) {
	override fun registerModels() {
		largeHandheldItem(ModWeapons.JUSTITIA.get())
		largeHandheldItem(ModWeapons.MIMICRY.get())
	}

	fun largeHandheldItem(item: Item): ItemModelBuilder {
		val itemLocation = BuiltInRegistries.ITEM.getKey(item)

		return this.getBuilder(itemLocation.toString()).parent(
			ModelFile.UncheckedModelFile(
				ResourceLocation.fromNamespaceAndPath(
					SillyThings.ID, "item/handheld_large"
				)
			)
		).texture("layer0", ResourceLocation.fromNamespaceAndPath(itemLocation.namespace, "item/${itemLocation.path}"))
	}
}