package gay.amethyst.silly_things.util.colour

class Gradient(vararg colours: RGB) {
	private val coloursList = colours.toList()

	constructor(vararg colours: Int) : this(
		*colours.map { RGB.fromHex(it) }.toTypedArray()
	)

	fun get(t: Double): RGB {
		val clamped = t.coerceIn(0.0, 1.0)
		val scaled = clamped * (coloursList.size - 1)
		val idx = scaled.toInt()
		val frac = scaled - idx
		val c1 = coloursList[idx]
		val c2 = coloursList.getOrElse(idx + 1) { coloursList[0] }
		return RGB(
			(c1.r + ((c2.r - c1.r) * frac)).toInt(),
			(c1.g + ((c2.g - c1.g) * frac)).toInt(),
			(c1.b + ((c2.b - c1.b) * frac)).toInt()
		)
	}
}