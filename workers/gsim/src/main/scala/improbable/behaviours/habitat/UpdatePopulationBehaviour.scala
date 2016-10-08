package improbable.behaviours.habitat

import improbable.apps.Expenditure
import improbable.behaviours.poacher.PoachRequest
import improbable.habitat.HabitatInfoComponentWriter
import improbable.logging.Logger
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case class HabitatResponse(killedElephants: Int) extends CustomMsg

class UpdatePopulationBehaviour(entity: Entity, world: World, logger: Logger, habitatInfoComponentWriter: HabitatInfoComponentWriter) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case msg@PoachRequest(poacherId, poacherDemand) =>
        logger.info("poach request received " + msg)
        killElephants(poacherId, poacherDemand)
      case Expenditure(amount) =>
        increasePopulation(amount)
    }
  }

  def increasePopulation(amount: Int): Unit = {
    val newPopulation = habitatInfoComponentWriter.population + amount
    habitatInfoComponentWriter.update.population(newPopulation).finishAndSend()
  }

  def killElephants(poacherId: EntityId, poacherDemand: Int): Unit = {
    val currentElephants = habitatInfoComponentWriter.population
    val killedElephants = math.max(currentElephants, poacherDemand)
    world.messaging.sendToEntity(poacherId, HabitatResponse(killedElephants))
    val remainingElephants = habitatInfoComponentWriter.population - killedElephants
    habitatInfoComponentWriter.update.population(remainingElephants).finishAndSend()
  }
}
