package improbable.behaviours

import improbable.apps.PoacherMessage
import improbable.logging.Logger
import improbable.papi.EntityId
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg

case object RequestDemand extends CustomMsg

case class DemandStatusResponse(cityId: EntityId, demand: Int) extends CustomMsg

case object RequestSupplyStatus extends CustomMsg

case class SupplyStatusResponse(supply: Int) extends CustomMsg

case class KillElephants(trade: Int) extends CustomMsg

case class CreateSupply(trade: Int) extends CustomMsg

class PoacherTradingBehaviour(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {
  override def onReady(): Unit = {
    world.messaging.onReceive{
      case PoacherMessage(targetHabitatId, targetDemand) =>
        //
    }
  }
}
