package improbable.behaviours.habitat

import improbable.habitat.HabitatInfoComponentWriter
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.util.SimulationSettings

class PopulationUpdateBehaviour(entity: Entity, world: World, logger: Logger, habitatInfoComponentWriter: HabitatInfoComponentWriter) extends EntityBehaviour {
  val increaseFactor = 1.2f

  override def onReady(): Unit = {
    world.timing.every(SimulationSettings.GameTickInterval) {
      habitatInfoComponentWriter.update.population(habitatInfoComponentWriter.population * increaseFactor).finishAndSend()
    }
  }
}
