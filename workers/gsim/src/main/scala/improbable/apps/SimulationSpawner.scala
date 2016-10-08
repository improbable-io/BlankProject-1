package improbable.apps

import improbable.behaviours.habitat.PoacherResponse
import improbable.logging.Logger
import improbable.natures.{City, ElephantRegion, Poacher}
import improbable.papi.EntityId
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldApp
import improbable.util.LatLonPosition

case object StepRequest extends CustomMsg

case object Step extends CustomMsg


case object RequestDemand extends CustomMsg

case class CityDemand(cityId: EntityId, demand: Int) extends CustomMsg

case object RequestSupplyStatus extends CustomMsg

case class SupplyStatusResponse(supply: Int) extends CustomMsg

case class KillElephants(trade: Int) extends CustomMsg

case class CreateSupply(trade: Int) extends CustomMsg

case class DeadElephants(elephants: Int) extends CustomMsg

case class TriggerPoacher(targetHabitatId: EntityId, targetDemand: Int) extends CustomMsg

class SimulationSpawner(appWorld: AppWorld, logger: Logger) extends WorldApp {

  appWorld.messaging.onReceive {
    case StepRequest =>
      requestDemands()
  }

  var cities: Map[EntityId, LatLonPosition] = Map.empty
  var poachers: Map[EntityId, LatLonPosition] = Map.empty
  var habitats: Map[EntityId, LatLonPosition] = Map.empty

  spawnCities()
  //spawnPoachers()
  spawnHabitats()

  private var citiesRepliedSoFar: Map[EntityId, Int] = Map.empty

  private def spawnCities() = {
    SimulationSpawner.defaultCities.foreach {
      city =>
        val cityId = appWorld.entities.spawnEntity(City(new LatLonPosition(city._2(0), city._2(1)), city._1, city._2(2)))
        cities = cities + (cityId -> new LatLonPosition(city._2(0), city._2(1)))
    }
  }

  private def spawnPoachers() = {
    val positions = List(new LatLonPosition(39.9, 116.4))
    positions.foreach {
      position =>
        val poacherId = appWorld.entities.spawnEntity(Poacher(position))
        poachers = poachers + (poacherId -> position)
    }
  }

  private def spawnHabitats() = {
    SimulationSpawner.defaultHabitats.foreach {
      habitat =>
        val habitatId = appWorld.entities.spawnEntity(ElephantRegion(new LatLonPosition(habitat._2(0), habitat._2(1)), habitat._1, habitat._2(2).toInt))
        habitats = habitats + (habitatId -> new LatLonPosition(habitat._2(0), habitat._2(1)))
    }
  }

  private def requestDemands(): Unit = {
    cities.keys.foreach(cityId => appWorld.messaging.sendToEntity(cityId, RequestDemand))
  }

  appWorld.messaging.onReceive {
    case CityDemand(cityId, demand) =>
      citiesRepliedSoFar = citiesRepliedSoFar + (cityId -> demand)
      if (citiesRepliedSoFar.size == cities.size) {
        messagePoachers()
      }
  }

  private def messagePoachers(): Unit = {
    val demandPerPoacher = Math.floor(citiesRepliedSoFar.values.sum / poachers.size).toInt

    poachers.foreach {
      case (poacherId, poacherPosition) =>
        val nearestHabitat = habitats.minBy { case (_, habitatPosition) => habitatPosition.distanceTo(poacherPosition) }._1
        appWorld.messaging.sendToEntity(poacherId, TriggerPoacher(nearestHabitat, demandPerPoacher))
    }
    citiesRepliedSoFar = Map.empty
  }

  appWorld.messaging.onReceive {
    case PoacherResponse(poacherId, killedElephants) =>
      val poacherPosition = poachers(poacherId)
      val nearestCity = cities.minBy { case (cityId, position) => position.distanceTo(poacherPosition) }
      appWorld.messaging.sendToEntity(nearestCity._1, DeadElephants(killedElephants))
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
  //  private def requestDemands(): Unit = {
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

object SimulationSpawner {
  // Format: Latitude, Longitude, population/demand

  val defaultHabitats: Map[String, List[Float]] = Map(
    "Ethiopia" -> List(9.145000f, 40.489673f, 3000f),
    "Uganda" -> List(1.373333f, 32.290275f, 10000f),
    "Kenya" -> List(-0.023559f, 37.906193f, 60000f),
    "Tanzania" -> List(-6.369028f, 34.888822f, 90000f),
    "Zambia" -> List(-13.133897f, 27.849332f, 60000f),
    "Zimbabwe" -> List(-19.015438f, 29.154857f, 110000f),
    "Mozambique" -> List(-18.665695f, 35.529562f, 30000f),
    "Angola" -> List(-11.202692f, 17.873887f, 10000f),
    "Botswana" -> List(-22.328474f, 24.684866f, 100000f),
    "South Africa" -> List(-30.559482f, 22.937506f, 50000f)
  )

  val defaultCities: Map[String, List[Float]] = Map(
    "China" -> List(35.861660f, 104.195397f, 36f),
    "Philippines" -> List(12.879721f, 121.774017f, 34f),
    "Thailand" -> List(15.870032f, 100.992541f, 14f),
    "Hongkong" -> List(22.396428f, 114.109497f, 4f),
    "Vietnam" -> List(14.058324f, 108.277199f, 14f),
    "Malaysia" -> List(4.210484f, 101.975766f, 24f),
    "Indonesia" -> List(-0.789275f, 113.921327f, 5f)
  )
}
