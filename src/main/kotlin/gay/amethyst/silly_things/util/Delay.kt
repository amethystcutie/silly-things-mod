package gay.amethyst.silly_things.util

import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.ServerTickEvent

@EventBusSubscriber
object Delay {
	private var tick: Long = 0L
	private val scheduled = mutableListOf<ScheduledAction>()

	data class ScheduledAction(
		val time: Long,
		val action: () -> Unit
	)

	fun schedule(delay: Int, action: () -> Unit) {
		val time = tick + delay
		scheduled.add(ScheduledAction(time, action))
	}

	@SubscribeEvent
	fun tick(event: ServerTickEvent.Post) {
		tick++

		val iter = scheduled.iterator()
		while (iter.hasNext()) {
			val action = iter.next()
			if (tick >= action.time) {
				action.action()
				iter.remove()
			}
		}
	}
}