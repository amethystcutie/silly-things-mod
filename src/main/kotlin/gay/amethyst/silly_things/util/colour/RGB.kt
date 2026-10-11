package gay.amethyst.silly_things.util.colour

import org.joml.Vector3d
import org.joml.Vector3f

data class RGB(val r: Int, val g: Int, val b: Int) {
	fun toHex(): Int = (r shl 16) or (g shl 8) or b

	fun toVec3f() = Vector3f(r.toFloat() / 255, g.toFloat() / 255, b.toFloat() / 255)
	fun toVec3d() = Vector3d(toVec3f())

	companion object {
		fun fromHex(hex: Int) = RGB((hex shr 16) and 0xFF, (hex shr 8) and 0xFF, hex and 0xFF)
		fun fromVec3d(vec3d: Vector3d) = RGB((vec3d.x * 255).toInt(), (vec3d.y * 255).toInt(), (vec3d.z * 255).toInt())
	}
}
