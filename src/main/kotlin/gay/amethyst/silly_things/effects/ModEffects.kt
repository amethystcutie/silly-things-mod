package gay.amethyst.silly_things.effects

import gay.amethyst.silly_things.SillyThings
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModEffects {
	val MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, SillyThings.ID)

	val BLEEDING = MOB_EFFECTS.register("bleeding", ::BleedingEffect)
}