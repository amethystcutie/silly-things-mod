package gay.amethyst.silly_things.items

import com.mojang.serialization.Codec
import gay.amethyst.silly_things.SillyThings
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier
import java.util.function.UnaryOperator

object ModDataComponents {
	val DATA_COMPONENT_TYPES: DeferredRegister.DataComponents = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,
																									  SillyThings.ID)

	val FIRE_MODE = register("fire_mode", Codec.INT)
	val ACTIVE = register("active", Codec.BOOL)
	val HAND = register("hand", Codec.INT)
	val DUMMY = register("dummy", Codec.BOOL)

	fun <T> register(
		name: String,
		builderOperator: UnaryOperator<DataComponentType.Builder<T>>
	): DeferredHolder<DataComponentType<*>, DataComponentType<T>> = DATA_COMPONENT_TYPES.registerComponentType(
		name
	) { builderOperator.apply(DataComponentType.builder()) }

	fun <T> register(name: String, codec: Codec<T>): DeferredHolder<DataComponentType<*>, DataComponentType<T>> = register(name) {
		builder -> builder.persistent(codec)
	}
}