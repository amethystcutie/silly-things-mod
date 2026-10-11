package gay.amethyst.silly_things.util

import net.minecraft.util.RandomSource

fun RandomSource.floatRange(min: Float, max: Float) = this.nextFloat() * (max - min) + min
fun RandomSource.doubleRange(min: Double, max: Double) = this.nextDouble() * (max - min) + min
