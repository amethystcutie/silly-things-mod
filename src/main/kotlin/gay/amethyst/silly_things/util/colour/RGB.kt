package gay.amethyst.silly_things.util.colour

import foundry.veil.api.client.color.Color
import foundry.veil.api.client.color.Colorc
import org.joml.Vector3d
import org.joml.Vector3f
import kotlin.math.roundToInt

data class RGB(val r: Int, val g: Int, val b: Int) {
	fun toHex(): Int = (r shl 16) or (g shl 8) or b

	fun toVec3f() = Vector3f(r.toFloat() / 255, g.toFloat() / 255, b.toFloat() / 255)
	fun toVec3d() = Vector3d(toVec3f())

	fun toVeil() = Color(this.toHex())

	fun mix(other: RGB, amount: Float) = mix(this, other, amount)
	fun lighten(amount: Float) = lighten(this, amount)
	fun darken(amount: Float) = darken(this, amount)
	companion object {
		fun fromHex(hex: Int) = RGB((hex shr 16) and 0xFF, (hex shr 8) and 0xFF, hex and 0xFF)
		fun fromHex(hex: String) = fromHex(hex.removePrefix("#").toInt(16))

		fun fromVec3d(vec3d: Vector3d) =
			RGB((vec3d.x * 255).roundToInt(), (vec3d.y * 255).roundToInt(), (vec3d.z * 255).roundToInt())

		fun fromVeil(veil: Colorc) = RGB(veil.rgb())

		fun mix(colourA: RGB, colourB: RGB, amount: Float) = RGB(
			(colourA.r + amount * (colourB.r - colourA.r)).roundToInt(),
			(colourA.g + amount * (colourB.g - colourA.g)).roundToInt(),
			(colourA.b + amount * (colourB.b - colourA.b)).roundToInt()
		)

		fun lighten(colour: RGB, amount: Float) = mix(colour, Colours.WHITE, amount)
		fun darken(colour: RGB, amount: Float) = mix(colour, Colours.BLACK, amount)

		operator fun invoke(hex: Int) = fromHex(hex)
		operator fun invoke(hex: String) = fromHex(hex)
		operator fun invoke(vec3d: Vector3d) = fromVec3d(vec3d)
		operator fun invoke(veil: Colorc) = fromVeil(veil)
	}
}
