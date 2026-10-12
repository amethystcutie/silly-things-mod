package gay.amethyst.silly_things.effects

import gay.amethyst.silly_things.SillyThings
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.neoforged.neoforge.registries.DeferredRegister

object ModEffects {
	val MOB_EFFECTS: DeferredRegister<MobEffect> = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, SillyThings.ID)

	val BLEEDING: Holder<MobEffect> = MOB_EFFECTS.register("bleeding", ::BleedingEffect)
}