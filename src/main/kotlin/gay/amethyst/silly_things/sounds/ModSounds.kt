package gay.amethyst.silly_things.sounds

import gay.amethyst.silly_things.SillyThings
import gay.amethyst.silly_things.sounds.weapons.ModWeaponSounds
import gay.amethyst.silly_things.util.touch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModSounds {
	val SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SillyThings.ID)

	init {
		ModWeaponSounds.touch()
	}

	fun registerSoundEvent(name: String): Supplier<SoundEvent> = SOUND_EVENTS.register(name, Supplier {
		SoundEvent.createVariableRangeEvent(
			SillyThings.res(name)
		)
	})
}