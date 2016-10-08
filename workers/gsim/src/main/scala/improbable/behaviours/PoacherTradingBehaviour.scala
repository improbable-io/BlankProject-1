package improbable.behaviours

import improbable.apps.{SimulationSpawner, TriggerPoacher}
import improbable.behaviours.habitat.{PoachResponse, PoacherResponse}
import improbable.logging.Logger
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldAppDescriptor

case class PoachRequest(poacherId: EntityId, demand: Int) extends CustomMsg

class PoacherTradingBehaviour(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {
  override def onReady(): Unit = {

    world.messaging.onReceive {
      case TriggerPoacher(habitatId, targetDemand) =>
        poachFromHabitat(habitatId, targetDemand)
      case PoachResponse(killedElephants) =>
        world.messaging.sendToApp(
          WorldAppDescriptor.forClass[SimulationSpawner].name, PoacherResponse(entity.entityId, killedElephants)
        )
    }
  }

  def poachFromHabitat(habitatId: EntityId, targetDemand: Int): Unit = {
    val maximumDemand = 50
    val maximumPossibleDemand = Math.min(targetDemand, maximumDemand)
    world.messaging.sendToEntity(habitatId, PoachRequest(entity.entityId, maximumPossibleDemand))
  }
}
