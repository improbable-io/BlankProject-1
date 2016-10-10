package improbable.apps

import improbable.behaviours.city.CityDemand
import improbable.behaviours.player.StepRequest
import improbable.behaviours.poacher.{PoacherDead, PoacherResponse}
import improbable.logging.Logger
import improbable.natures.{City, Habitat, Poacher}
import improbable.papi.EntityId
import improbable.papi.world.AppWorld
import improbable.papi.world.messaging.CustomMsg
import improbable.papi.worldapp.WorldApp
import improbable.player.StepData
import improbable.util.{GameSettings, LatLonPosition}

case object RequestDemand extends CustomMsg

case class TriggerPoacher(targetHabitatId: EntityId, targetDemand: Int) extends CustomMsg

case class Expenditure(amount: Int) extends CustomMsg

case class InvestmentInPlayer(amount: Int) extends CustomMsg

case class Trade(poacherPosition: LatLonPosition, numberOfElephants: Int)

case class Trades(trades: List[Trade]) extends CustomMsg

case object Step extends CustomMsg

class SimulationSpawner(appWorld: AppWorld, logger: Logger) extends WorldApp {

  var cities: Map[EntityId, LatLonPosition] = Map.empty
  var poachers: Map[EntityId, LatLonPosition] = Map.empty
  var habitats: Map[EntityId, LatLonPosition] = Map.empty

  var cityReplies: Map[EntityId, Int] = Map.empty
  var poacherReplies: Map[EntityId, Int] = Map.empty

  spawnSimulation()
  listenToStepRequest()
  listenToCityDemandsAndBeginPoaching()
  listenToPoacherResponse()
  listenToDeadPoachers()

  def spawnSimulation(): Unit = {
    logger.info("spawning simulation")
    appWorld.entities.spawnEntity(improbable.natures.Map())
    logger.info("Spawned map")
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
        val poacherId = appWorld.entities.spawnEntity(Poacher(new LatLonPosition(poacher._2(0), poacher._2(1)), poacher._1))
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
      case msg@StepRequest(playerId, stepData) =>
        logger.info("")
        logger.info("NEW TURN")
        logger.info("")
        logger.info("step request received " + msg)
        sendMoneyToPlayer(msg)
        triggerChangeInCityDemand()
        triggerReproductionInHabitats()
        spendExpenditure(stepData)
        requestDemandsFromCities()
    }
  }

  def triggerChangeInCityDemand(): Unit = {
    cities.foreach { case (entityId, _) => appWorld.messaging.sendToEntity(entityId, Step) }
  }

  def triggerReproductionInHabitats(): Unit = {
    habitats.foreach { case (entityId, _) => appWorld.messaging.sendToEntity(entityId, Step) }
  }

  def sendMoneyToPlayer(stepRequest: StepRequest): Unit = {
    appWorld.messaging.sendToEntity(stepRequest.playerId, InvestmentInPlayer(GameSettings.moneyPerTurn - stepRequest.step.expenditures.map(_.amount).sum))
  }

  def spendExpenditure(stepData: StepData): Unit = {
    stepData.expenditures.foreach {
      expenditure => appWorld.messaging.sendToEntity(expenditure.target, Expenditure(expenditure.amount))
    }
  }

  def requestDemandsFromCities(): Unit = {
    cities.keys.foreach(cityId => appWorld.messaging.sendToEntity(cityId, RequestDemand))
  }

  def listenToCityDemandsAndBeginPoaching() = {
    appWorld.messaging.onReceive {
      case msg@CityDemand(cityId, demand) =>
        logger.info("city demand received " + msg)
        cityReplies = cityReplies + (cityId -> demand)
        if (cityReplies.size == cities.size) {
          logger.info("cities size: " + cities.size.toString)
          messagePoachers()
        }
    }
  }

  def messagePoachers(): Unit = {
    logger.info("poachers size: " + poachers.size.toString)
        if (poachers.size == 0) {
    val demandPerPoacher = cityReplies.values.sum / poachers.size

    poachers.foreach {
      case (poacherId, poacherPosition) =>
        val nearestHabitatToPoacher = habitats.minBy { case (_, habitatPosition) => habitatPosition.distanceTo(poacherPosition) }._1
        appWorld.messaging.sendToEntity(poacherId, TriggerPoacher(nearestHabitatToPoacher, demandPerPoacher))
    }

        }
    cityReplies = Map.empty
  }

  def listenToPoacherResponse() = {
    appWorld.messaging.onReceive {
      case msg@PoacherResponse(poacherId, killedElephants) =>
        poacherReplies = poacherReplies + (poacherId -> killedElephants)
        if (poacherReplies.size == poachers.size) {
          sendTradesMessages()
        }
    }


  }

  def sendTradesMessages(): Unit = {
    val poachersToNearestCities = poachers.map {
      case (poacherId, poacherPosition) =>
        val closestCity = cities.minBy(_._2.distanceTo(poacherPosition))._1
        poacherId -> closestCity
    }

    val citiesToPoachers = cities.keys.map {
      cityId =>
        val poachersClosestToCity = poachersToNearestCities.filter(_._2 == cityId).keys.toList
        cityId -> poachersClosestToCity
    }.toMap

    citiesToPoachers.foreach {
      case (cityId, poacherIds) =>
        val trades = poacherIds.map {
          poacherId =>
            val poacherPosition = poachers(poacherId)
            Trade(poacherPosition, poacherReplies.getOrElse(poacherId, 0))
        }
        appWorld.messaging.sendToEntity(cityId, Trades(trades))
    }

    poacherReplies = Map.empty

  }

  def listenToDeadPoachers() = {
    appWorld.messaging.onReceive {
      case msg@PoacherDead(poacherId) =>
        logger.info("poacher dead received " + msg)
        poachers = poachers - poacherId
        poacherReplies = poacherReplies - poacherId
    }
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
