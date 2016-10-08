package improbable.apps

import improbable.behaviours.habitat.PoacherResponse
import improbable.behaviours.{CityDemand, StepRequest}
import improbable.logging.Logger
import improbable.natures.{City, Habitat, Poacher}
import improbable.papi.EntityId
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldApp
import improbable.util.LatLonPosition

case object Step extends CustomMsg

case object RequestDemand extends CustomMsg


case class DeadElephants(elephants: Int) extends CustomMsg

case class TriggerPoacher(targetHabitatId: EntityId, targetDemand: Int) extends CustomMsg

class SimulationSpawner(appWorld: AppWorld, logger: Logger) extends WorldApp {

  var cities: Map[EntityId, LatLonPosition] = Map.empty
  var poachers: Map[EntityId, LatLonPosition] = Map.empty
  var habitats: Map[EntityId, LatLonPosition] = Map.empty

  var citiesRepliedSoFar: Map[EntityId, Int] = Map.empty

  spawnSimulation()
  listenToStepRequest()
  requestDemandsFromCities()
  listenToCityDemands()

  def spawnSimulation(): Unit = {
    spawnCities()
    spawnPoachers()
    spawnHabitats()
  }

  def spawnCities() = {
    SimulationSpawner.initialCities.foreach {
      city =>
        val cityId = appWorld.entities.spawnEntity(City(new LatLonPosition(city._2(0), city._2(1)), city._1, city._2(2).toInt))
        cities = cities + (cityId -> new LatLonPosition(city._2(0), city._2(1)))
    }
  }

  def spawnPoachers() = {
    SimulationSpawner.initialHabitats.foreach {
      poacher =>
        val poacherId = appWorld.entities.spawnEntity(Poacher(new LatLonPosition(poacher._2(0), poacher._2(1))))
        poachers = poachers + (poacherId -> new LatLonPosition(poacher._2(0), poacher._2(1)))
    }
  }

  def spawnHabitats() = {
    SimulationSpawner.initialHabitats.foreach {
      habitat =>
        val habitatId = appWorld.entities.spawnEntity(Habitat(new LatLonPosition(habitat._2(0), habitat._2(1)), habitat._1, habitat._2(2).toInt))
        habitats = habitats + (habitatId -> new LatLonPosition(habitat._2(0), habitat._2(1)))
    }
  }

  def listenToStepRequest() = {
    appWorld.messaging.onReceive {
      case StepRequest =>
        requestDemandsFromCities()
    }
  }

  def requestDemandsFromCities(): Unit = {
    cities.keys.foreach(cityId => appWorld.messaging.sendToEntity(cityId, RequestDemand))
  }

  def listenToCityDemands() = {
    appWorld.messaging.onReceive {
      case CityDemand(cityId, demand) =>
        citiesRepliedSoFar = citiesRepliedSoFar + (cityId -> demand)
        if (citiesRepliedSoFar.size == cities.size) {
          messagePoachers()
        }
    }
  }

  def messagePoachers(): Unit = {
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
}

object SimulationSpawner {
  // Format: Latitude, Longitude, population/demand

  val initialHabitats: Map[String, List[Float]] = Map(
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

  val initialCities: Map[String, List[Float]] = Map(
    "China" -> List(35.861660f, 104.195397f, 36f),
    "Philippines" -> List(12.879721f, 121.774017f, 34f),
    "Thailand" -> List(15.870032f, 100.992541f, 14f),
    "Hongkong" -> List(22.396428f, 114.109497f, 4f),
    "Vietnam" -> List(14.058324f, 108.277199f, 14f),
    "Malaysia" -> List(4.210484f, 101.975766f, 24f),
    "Indonesia" -> List(-0.789275f, 113.921327f, 5f)
  )
}
