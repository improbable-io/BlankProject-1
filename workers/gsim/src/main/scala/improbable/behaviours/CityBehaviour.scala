package improbable.behaviours

import improbable.apps.DeadElephants
import improbable.logging.Logger
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World

class CityBehaviour(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case DeadElephants(elephants) =>
        //TODO
    }

  }
}
