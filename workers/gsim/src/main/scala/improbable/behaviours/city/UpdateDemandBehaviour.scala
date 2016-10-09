package improbable.behaviours.city

import improbable.apps._
import improbable.city.{ArrowData, CityInfoComponent, CityInfoComponentWriter}
import improbable.logging.Logger
import improbable.papi._
import improbable.papi.entity.{Entity, EntityBehaviour}
import improbable.papi.world.World
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldAppDescriptor
import improbable.util.{GameSettings, LatLonPosition}

case class CityDemand(cityId: EntityId, demand: Int) extends CustomMsg

class UpdateDemandBehaviour(entity: Entity, world: World, logger: Logger, cityInfoComponentWriter: CityInfoComponentWriter) extends EntityBehaviour {

  private val cityInfoComponent = entity.watch[CityInfoComponent]

  override def onReady(): Unit = {
    world.messaging.onReceive {
      case msg@Trade(poacherPosition, numberOfElephants) =>
        logger.info("trade received: " + "(" + msg.poacherPosition.getLat + ", " + msg.poacherPosition.getLon + ") " + msg.numberOfElephants)
        updateArrowData(poacherPosition, numberOfElephants)
      case RequestDemand =>
        world.messaging.sendToApp(WorldAppDescriptor.forClass[SimulationSpawner].name, CityDemand(entity.entityId, cityInfoComponent.demand.get))
      case Expenditure(amount) =>
        reduceDemand(amount)
      case Step =>
        randomiseDemand()
    }
  }

  def updateArrowData(poacherPosition: LatLonPosition, numberOfElephants: Int): Unit = {
    cityInfoComponentWriter.update.arrowData(ArrowData(poacherPosition.convertToVector(), numberOfElephants)).finishAndSend()
  }

  def randomiseDemand() = {
    val newDemand = (cityInfoComponentWriter.demand + scala.util.Random.nextGaussian() * GameSettings.demandRandomisationSD * cityInfoComponentWriter.demand).toInt
    cityInfoComponentWriter.update.demand(newDemand).finishAndSend()
  }

  def reduceDemand(amount: Int): Unit = {
    val newDemand = math.min(cityInfoComponentWriter.demand - amount, 0)
    cityInfoComponentWriter.update.demand(newDemand).finishAndSend()
  }
}
