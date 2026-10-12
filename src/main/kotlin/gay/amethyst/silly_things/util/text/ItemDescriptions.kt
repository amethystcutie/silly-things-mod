package gay.amethyst.silly_things.util.text

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

object ItemDescriptions {
	enum class Reference(val component: Component) {
		LOBCORP(Component.literal("Lobotomy ").withColor(0xFFFEA3).append(Component.literal("Corporation").withColor(0xFC3A3A)))
	}

	fun from(source: Reference): MutableComponent = Component.literal("from ").withStyle(ChatFormatting.GRAY)
		.append(source.component)
}