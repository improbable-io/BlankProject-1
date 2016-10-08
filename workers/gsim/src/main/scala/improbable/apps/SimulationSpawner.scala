package improbable.apps

import improbable.logging.Logger
import improbable.natures.City
import improbable.papi.EntityId
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldApp

case class LatLonPosition(lat: Double, lon: Double)

case object StepRequest extends CustomMsg

case object Step extends CustomMsg


class SimulationSpawner(appWorld: AppWorld, logger: Logger) extends WorldApp {

  // 1
  appWorld.messaging.onReceive {
    case StepRequest =>
      step()
  }

  var cities: Map[EntityId, LatLonPosition] = Map.empty
  var poachers: Map[EntityId, LatLonPosition] = Map.empty

  spawnCities()
  spawnPoachers()

  private def spawnCities() = {
    val positions = List(LatLonPosition(52, 0))
    positions.foreach {
      position =>
        val cityId = appWorld.entities.spawnEntity(City(position))
        cities = cities + (cityId -> position)
    }
  }

  private def spawnPoachers() = {
    val positions = List(LatLonPosition(39.9, 116.4))
    positions.foreach {
      position =>
        val poacherId = appWorld.entities.spawnEntity(City(position))
        poachers = poachers + (poacherId -> position)
    }
  }

  private def step(): Unit = {
    cities.keys.foreach(cityId => appWorld.messaging.sendToEntity(cityId, Step))
    poachers.keys.foreach(poacherId => appWorld.messaging.sendToEntity(poacherId, Step))
  }

  //  private var citiesRepliedSoFar: Map[EntityId, Int] = Map.empty
  //  private var cities: Set[EntityId] = Set.empty
  //  private var habitats: Set[EntityId] = Set.empty
  //  private var habitatsRepliedSoFar: List[Int] = List()
  //
  //  override def onReady(): Unit = {
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
  //    //    findCitiesAndHabitats()
  //    //    step()
  //  }
  //
  //  private def gotAllResponses: Boolean = citiesRepliedSoFar.size == cities.size && habitatsRepliedSoFar.size == habitats.size
  //
  //  private def calculateThing() = {
  //    val totalDemand = citiesRepliedSoFar.values.sum
  //    val totalSupply = habitatsRepliedSoFar.sum
  //    val trade = math.min(totalDemand, totalSupply)
  //
  //    world.messaging.sendToEntity(habitats.head, KillElephants(trade))
  //    world.messaging.sendToEntity(cities.head, CreateSupply(trade))
  //  }
  //
  //  private def step(): Unit = {
  //    habitatsRepliedSoFar = List()
  //    citiesRepliedSoFar = Map.empty()
  //
  //    cities.foreach(entityId => world.messaging.sendToEntity(entityId, RequestDemand))
  //    habitats.foreach(entityId => world.messaging.sendToEntity(entityId, RequestSupplyStatus))
  //  }
  //
  //  private def findCitiesAndHabitats(): Unit = {
  //    cities = Set(
  //      world.entities.find(entity.position, 10000, Set(improbable.natures.Map.name))
  //        .sortBy(_.position.distanceTo(entity.position))
  //        .map(_.entityId)
  //        .head
  //    )
  //
  //    habitats = Set(
  //      world.entities.find(entity.position, 10000, Set(improbable.natures.Map.name))
  //        .sortBy(_.position.distanceTo(entity.position))
  //        .map(_.entityId)
  //        .head
  //    )
  //  }


}