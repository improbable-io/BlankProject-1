package improbable.behaviours

import improbable.apps.{DeadElephants, RequestDemand, SimulationSpawner}
import improbable.city.CityInfoComponent
import improbable.logging.Logger
import improbable.papi._
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldAppDescriptor

case class CityDemand(cityId: EntityId, demand: Int) extends CustomMsg

class CityBehaviour(entity: Entity, world: World, logger: Logger) extends EntityBehaviour {

  private val cityInfoComponent = entity.watch[CityInfoComponent]

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case DeadElephants(elephants) =>
      // TODO (apu)
      case RequestDemand =>
        world.messaging.sendToApp(WorldAppDescriptor.forClass[SimulationSpawner].name, CityDemand(entity.entityId, cityInfoComponent.demand.get))
    }

  }
}
