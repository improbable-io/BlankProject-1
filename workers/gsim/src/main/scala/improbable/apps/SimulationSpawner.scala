package improbable.apps

import improbable.logging.Logger
import improbable.natures.City
import improbable.papi.EntityId
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldApp
import improbable.util.LatLonPosition

case object StepRequest extends CustomMsg

case object Step extends CustomMsg


case object RequestDemand extends CustomMsg

case class DemandStatusResponse(cityId: EntityId, demand: Int) extends CustomMsg

case object RequestSupplyStatus extends CustomMsg

case class SupplyStatusResponse(supply: Int) extends CustomMsg

case class KillElephants(trade: Int) extends CustomMsg

case class CreateSupply(trade: Int) extends CustomMsg


case class PoacherMessage(targetHabitatId: EntityId, targetDemand: Int) extends CustomMsg

class SimulationSpawner(appWorld: AppWorld, logger: Logger) extends WorldApp {

  // 1
  appWorld.messaging.onReceive {
    case StepRequest =>
      step()
  }

  var cities: Map[EntityId, LatLonPosition] = Map.empty
  var poachers: Map[EntityId, LatLonPosition] = Map.empty
  var habitats: Map[EntityId, LatLonPosition] = Map.empty

  spawnCities()
  spawnPoachers()
  spawnHabitats()
  private var citiesRepliedSoFar: Map[EntityId, Int] = Map.empty

  private def spawnCities() = {
    val positions = List(new LatLonPosition(52, 0))
    positions.foreach {
      position =>
        val cityId = appWorld.entities.spawnEntity(City(position))
        cities = cities + (cityId -> position)
    }
  }

  private def spawnPoachers() = {
    val positions = List(new LatLonPosition(39.9, 116.4))
    positions.foreach {
      position =>
        val poacherId = appWorld.entities.spawnEntity(City(position))
        poachers = poachers + (poacherId -> position)
    }
  }

  private def spawnHabitats() = {
    val positions = List(new LatLonPosition(39.9, 116.4))
    positions.foreach {
      position =>
        val habitatId = appWorld.entities.spawnEntity(City(position))
        habitats = habitats + (habitatId -> position)
    }
  }


  private def step(): Unit = {
    cities.keys.foreach(cityId => appWorld.messaging.sendToEntity(cityId, RequestDemand))
  }

  appWorld.messaging.onReceive {
    case DemandStatusResponse(cityId, demand) =>
      citiesRepliedSoFar = citiesRepliedSoFar + (cityId -> demand)
      if (citiesRepliedSoFar.size == cities.size) {
        messagePoachers()
      }
  }

  private def messagePoachers(): Unit = {
    val demandPerPoacher = citiesRepliedSoFar.values.sum / poachers.size

    poachers.foreach {
      case (poacherId, poacherPosition) =>
        val nearestHabitat = habitats.minBy { case (_, habitatPosition) => habitatPosition.distanceTo(poacherPosition) }._1
        appWorld.messaging.sendToEntity(poacherId, PoacherMessage(nearestHabitat, demandPerPoacher))
    }
    citiesRepliedSoFar = Map.empty
  }

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