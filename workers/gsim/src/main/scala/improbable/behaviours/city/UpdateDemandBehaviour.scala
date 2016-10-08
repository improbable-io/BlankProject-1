package improbable.behaviours.city

import improbable.apps.{DeadElephants, Expenditure, RequestDemand, SimulationSpawner}
import improbable.city.{CityInfoComponent, CityInfoComponentWriter}
import improbable.logging.Logger
import improbable.papi._
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldAppDescriptor

case class CityDemand(cityId: EntityId, demand: Int) extends CustomMsg

class UpdateDemandBehaviour(entity: Entity, world: World, logger: Logger, cityInfoComponentWriter: CityInfoComponentWriter) extends EntityBehaviour {

  private val cityInfoComponent = entity.watch[CityInfoComponent]

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case DeadElephants(elephants) =>
      // Consume the elephants.
      case RequestDemand =>
        world.messaging.sendToApp(WorldAppDescriptor.forClass[SimulationSpawner].name, CityDemand(entity.entityId, cityInfoComponent.demand.get))
      case Expenditure(amount) =>
        reduceDemand(amount)
    }

  }

  def reduceDemand(amount: Int): Unit = {
    val newDemand = cityInfoComponentWriter.demand - amount
    cityInfoComponentWriter.update.demand(newDemand).finishAndSend()
  }
}
