package gay.amethyst.silly_things.util.text

import gay.amethyst.silly_things.util.colour.Gradient
import gay.amethyst.silly_things.util.colour.RGB
import kotlin.math.sin

enum class Rarity(val id: Int, val colourLookup: () -> RGB) {
	MIMICRY(0, { Gradient(0xe63420, 0xe67c20).get(animTime()) }),
	JUSTITIA(1, { Gradient(0x3EC3B5, 0x7bedc1).get(animTime()) }),
	SOLEMN_LAMENT(2, { Gradient(0x431970, 0xededeb).get(animTime()) }),


	LOB_ZAYIN(4, { RGB.fromHex(0x22F900) }),
	LOB_TETH(5, { RGB.fromHex(0x1AA1FF) }),
	LOB_HE(6, { RGB.fromHex(0xFFF900) }),
	LOB_WAW(7, { RGB.fromHex(0x7A2FF2) }),
	LOB_ALEPH(8, { RGB.fromHex(0xFF0000) });

	val colour get() = colourLookup()
	val hexColour get() = colour.toHex()
	val light get() = colourLookup().lighten(0.2f)
	val lightHex get() = light.toHex()

	companion object {
		private fun time() = (System.currentTimeMillis() % 2000) / 2000.0
		private fun animTime() = (sin(time() * 2 * Math.PI) + 1) / 2
	}
}