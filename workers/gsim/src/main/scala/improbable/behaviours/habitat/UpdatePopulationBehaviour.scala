package improbable.behaviours.habitat

import improbable.behaviours.PoachRequest
import improbable.habitat.HabitatInfoComponentWriter
import improbable.logging.Logger
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class PoachResponse(numberOfKilledElephants: Int) extends CustomMsg

case class PoacherResponse(poacherId: EntityId, numberOfKilledElephants: Int) extends CustomMsg

class UpdatePopulationBehaviour(entity: Entity, world: World, logger: Logger, habitatInfoComponentWriter: HabitatInfoComponentWriter) extends EntityBehaviour {
  override def onReady(): Unit = {
    world.messaging.onReceive {
      case PoachRequest(poacherId, demand) =>
        killElephants(poacherId, demand)
    }
  }

  private def killElephants(poacherId: EntityId, demand: Int): Unit = {
    val currentElephants = habitatInfoComponentWriter.population
    val killedElephants = math.max(currentElephants, demand)
    world.messaging.sendToEntity(poacherId, PoachResponse(killedElephants))
    val remainingElephants = habitatInfoComponentWriter.population - killedElephants
    habitatInfoComponentWriter.update.population(remainingElephants).finishAndSend()
  }
}
