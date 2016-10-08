package improbable.behaviours.habitat

import improbable.habitat.PopulationComponentWriter
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.util.SimulationSettings

class PopulationUpdateBehaviour(entity: Entity, world: World, logger: Logger, populationComponentWriter: PopulationComponentWriter) extends EntityBehaviour {
  val increaseFactor = 1.2f

  override def onReady(): Unit = {
    world.timing.every(SimulationSettings.GameTickInterval) {
      populationComponentWriter.update.population(populationComponentWriter.population * increaseFactor).finishAndSend()
    }
  }
}
