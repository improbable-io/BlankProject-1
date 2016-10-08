package improbable.behaviours

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

  private var citiesRepliedSoFar: Map[EntityId, Int] = Map.empty
  private var cities: Set[EntityId] = Set.empty
  private var habitats: Set[EntityId] = Set.empty
  private var habitatsRepliedSoFar: List[Int] = List()

  override def onReady(): Unit = {
//    world.messaging.onReceive {
//      case DemandStatusResponse(cityId, demand) =>
//        citiesRepliedSoFar = citiesRepliedSoFar + (cityId -> demand)
//        if (gotAllResponses) {
//          calculateThing()
//        }
//
//      case SupplyStatusResponse(supply) =>
//        habitatsRepliedSoFar = habitatsRepliedSoFar.+:(supply)
//        if (gotAllResponses) {
//          calculateThing()
//        }
//    }
//
//    findCitiesAndHabitats()
//    step()
  }

  private def gotAllResponses: Boolean = citiesRepliedSoFar.size == cities.size && habitatsRepliedSoFar.size == habitats.size

  private def calculateThing() = {
    val totalDemand = citiesRepliedSoFar.values.sum
    val totalSupply = habitatsRepliedSoFar.sum
    val trade = math.min(totalDemand, totalSupply)

    world.messaging.sendToEntity(habitats.head, KillElephants(trade))
    world.messaging.sendToEntity(cities.head, CreateSupply(trade))
  }

  private def step(): Unit = {
    habitatsRepliedSoFar = List()
    citiesRepliedSoFar = Map.empty()

    cities.foreach(entityId => world.messaging.sendToEntity(entityId, RequestDemand))
    habitats.foreach(entityId => world.messaging.sendToEntity(entityId, RequestSupplyStatus))
  }

  private def findCitiesAndHabitats(): Unit = {
    cities = Set(
      world.entities.find(entity.position, 10000, Set(improbable.natures.Map.name))
        .sortBy(_.position.distanceTo(entity.position))
        .map(_.entityId)
        .head
    )

    habitats = Set(
      world.entities.find(entity.position, 10000, Set(improbable.natures.Map.name))
        .sortBy(_.position.distanceTo(entity.position))
        .map(_.entityId)
        .head
    )
  }
}
